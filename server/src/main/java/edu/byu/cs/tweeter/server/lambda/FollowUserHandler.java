package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.server.service.FollowEventServiceImpl;

public class FollowUserHandler implements RequestHandler<FollowUserRequest, FollowUserResponse> {
    @Override
    public FollowUserResponse handleRequest(FollowUserRequest request, Context context) {
        FollowEventServiceImpl service = new FollowEventServiceImpl();
        FollowUserResponse response = null;
        try {
            response = service.followUser(request);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FollowUserResponse(false);
    }
}
