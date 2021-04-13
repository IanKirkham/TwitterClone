package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.server.service.UserServiceImpl;

public class FolloweeCountHandler implements RequestHandler<GetCountRequest, GetCountResponse> {
    @Override
    public GetCountResponse handleRequest(GetCountRequest request, Context context) {
        UserServiceImpl service = new UserServiceImpl();
        try {
            return service.getFolloweeCount(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GetCountResponse(0);
    }
}