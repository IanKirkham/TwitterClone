package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
//import edu.byu.cs.tweeter.server.dao.StatusesDAO;

public class PostServiceImpl implements PostService {
    @Override
    public PostResponse savePost(PostRequest request) {
        //return getStatusesDAO().savePost(request);
        return null;
    }

    // TODO: I think we are going to get rid of the statusesDAO, not sure how we are going to do this with
    //  sqs yet but I think we need to access multiple DAO's here like story, feed, follows, etc.

//    public StatusesDAO getStatusesDAO() {
//        return new StatusesDAO();
//    }
}
