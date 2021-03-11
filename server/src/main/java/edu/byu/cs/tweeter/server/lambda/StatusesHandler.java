package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.server.service.StatusesServiceImpl;

public class StatusesHandler implements RequestHandler<StatusesRequest, StatusesResponse> {
    @Override
    public StatusesResponse handleRequest(StatusesRequest request, Context context) {
        StatusesServiceImpl service = new StatusesServiceImpl();
        return service.getStatuses(request);
    }
}
