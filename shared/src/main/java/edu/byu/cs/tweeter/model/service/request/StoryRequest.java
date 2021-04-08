package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class StoryRequest extends StatusesRequest {
    public StoryRequest(String userAlias, int limit, String lastStatus) {
        super(userAlias, limit, lastStatus);
    }
    public StoryRequest() {
        super();
    }
}
