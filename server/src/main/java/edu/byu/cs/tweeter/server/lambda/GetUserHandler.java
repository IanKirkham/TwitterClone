package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.service.UserServiceImpl;

public class GetUserHandler implements RequestHandler<UserRequest, UserResponse> {
    @Override
    public UserResponse handleRequest(UserRequest request, Context context) {
        UserServiceImpl service = new UserServiceImpl();
        try {
            return service.getUser(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserResponse("[Internal Error] Failed to retrieve user", null);
    }
}
