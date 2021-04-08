package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.DoesFollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

/**
 * A DAO for accessing 'user' data from the database.
 */
public class UserDAO {
    private static final AuthDAO authDAO = new AuthDAO();
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable("user");

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
        return new LoginResponse(user, authDAO.generateTokenForUser(user));
    }

    public RegisterResponse register(RegisterRequest request) {

        // make sure a user with the same alias doesn't exist
        if (readUser(table, request.getUsername()) != null) {
            return new RegisterResponse("Username already exists");
        }

        // create user
        Item item;
        try {
            item = createUser(table, request);

            User user = new User(
                    item.get("firstName").toString(),
                    item.get("lastName").toString(),
                    item.get("alias").toString(),
                    item.get("imageURL").toString()
            );
            AuthToken authToken = authDAO.generateTokenForUser(user);

            return new RegisterResponse(user, authToken);

        } catch (Exception e) {
            throw new RuntimeException("[Internal Error] Please try again");
        }
    }

    public static Item createUser(Table table, RegisterRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Item item = new Item()
                .withPrimaryKey("alias", request.getUsername())
                .withString("firstName", request.getFirstName())
                .withString("lastName", request.getLastName())
                .withString("password", generateStrongPasswordHash(request.getPassword()))
                .withString("imageURL", "dummy url"); // TODO upload image bytes to s3 and put URL here

        return table.putItem(item).getItem();
    }

    public static Item readUser(Table table, String alias) {
        return table.getItem("alias", alias);
    }


//    //TODO:  move out of this class
//    public FollowUserResponse followUser(FollowUserRequest request) {
//        // TODO: Modify user1's followers, user2's following in the table
//        // TODO: Authenticate the request
//        return new FollowUserResponse(true, "Successfully followed user");
//    }
//
//    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
//        // TODO: Modify user12's followers, user1's following in the table
//        // TODO: Authenticate the request
//        return new UnfollowUserResponse(true, "Successfully unfollowed user");
//    }

    /**
     * Generate strong hashed and salted password using PBKDF2
     * @param password password from register request
     * @return hashed + salted password
     * @throws NoSuchAlgorithmException exception
     * @throws InvalidKeySpecException exception
     */
    private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
}
