package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserIntegrationTest {
    private static UserServiceProxy proxy;
    private static UserRequest request;
    private static UserResponse response;

    @BeforeAll
    static void setup() {
        proxy = new UserServiceProxy();
        List<String> userAliases = new ArrayList<>();
        userAliases.add("@AllenAnderson");
        userAliases.add("@AmyAmes");
        userAliases.add("@BobBobson");

        String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
        String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

        User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
        User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
        User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);

        request = new UserRequest(userAliases, 3, null);
        response = new UserResponse(Arrays.asList(user1, user2, user3), false);
    }


    @Test
    void getUsersTest() throws IOException, TweeterRemoteException {
        UserResponse userResponse = proxy.getUsers(request);
        assertEquals(response, userResponse);
    }
}
