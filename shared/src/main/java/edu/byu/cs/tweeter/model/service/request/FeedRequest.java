package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedRequest extends StatusesRequest {
    public FeedRequest(String userAlias, int limit, String lastStatusKey) {
        super(userAlias, limit, lastStatusKey);
    }
    public FeedRequest() {
        super();
    }
}
