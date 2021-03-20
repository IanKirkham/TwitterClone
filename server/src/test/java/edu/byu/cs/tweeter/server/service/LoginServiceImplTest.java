package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImplTest {

    private LoginRequest request;
    private LoginResponse expectedResponse;
    private UserDAO mockUserDAO;
    private LoginServiceImpl loginServiceImplSpy;

    @BeforeEach
    public void setup() {
        User testUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        request = new LoginRequest("TestUser", "password");

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new LoginResponse(testUser, new AuthToken());
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.login(request)).thenReturn(expectedResponse);

        loginServiceImplSpy = Mockito.spy(LoginServiceImpl.class);
        Mockito.when(loginServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    /**
     * Verify that the {@link LoginServiceImpl#login(LoginRequest)}
     * method returns the same result as the {@link UserDAO} class.
     */
    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceImplSpy.login(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
