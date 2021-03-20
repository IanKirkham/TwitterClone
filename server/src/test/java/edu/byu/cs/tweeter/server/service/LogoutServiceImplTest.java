package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.AuthDAO;

public class LogoutServiceImplTest {

    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private AuthDAO mockAuthDAO;
    private LogoutServiceImpl logoutServiceImplSpy;

    @BeforeEach
    public void setup() {
        User testUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        request = new LogoutRequest(testUser, new AuthToken());

        // Setup a mock AuthDAO that will return known responses
        expectedResponse = new LogoutResponse(true, "Successfully logged out");
        mockAuthDAO = Mockito.mock(AuthDAO.class);
        Mockito.when(mockAuthDAO.logout(request)).thenReturn(expectedResponse);

        logoutServiceImplSpy = Mockito.spy(LogoutServiceImpl.class);
        Mockito.when(logoutServiceImplSpy.getAuthDAO()).thenReturn(mockAuthDAO);
    }

    /**
     * Verify that the {@link LogoutServiceImpl#logout(LogoutRequest)}
     * method returns the same result as the {@link AuthDAO} class.
     */
    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceImplSpy.logout(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
