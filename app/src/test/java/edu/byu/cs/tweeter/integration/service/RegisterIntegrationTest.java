package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterIntegrationTest {
    private static RegisterServiceProxy proxy;
    private static RegisterRequest request;
    private static User user1;
    private static String userName;

    @BeforeAll
    static void setup() {
        proxy = new RegisterServiceProxy();
        user1 = new User("Delete", "Me",  "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        userName = UUID.randomUUID().toString().substring(0, 8);
        byte[] imageBytes = {'A', 'B'};
        request = new RegisterRequest("Delete","Me", userName,"123", imageBytes);
    }

    @Test
    void registerTest() throws IOException, TweeterRemoteException {
        RegisterResponse registerResponse = proxy.register(request);
        assertEquals(user1.getFirstName(), registerResponse.getUser().getFirstName());
        assertEquals(user1.getLastName(), registerResponse.getUser().getLastName());
        assertEquals(userName, registerResponse.getUser().getAlias());
        assertNotNull(registerResponse.getAuthToken());
        assertNotNull(registerResponse.getAuthToken().getToken());
        assertEquals(userName, registerResponse.getAuthToken().getAlias());
    }
}
