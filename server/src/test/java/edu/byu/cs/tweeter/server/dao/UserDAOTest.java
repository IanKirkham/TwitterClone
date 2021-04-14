package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.imageio.ImageIO;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class UserDAOTest {

    private UserDAO userDAO;
    private User testUser;

    @BeforeEach
    void setup() {
        userDAO = new UserDAO();
        testUser = new User("Test", "User", "@TestUser", "");
    }

    @Test
    void testLogin_successResponse() {
        LoginRequest loginRequest = new LoginRequest("@TestUser", "123");
        LoginResponse loginResponse = userDAO.login(loginRequest);

        Assertions.assertTrue(loginResponse.isSuccess());
    }

    @Test
    void testLogin_failureResponse() {
        LoginRequest loginRequest = new LoginRequest("@FakeUsername", "fakepassword");
        LoginResponse loginResponse = userDAO.login(loginRequest);

        Assertions.assertFalse(loginResponse.isSuccess());
    }

    @Test
    void testRegister_successResponse() throws IOException {

        URL imageURL = new URL("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        BufferedImage image = ImageIO.read(imageURL);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bos);
        byte[] data = bos.toByteArray();

        RegisterRequest registerRequest = new RegisterRequest("New", "User", "@NewUser", "123", Base64.getEncoder().encode(data));
        RegisterResponse registerResponse = userDAO.register(registerRequest);

        Assertions.assertTrue(registerResponse.isSuccess());
    }

    @Test
    void testRegister_failureResponse() {
        // Attempt to register a user that is already registered
        RegisterRequest registerRequest = new RegisterRequest("Test", "User", "@TestUser", "123", null);
        RegisterResponse registerResponse = userDAO.register(registerRequest);

        Assertions.assertFalse(registerResponse.isSuccess());
    }

    @Test
    void testGetUser_successResponse() {
        User user = userDAO.getUser(testUser.getAlias());
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getAlias(), testUser.getAlias());
    }

    @Test
    void testGetUser_failureResponse() {
        User user = userDAO.getUser("dummyalias");
        Assertions.assertNull(user);
    }

    @Test
    void testGeneratePasswordHash_returnsUniqueHash() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password1 = UserDAO.generateStrongPasswordHash("password");
        String password2 = UserDAO.generateStrongPasswordHash("password");

        Assertions.assertNotEquals(password1, password2);
    }
}