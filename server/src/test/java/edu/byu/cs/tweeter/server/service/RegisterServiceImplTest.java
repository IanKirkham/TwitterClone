package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImplTest {

    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private UserDAO mockUserDAO;
    private RegisterServiceImpl register1ServiceImplSpy;

    @BeforeEach
    public void setup() {
        User testUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        request = new RegisterRequest("Test", "User", "TestUser", "password", new byte[0]);

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new RegisterResponse(testUser, new AuthToken());
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.register(request)).thenReturn(expectedResponse);

        register1ServiceImplSpy = Mockito.spy(RegisterServiceImpl.class);
        Mockito.when(register1ServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    /**
     * Verify that the {@link RegisterServiceImpl#register(RegisterRequest)}
     * method returns the same result as the {@link UserDAO} class.
     */
    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = register1ServiceImplSpy.register(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
