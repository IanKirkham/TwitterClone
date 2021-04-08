package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class StoryRequest extends StatusesRequest {
    public StoryRequest(String userAlias, int limit, String lastStatus, AuthToken authToken) {
        super(userAlias, limit, lastStatus, authToken);
    }
    public StoryRequest() {
        super();
    }
}
