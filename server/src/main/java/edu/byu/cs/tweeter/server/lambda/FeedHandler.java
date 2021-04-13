package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.net.GsonFactory;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.server.service.StatusesServiceImpl;

public class FeedHandler implements RequestStreamHandler {
    public static final Gson gson = GsonFactory.gson;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        FeedRequest request = gson.fromJson(result, FeedRequest.class);

        StatusesServiceImpl service = new StatusesServiceImpl();
        StatusesResponse response = null;
        try {
            response = service.getFeed(request);
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
        }
        String responseJSON = gson.toJson(response);
        outputStream.write(responseJSON.getBytes(StandardCharsets.UTF_8));
    }
}