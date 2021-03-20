package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterPresenterTest {

    private RegisterRequest registerRequest;
    private RegisterResponse registerResponse;
    private RegisterServiceProxy mockRegisterService;
    private RegisterPresenter presenter;

    private boolean viewWasCalled = false;

    @BeforeEach
    public void setup() throws Exception {
        User testUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        registerRequest = new RegisterRequest("Test", "User", "TestUser", "password", new byte[0]);
        registerResponse = new RegisterResponse(testUser, new AuthToken());

        // Create a mock RegisterService
        mockRegisterService = Mockito.mock(RegisterServiceProxy.class);
        Mockito.when(mockRegisterService.register(registerRequest)).thenReturn(registerResponse);

        // Wrap a RegisterPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new RegisterPresenter(new RegisterPresenter.View() {
            public void registerSuccessful(RegisterResponse registerResponse) {
                viewWasCalled = true;
            }
            public void registerUnsuccessful(RegisterResponse registerResponse) {
                viewWasCalled = true;
            }
            public void handleException(Exception exception) {}
        }));
        Mockito.when(presenter.getRegisterService()).thenReturn(mockRegisterService);

        viewWasCalled = false;
    }

    @Test
    public void testRegister_returnsServiceResult() throws Exception {
        Mockito.when(mockRegisterService.register(registerRequest)).thenReturn(registerResponse);

        Assertions.assertEquals(registerResponse, presenter.register(registerRequest));
    }

    @Test
    public void testViewMethod_wasCalled() throws Exception {
        presenter.registerSuccessful(registerResponse);
        Assertions.assertTrue(viewWasCalled);
    }

    @Test
    public void testRegister_serviceThrowsIOException_presenterThrowsIOException() throws Exception {
        Mockito.when(mockRegisterService.register(registerRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.register(registerRequest);
        });
    }
}
