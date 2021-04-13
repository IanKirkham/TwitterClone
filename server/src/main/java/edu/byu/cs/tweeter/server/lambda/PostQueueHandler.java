package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.net.GsonFactory;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;

public class PostQueueHandler implements RequestHandler<SQSEvent, Void> {
    public static final String qURL = "https://sqs.us-west-2.amazonaws.com/036224226136/jobsQ";
    public static final Gson gson = GsonFactory.gson;
    public static final FollowsDAO followsDAO = new FollowsDAO();


    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String postJSON = msg.getBody();
            PostRequest request = gson.fromJson(postJSON, PostRequest.class);

            List<String> userAliases = followsDAO.getFollowers(request.getAuthor());

            for (int i = 0; i < userAliases.size(); i += 25) {
                List<String> subList;
                if (userAliases.size() - i < 25) {
                    subList = userAliases.subList(i, userAliases.size());
                } else {
                    subList = userAliases.subList(i, i + 25);
                }
                FeedUpdateJob job = new FeedUpdateJob(subList, request.getAuthor(), request.getTimePublished().toString(), request.getContent());
                String jobJSON = gson.toJson(job);
                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(qURL)
                        .withMessageBody(jobJSON);

                AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
                sqs.sendMessage(send_msg_request);
            }
        }
        return null;
    }
}