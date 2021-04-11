package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowEventRequest extends AuthenticatedRequest {
    private String primaryUserAlias;
    private String currentUserAlias;
    private String primaryUserName;
    private String currentUserName;

    public FollowEventRequest(String primaryUserAlias, AuthToken authToken, String currentUserAlias, String primaryUserName, String currentUserName) {
        super(authToken);
        this.primaryUserAlias = primaryUserAlias;
        this.currentUserAlias = currentUserAlias;
        this.primaryUserName = primaryUserName;
        this.currentUserName = currentUserName;
    }

    public FollowEventRequest() {}

    public String getPrimaryUserAlias() {
        return primaryUserAlias;
    }

    public String getCurrentUserAlias() {
        return currentUserAlias;
    }

    public void setPrimaryUserAlias(String primaryUserAlias) {
        this.primaryUserAlias = primaryUserAlias;
    }

    public void setCurrentUserAlias(String currentUserAlias) {
        this.currentUserAlias = currentUserAlias;
    }

    public String getPrimaryUserName() {
        return primaryUserName;
    }

    public void setPrimaryUserName(String primaryUserName) {
        this.primaryUserName = primaryUserName;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }
}
