package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;

public class PostQueueHandler implements RequestHandler<SQSEvent, Void> {
    public static String qURL = "https://sqs.us-west-2.amazonaws.com/036224226136/jobsQ";
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    JsonObject jo = json.getAsJsonObject();
                    if (jo.get("year") != null) {
                        return LocalDateTime.of(jo.get("year").getAsInt(),
                                jo.get("monthValue").getAsInt(),
                                jo.get("dayOfMonth").getAsInt(),
                                jo.get("hour").getAsInt(),
                                jo.get("minute").getAsInt(),
                                jo.get("second").getAsInt(),
                                jo.get("nano").getAsInt());
                    } else {
                        JsonObject d = jo.getAsJsonObject("date");
                        JsonObject t = jo.getAsJsonObject("time");
                        return LocalDateTime.of(d.get("year").getAsInt(),
                                d.get("month").getAsInt(),
                                d.get("day").getAsInt(),
                                t.get("hour").getAsInt(),
                                t.get("minute").getAsInt(),
                                t.get("second").getAsInt(),
                                t.get("nano").getAsInt());
                    }
                }
            }).create();

    public static FollowsDAO followsDAO = new FollowsDAO();


    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String postJSON = msg.getBody();
            PostRequest request = gson.fromJson(postJSON, PostRequest.class);

            List<String> userAliases = followsDAO.getFollowers(request.getAuthor());
            System.out.println(userAliases); // TODO: Delete me

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
                SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
            }
        }
        return null;
    }
}