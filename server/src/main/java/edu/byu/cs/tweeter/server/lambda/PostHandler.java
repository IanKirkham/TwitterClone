package edu.byu.cs.tweeter.server.lambda;


import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.service.PostServiceImpl;

public class PostHandler implements RequestStreamHandler  {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        System.out.println(result);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        JsonObject jo = json.getAsJsonObject();
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
                }).create();

        PostRequest request = gson.fromJson(result, PostRequest.class);
        System.out.println("Time published before additional read " + request.getTimePublished());

        PostServiceImpl service = new PostServiceImpl();
        PostResponse response = service.savePost(request);
        String responseJSON = gson.toJson(response);
        System.out.println(responseJSON);
        outputStream.write(responseJSON.getBytes(StandardCharsets.UTF_8));
    }
}
