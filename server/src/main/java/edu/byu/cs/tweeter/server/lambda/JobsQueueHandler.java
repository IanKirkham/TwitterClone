package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
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

import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class JobsQueueHandler implements RequestHandler<SQSEvent, Void> {
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

    public static FeedDAO feedDAO = new FeedDAO();

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