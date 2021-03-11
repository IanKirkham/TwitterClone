package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostServiceImpl implements PostService {
    @Override
    public PostResponse savePost(PostRequest request) {
        Status status = new Status(request.getContent(), request.getAuthor(), request.getTimePublished());
        return new PostResponse(status);
    }
}
