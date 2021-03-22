package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterIntegrationTest {
    private static RegisterServiceProxy proxy;
    private static RegisterRequest request;
    private static User user1;
    private static AuthToken authToken;

    private static RegisterResponse response;

    @BeforeAll
    static void setup() {
        proxy = new RegisterServiceProxy();
        user1 = new User("Test", "User",  "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        authToken = new AuthToken("8af4d1be-f1fa-40a6-b56d-f741a31f8421");

        request = new RegisterRequest("Test","User","Test","User",null);
        response = new RegisterResponse(user1, authToken);
    }

    @Test
    void registerTest() throws IOException, TweeterRemoteException {
        RegisterResponse registerResponse = proxy.register(request);
        assertEquals(response, registerResponse);
    }
}
