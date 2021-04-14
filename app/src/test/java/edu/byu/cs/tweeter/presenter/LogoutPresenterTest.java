package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.client.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutPresenterTest {

    private LogoutRequest logoutRequest;
    private LogoutResponse logoutResponse;
    private LogoutServiceProxy mockLogoutService;
    private LogoutPresenter presenter;

    private boolean viewWasCalled = false;

    @BeforeEach
    public void setup() throws Exception {
        User testUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        logoutRequest = new LogoutRequest(testUser.getAlias(), new AuthToken());
        logoutResponse = new LogoutResponse(true, "Successfully logged out");

        // Create a mock LogoutService
        mockLogoutService = Mockito.mock(LogoutServiceProxy.class);
        Mockito.when(mockLogoutService.logout(logoutRequest)).thenReturn(logoutResponse);

        // Wrap a LogoutPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new LogoutPresenter(new LogoutPresenter.View() {
            public void logoutSuccessful(LogoutResponse logoutResponse) {
                viewWasCalled = true;
            }
            public void logoutUnsuccessful(LogoutResponse logoutResponse) {
                viewWasCalled = true;
            }
            public void handleException(Exception ex) {}
        }));
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutService);

        viewWasCalled = false;
    }

    @Test
    public void testLogout_returnsServiceResult() throws Exception {
        Mockito.when(mockLogoutService.logout(logoutRequest)).thenReturn(logoutResponse);

        Assertions.assertEquals(logoutResponse, presenter.logout(logoutRequest));
    }

    @Test
    public void testViewMethod_wasCalled() throws Exception {
        presenter.logoutSuccessful(logoutResponse);
        Assertions.assertTrue(viewWasCalled);
    }

    @Test
    public void testLogout_serviceThrowsIOException_presenterThrowsIOException() throws Exception {
        Mockito.when(mockLogoutService.logout(logoutRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.logout(logoutRequest);
        });
    }
}
