package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;

/**
 * The presenter for the logout functionality of the application.
 */
public class LogoutPresenter implements LogoutTask.Observer {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        void logoutSuccessful(LogoutResponse logoutResponse);
        void logoutUnsuccessful(LogoutResponse logoutResponse);
        void handleException(Exception ex);
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
    public LogoutResponse logout(LogoutRequest logoutRequest) throws IOException, TweeterRemoteException {
        LogoutService logoutService = getLogoutService();
        return logoutService.logout(logoutRequest);
    }

    @Override
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        if (view != null) {
            view.logoutSuccessful(logoutResponse);
        }
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
        if (view != null) {
            view.logoutUnsuccessful(logoutResponse);
        }
    }

    @Override
    public void handleException(Exception ex) {
        if (view != null) {
            view.handleException(ex);
        }
    }

    /**
     * Returns an instance of {@link LogoutService}. Allows mocking of the LogoutService class
     * for testing purposes. All usages of LogoutService should get their LogoutService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public LogoutService getLogoutService() {
        return new LogoutServiceProxy();
    }
}
