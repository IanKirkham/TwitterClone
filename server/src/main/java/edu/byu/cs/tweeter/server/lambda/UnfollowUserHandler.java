package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.server.service.FollowEventServiceImpl;

public class UnfollowUserHandler implements RequestHandler<UnfollowUserRequest, UnfollowUserResponse> {
    @Override
    public UnfollowUserResponse handleRequest(UnfollowUserRequest request, Context context) {
        FollowEventServiceImpl service = new FollowEventServiceImpl();
        try {
            UnfollowUserResponse response = service.unfollowUser(request);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UnfollowUserResponse(false);
    }
}
