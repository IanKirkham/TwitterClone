package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserServiceImplTest {

    private UserRequest request;
    private UserResponse expectedResponse;
    private UserDAO mockUserDAO;
    private UserServiceImpl userServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        currentUser.setFollowers(new ArrayList<>(Arrays.asList(resultUser1.getAlias(), resultUser2.getAlias())));
        currentUser.setFollowees(new ArrayList<>(Arrays.asList(resultUser1.getAlias(), resultUser2.getAlias(), resultUser3.getAlias())));

        // Setup request objects to use in the tests
        request = new UserRequest(currentUser.getFollowers(), 2, null);

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new UserResponse(Arrays.asList(resultUser1, resultUser2), false);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getUsers(request)).thenReturn(expectedResponse);

        userServiceImplSpy = Mockito.spy(UserServiceImpl.class);
        Mockito.when(userServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    /**
     * Verify that the {@link UserServiceImpl#getUsers(UserRequest)}
     * method returns the same result as the {@link UserDAO} class.
     */
    @Test
    public void testGetUsers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceImplSpy.getUsers(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}

