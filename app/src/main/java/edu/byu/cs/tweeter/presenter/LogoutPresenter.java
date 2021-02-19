package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

/**
 * The presenter for the logout functionality of the application.
 */
public class LogoutPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LogoutPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a logout request.
     *
     * @param logoutRequest the request.
     */
    public LogoutResponse login(LogoutRequest logoutRequest) throws IOException {
        LogoutService logoutService = new LogoutService();
        return logoutService.logout(logoutRequest);
    }
}
