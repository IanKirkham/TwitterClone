package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.service.UserServiceImpl;

public class FolloweeHandler implements RequestHandler<FolloweeRequest, UserResponse> {
    @Override
    public UserResponse handleRequest(FolloweeRequest request, Context context) {
        UserServiceImpl service = new UserServiceImpl();
        try {
            return service.getFollowees(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserResponse("[Internal Error] Failed to get followees", "");
    }
}
