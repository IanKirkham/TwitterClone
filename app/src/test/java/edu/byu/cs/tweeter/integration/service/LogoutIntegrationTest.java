package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogoutIntegrationTest {
    private static LogoutServiceProxy proxy;
    private static LogoutRequest request;
    private static User user1;
    private static AuthToken authToken;

    private static LogoutResponse response;

    @BeforeAll
    static void setup() {
        proxy = new LogoutServiceProxy();
        user1 = new User("Test", "User",  "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        authToken = new AuthToken("8af4d1be-f1fa-40a6-b56d-f741a31f8421");

        request = new LogoutRequest(user1, authToken);
        response = new LogoutResponse(true, "Logout successful");
    }

    @Test
    void LogoutTest() throws IOException, TweeterRemoteException {
        LogoutResponse logoutResponse = proxy.logout(request);
        assertEquals(response, logoutResponse);
    }
}
