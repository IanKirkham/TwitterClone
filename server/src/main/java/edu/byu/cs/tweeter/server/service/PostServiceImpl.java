package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.StatusesDAO;

public class PostServiceImpl implements PostService {
    @Override
    public PostResponse savePost(PostRequest request) {
        return getStatusesDAO().savePost(request);
    }

    public StatusesDAO getStatusesDAO() {
        return new StatusesDAO();
    }
}
