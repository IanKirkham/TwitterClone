package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * A DAO for accessing 'user' data from the database.
 */
public class UserDAO {

    // Table name
    private static final String tableName = "user";

    // DynamoDB client
    private static final AuthDAO authDAO = new AuthDAO();
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable(tableName);

    // s3
    private static final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-west-2").build();
    private static final String bucketName = "tweeter-user-pics";


    public LoginResponse login(LoginRequest request) {

        // Retrieve user from the database
        Item retrievedUser = readUser(table, request.getUsername());
        if (retrievedUser == null) {
            return new LoginResponse("Invalid username or password");
        }

        String generatedSecuredPasswordHash = retrievedUser.getString("password");
        boolean matched = false;
        try {
            matched = validatePassword(request.getPassword(), generatedSecuredPasswordHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("[Internal Error] Please try again");
        }

        if (!matched) {
            return new LoginResponse("Invalid username or password");
        }

        User user = new User(
                retrievedUser.get("firstName").toString(),
                retrievedUser.get("lastName").toString(),
                retrievedUser.get("alias").toString(),
                retrievedUser.get("imageURL").toString()
        );
        AuthToken authToken = authDAO.generateTokenForUser(user);

        return new LoginResponse(user, authToken);
    }

    public RegisterResponse register(RegisterRequest request) {

        // make sure a user with the same alias doesn't exist
        if (readUser(table, request.getUsername()) != null) {
            return new RegisterResponse("Username already exists");
        }

        User user = createUser(table, request);
        AuthToken authToken = authDAO.generateTokenForUser(user);

        return new RegisterResponse(user, authToken);
    }

    public User createUser(Table table, RegisterRequest request) {

        // decode base64 byte array
        byte[] decodedByteArray = Base64.getDecoder().decode(request.getImageBytes());

        // upload image to s3 bucket
        String imageURL = uploadToS3(decodedByteArray, request.getUsername());

        Item item = null;
        try {
            item = new Item()
                    .withPrimaryKey("alias", request.getUsername())
                    .withString("firstName", request.getFirstName())
                    .withString("lastName", request.getLastName())
                    .withString("password", generateStrongPasswordHash(request.getPassword()))
                    .withString("imageURL", imageURL);
        } catch (Exception e) {
            throw new RuntimeException("[Internal Error] An error occurred while registering");
        }

        table.putItem(item);
        return new User(
                item.get("firstName").toString(),
                item.get("lastName").toString(),
                item.get("alias").toString(),
                item.get("imageURL").toString()
        );
    }

    public String uploadToS3(byte[] byteArray, String user) {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        String key = user.substring(1) + ".jpg";

        s3.putObject(new PutObjectRequest(bucketName, key, bis, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3.getUrl(bucketName, key).toString();
    }

    public UserResponse getUser_Authenticated(UserRequest request) {
        if (!AuthDAO.isValidTokenForUser(request.getAuthToken().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }
        User user = getUser(request.getUserAlias());
        return new UserResponse(user, null, false, null);
    }

    public User getUser(String alias) {
        Item item = table.getItem("alias", alias);
        if (item == null) {
            return null;
        }
        return new User(
                item.get("firstName").toString(),
                item.get("lastName").toString(),
                item.get("alias").toString(),
                item.get("imageURL").toString()
        );
    }

    public static Item readUser(Table table, String alias) {
        return table.getItem("alias", alias);
    }

    /**
     * Generate strong hashed and salted password using PBKDF2
     * @param password password from register request
     * @return hashed + salted password
     * @throws NoSuchAlgorithmException exception
     * @throws InvalidKeySpecException exception
     */
    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Validate password using PBKDF2 hashing algorithm
     * @param originalPassword password provided by user
     * @param storedPassword securely hashed password for a given username
     * @return boolean for valid password or not
     * @throws NoSuchAlgorithmException exception
     * @throws InvalidKeySpecException exception
     */
    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public void addUserBatch(List<User> users) {

        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(tableName);

        // Add each user into the TableWriteItems object
        for (User user : users) {
            Item item = null;
            try {
                item = new Item()
                        .withPrimaryKey("alias", user.getAlias())
                        .withString("firstName", user.getFirstName())
                        .withString("lastName", user.getLastName())
                        .withString("password", generateStrongPasswordHash("123"))
                        .withString("imageURL", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(tableName);
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }
}
