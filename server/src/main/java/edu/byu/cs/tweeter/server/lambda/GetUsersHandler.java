package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.service.UserServiceImpl;

public class GetUsersHandler implements RequestHandler<UserRequest, UserResponse> {

    @Override
    public UserResponse handleRequest(UserRequest request, Context context) {
        UserServiceImpl service = new UserServiceImpl();
        //return service.getUsers(request);
        return null;
    }
}
