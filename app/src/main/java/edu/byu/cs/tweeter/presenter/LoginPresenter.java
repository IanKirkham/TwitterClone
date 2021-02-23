package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;

/**
 * The presenter for the login functionality of the application.
 */
public class LoginPresenter implements LoginTask.Observer {

    private final View view;

    @Override
    public void loginSuccessful(LoginResponse loginResponse) {
        if (view != null) {
            view.loginSuccessful(loginResponse);
        }
    }

    @Override
    public void loginUnsuccessful(LoginResponse loginResponse) {
        if (view != null) {
            view.loginUnsuccessful(loginResponse);
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
        void loginSuccessful(LoginResponse loginResponse);
        void loginUnsuccessful(LoginResponse loginResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LoginPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a login request.
     *
     * @param loginRequest the request.
     */
    public LoginResponse login(LoginRequest loginRequest) throws IOException {
        LoginService loginService = new LoginService();
        return loginService.login(loginRequest);
    }
}
