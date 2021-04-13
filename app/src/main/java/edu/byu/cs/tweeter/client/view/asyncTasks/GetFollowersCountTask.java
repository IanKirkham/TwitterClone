package edu.byu.cs.tweeter.client.view.asyncTasks;


import edu.byu.cs.tweeter.client.presenter.UserPresenter;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;

public class GetFollowersCountTask extends GetFollowCountTask {
    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve users.
     * @param observer  the observer who wants to be notified when this task completes.
     */
    public GetFollowersCountTask(UserPresenter presenter, Observer observer) {
        super(presenter, observer);
    }

    @Override
    protected GetCountResponse doInBackground(GetCountRequest... getCountRequests) {
        GetCountResponse response = null;
        try {
            response = presenter.getFollowerCount(getCountRequests[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(GetCountResponse getCountResponse) {
        observer.updateFollowerCount(getCountResponse.getCount());
    }
}
