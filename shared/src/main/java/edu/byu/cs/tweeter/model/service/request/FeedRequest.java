package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FeedRequest extends StatusesRequest {
    public FeedRequest(String userAlias, int limit, String lastStatusKey, AuthToken authToken) {
        super(userAlias, limit, lastStatusKey, authToken);
    }
    public FeedRequest() {
        super();
    }
}
