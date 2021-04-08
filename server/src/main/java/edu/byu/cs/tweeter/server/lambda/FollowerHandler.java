package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.service.UserServiceImpl;

public class FollowerHandler implements RequestHandler<FollowerRequest, UserResponse> {
    @Override
    public UserResponse handleRequest(FollowerRequest request, Context context) {
        UserServiceImpl service = new UserServiceImpl();
        try {
            return service.getFollowers(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserResponse("[Internal Error] Failed to get followers", "");
    }
}
