package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.net.GsonFactory;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.service.PostServiceImpl;

public class PostHandler implements RequestStreamHandler  {
    public static final Gson gson = GsonFactory.gson;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        PostRequest request = gson.fromJson(result, PostRequest.class);

        PostServiceImpl service = new PostServiceImpl();
        PostResponse response = service.savePost(request);
        String responseJSON = gson.toJson(response);
        outputStream.write(responseJSON.getBytes(StandardCharsets.UTF_8));
    }
}
