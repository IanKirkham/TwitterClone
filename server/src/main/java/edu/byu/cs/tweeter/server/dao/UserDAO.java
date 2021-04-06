package edu.byu.cs.tweeter.server.dao;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * A DAO for accessing 'user' data from the database.
 */
public class UserDAO {
    private static AuthDAO authDAO = new AuthDAO();

    // This is the hard coded user data returned by the 'getUsers()' method
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private static final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private static final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private static final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private static final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private static final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private static final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private static final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private static final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private static final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private static final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private static final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private static final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private static final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private static final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private static final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private static final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private static final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private static final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private static final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private static final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);

    public UserResponse getUsers(UserRequest request) {
        // TODO: Authenticate the request
        assert request.getLimit() > 0;
        assert request.getUserAliases() != null;

        List<User> allUsers = getRequestedUsers(request.getUserAliases());
        List<User> responseUsers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int userIndex = getUsersStartingIndex(request.getLastUserAlias(), allUsers);

            for (int limitCounter = 0; userIndex < allUsers.size() && limitCounter < request.getLimit(); userIndex++, limitCounter++) {
                responseUsers.add(allUsers.get(userIndex));
            }
            hasMorePages = userIndex < allUsers.size();
        }

        return new UserResponse(responseUsers, hasMorePages);
    }

    /**
     * Determines the index for the first user in the specified 'allUsers' list that should
     * be returned in the current request. This will be the index of the next user after the
     * specified 'lastUser'.
     *
     * @param lastUserAlias the alias of the last user that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allUsers the generated list of users from which we are returning paged results.
     * @return the index of the first user to be returned.
     */
    private int getUsersStartingIndex(String lastUserAlias, List<User> allUsers) {
        int usersIndex = 0;

        if (lastUserAlias != null) {
            for (int i = 0; i < allUsers.size(); i++) {
                if (lastUserAlias.equals(allUsers.get(i).getAlias())) {
                    usersIndex = i + 1;
                    break;
                }
            }
        }

        return usersIndex;
    }

    List<User> getRequestedUsers(List<String> userAliases) {
        ArrayList<User> users = new ArrayList<>(getDummyUsers());
        users.removeIf(user -> !userAliases.contains(user.getAlias()));
        return users;
    }

    public LoginResponse login(LoginRequest request) {

        String generatedSecuredPasswordHash = ""; // TODO: retrieve hashed password from database here
        boolean matched = false;
        try {
            matched = validatePassword(request.getPassword(), generatedSecuredPasswordHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        System.out.println("Passwords Match: " + matched);

        // TODO: Hash request password and lookup user's password hash in table
        // TODO: Error checking
        User user = new User("Test", "User", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        user.setFollowees(getDummyUserAliases());
        user.setFollowers(getDummyUserAliases());
        return new LoginResponse(user, authDAO.generateTokenForUser(user));
    }

    public RegisterResponse register(RegisterRequest request) {

        // TODO: Store hashed + salted password in database
        String generatedSecuredPasswordHash = null;
        try {
            generatedSecuredPasswordHash = generateStrongPasswordHash(request.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        System.out.println("Generated Password: " + generatedSecuredPasswordHash);

        // TODO: Add user to the database
        // TODO: Error checking
        User user = new User("Test", "User", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        user.setFollowees(getDummyUserAliases());
        user.setFollowers(getDummyUserAliases());
        return new RegisterResponse(user, authDAO.generateTokenForUser(user));
    }

    List<String> getDummyUserAliases() {
        ArrayList<String> userAliases = new ArrayList<>();
        getBasicDummyUsers().forEach(user -> { userAliases.add(user.getAlias()); });
        return userAliases;
    }

    List<User> getDummyUsers() {
        User user = new User("Test", "User", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        user.setFollowees(getDummyUserAliases());
        user.setFollowers(getDummyUserAliases());
        return Arrays.asList(user, user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

    List<User> getBasicDummyUsers() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

    public FollowUserResponse followUser(FollowUserRequest request) {
        // TODO: Modify user1's followers, user2's following in the table
        // TODO: Authenticate the request
        return new FollowUserResponse(true, "Successfully followed user");
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        // TODO: Modify user12's followers, user1's following in the table
        // TODO: Authenticate the request
        return new UnfollowUserResponse(true, "Successfully unfollowed user");
    }

    // TODO: should the logic for hashing / validating passwords be in the DAO or the service class?
    //  violation of Single Responsibility Principle?
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
    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
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
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
