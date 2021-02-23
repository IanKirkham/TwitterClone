package edu.byu.cs.tweeter.view.asyncTasks;

import edu.byu.cs.tweeter.model.service.response.FollowEventResponse;

public interface FollowEventTask {
    public abstract interface Observer {
        void requestSuccessful(FollowEventResponse response);
        void requestUnsuccessful(FollowEventResponse response);
        void handleException(Exception exception);
    }
}
