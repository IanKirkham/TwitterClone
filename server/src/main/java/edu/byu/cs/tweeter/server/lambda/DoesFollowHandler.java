package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.DoesFollowRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.server.service.FollowEventServiceImpl;

public class DoesFollowHandler implements RequestHandler<DoesFollowRequest, DoesFollowResponse> {
    @Override
    public DoesFollowResponse handleRequest(DoesFollowRequest request, Context context) {
        FollowEventServiceImpl service = new FollowEventServiceImpl();
        try {
            return service.doesFollowUser(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DoesFollowResponse(false);
    }
}
