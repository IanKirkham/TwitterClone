package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;

public class PostServiceImpl implements PostService {
    private static final String qURL = "https://sqs.us-west-2.amazonaws.com/036224226136/postsQ";

    @Override
    public PostResponse savePost(PostRequest request) {
        sendToSQS(request);
        return getStoryDAO().savePost(request);
    }

    public void sendToSQS(PostRequest request) {
        String postJSON = (new Gson().toJson(request));

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(qURL)
                .withMessageBody(postJSON);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(send_msg_request);
    }

    public StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
}
