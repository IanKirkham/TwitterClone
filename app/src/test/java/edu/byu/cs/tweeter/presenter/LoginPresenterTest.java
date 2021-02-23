package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginPresenterTest {

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private LoginService mockLoginService;
    private LoginPresenter presenter;

    private boolean viewWasCalled = false;

    @BeforeEach
    public void setup() throws Exception {
        User testUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        loginRequest = new LoginRequest("TestUser", "password");
        loginResponse = new LoginResponse(testUser, new AuthToken());

        // Create a mock LoginService
        mockLoginService = Mockito.mock(LoginService.class);
        Mockito.when(mockLoginService.login(loginRequest)).thenReturn(loginResponse);

        // Wrap a LoginPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new LoginPresenter(new LoginPresenter.View() {
            public void loginSuccessful(LoginResponse loginResponse) {
                viewWasCalled = true;
            }
            public void loginUnsuccessful(LoginResponse loginResponse) {
                viewWasCalled = true;
            }
            public void handleException(Exception exception) {}
        }));
        Mockito.when(presenter.getLoginService()).thenReturn(mockLoginService);

        viewWasCalled = false;
    }

    @Test
    public void testLogin_returnsServiceResult() throws Exception {
        Mockito.when(mockLoginService.login(loginRequest)).thenReturn(loginResponse);

        Assertions.assertEquals(loginResponse, presenter.login(loginRequest));
    }

    @Test
    public void testViewMethod_wasCalled() throws Exception {
        presenter.loginSuccessful(loginResponse);
        Assertions.assertTrue(viewWasCalled);
    }

    @Test
    public void testLogin_serviceThrowsIOException_presenterThrowsIOException() throws Exception {
        Mockito.when(mockLoginService.login(loginRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.login(loginRequest);
        });
    }
}
