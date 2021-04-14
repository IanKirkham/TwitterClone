package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginIntegrationTest {
    private static LoginServiceProxy proxy;
    private static LoginRequest request;
    private static User user1;

    private static LoginResponse response;

    @BeforeAll
    static void setup() {
        proxy = new LoginServiceProxy();
        user1 = new User("Test", "User",  "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new LoginRequest("@TestUser","123");
    }

    @Test
    void loginTest() throws IOException, TweeterRemoteException {
        LoginResponse loginResponse = proxy.login(request);
        assertEquals(user1, loginResponse.getUser());
        assertNotNull(loginResponse.getAuthToken());
        assertEquals(user1.getAlias(), loginResponse.getAuthToken().getAlias());
        assertNotNull(loginResponse.getAuthToken().getToken());
    }
}
