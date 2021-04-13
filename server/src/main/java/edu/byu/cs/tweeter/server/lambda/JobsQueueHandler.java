package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.net.GsonFactory;
import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class JobsQueueHandler implements RequestHandler<SQSEvent, Void> {
    public static final Gson gson = GsonFactory.gson;
    public static final FeedDAO feedDAO = new FeedDAO();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String jobJSON = msg.getBody();
            FeedUpdateJob request = gson.fromJson(jobJSON, FeedUpdateJob.class);

            List<String> userAliases = request.getUserAliases();

            feedDAO.addPostBatch(userAliases, request.author, request.timePublished, request.content);
        }
        return null;
    }
}