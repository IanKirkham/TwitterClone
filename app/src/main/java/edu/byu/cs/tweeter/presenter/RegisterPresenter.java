package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.view.asyncTasks.RegisterTask;

/**
 * The presenter for the registration functionality of the application.
 */
public class RegisterPresenter implements RegisterTask.Observer {

    private final RegisterPresenter.View view;

    @Override
    public void registerSuccessful(RegisterResponse registerResponse) {
        if (view != null) {
            view.registerSuccessful(registerResponse);
        }
    }

    @Override
    public void registerUnsuccessful(RegisterResponse registerResponse) {
        if (view != null) {
            view.registerUnsuccessful(registerResponse);
        }
    }

    @Override
    public void handleException(Exception exception) {
        if (view != null) {
            view.handleException(exception);
        }
    }

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        void registerSuccessful(RegisterResponse registerResponse);
        void registerUnsuccessful(RegisterResponse registerResponse);
        void handleException(Exception ex);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public RegisterPresenter(RegisterPresenter.View view) {
        this.view = view;
    }

    /**
     * Makes a register request.
     *
     * @param registerRequest the request.
     */
    public RegisterResponse register(RegisterRequest registerRequest) throws IOException {
        RegisterService registerService = getRegisterService();
        return registerService.register(registerRequest);
    }

    /**
     * Returns an instance of {@link RegisterService}. Allows mocking of the RegisterService class
     * for testing purposes. All usages of RegisterService should get their RegisterService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    RegisterService getRegisterService() {
        return new RegisterService();
    }
}
