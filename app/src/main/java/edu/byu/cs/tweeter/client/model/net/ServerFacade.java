package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "Insert your API invoke URL here";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);


    // TODO: move the rest of this dummy data to their respective DAO objects
    private static final User testUser = new User("Test", "User",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    private static final Status status1 = new Status("Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com", user1, LocalDateTime.now());
    private static final Status status2 = new Status("Content: Hello World!, Mentions: @BobBobson, URLs: https://www.google.com", user1, LocalDateTime.now());
    private static final Status status3 = new Status("Content: Hello World!, Mentions: @BobBobson, URLs: www.google.com", user1, LocalDateTime.now());
    private static final Status status4 = new Status("Content: Hello World!, Mentions: @BobBobson, URLs: http://www.4jflr8hdjjdla.com", user1, LocalDateTime.now());
    private static final Status status5 = new Status("Content: Hello World! \uD83D\uDE03, Mentions: @AllenAnderson, URLs: http://www.google.com", user2, LocalDateTime.now());
    private static final Status status6 = new Status("Content: Hello World!, Mentions: @AllenAnderson, URLs: https://www.google.com", user2, LocalDateTime.now());
    private static final Status status7 = new Status("Content: Hello World!, Mentions: @AllenAnderson, URLs: www.google.com", user2, LocalDateTime.now());
    private static final Status status8 = new Status("Content: Hello World!, Mentions: @AllenAnderson, URLs: http://www.4jflr8hdjjdla.com", user2, LocalDateTime.now());
    private static final Status status9 = new Status("Content: Hello World! \uD83D\uDE03, Mentions: @AmyAmes, URLs: http://www.google.com", user3, LocalDateTime.now());
    private static final Status status10 = new Status("Content: Hello World!, Mentions: @AmyAmes, URLs: https://www.google.com", user3, LocalDateTime.now());
    private static final Status status11 = new Status("Content: Hello World!, Mentions: @AmyAmes, URLs: www.google.com", user3, LocalDateTime.now());
    private static final Status status12 = new Status("Content: Hello World!, Mentions: @AmyAmes, URLs: http://www.4jflr8hdjjdla.com", user3, LocalDateTime.now());
    private static final Status status13 = new Status("Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com", testUser, LocalDateTime.now());
    private static final Status status14 = new Status("Content: Hello World!, Mentions: @BobBobson, URLs: https://www.google.com", testUser, LocalDateTime.now());
    private static final Status status15 = new Status("Content: Hello World!, Mentions: @BobBobson, URLs: www.google.com", testUser, LocalDateTime.now());
    private static final Status status16 = new Status("Content: Hello World!, Mentions: @BobBobson, URLs: http://www.4jflr8hdjjdla.com", testUser, LocalDateTime.now());

    private static final List<Status> sessionStatuses = new ArrayList<>();

    // TODO: finish refactoring each of these methods so each one uses the clientCommunicator to make requests.
    //  All logic related to generating dummy data should also be put into its respective DAO
    //  (i.e. getStatuses, getStatusesStartingIndex, etc.) all in one DAO
    /**
     * Performs a login and if successful, returns the logged in user and an auth token. The current
     * implementation is hard-coded to return a dummy user and doesn't actually make a network
     * request.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
//        User user = testUser;
//        user.setFollowees(getDummyUserAliases());
//        user.setFollowers(getDummyUserAliases());
//        return new LoginResponse(user, new AuthToken());
        LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        return new RegisterResponse(user, new AuthToken());
    }

    public LogoutResponse logout(LogoutRequest request) {
        // Invalidate Auth Token
        return new LogoutResponse("Successfully Logged Out");
    }

    public UserResponse getUsers(UserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        UserResponse response = clientCommunicator.doPost(urlPath, request, null, UserResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    List<String> getDummyUserAliases() {
        ArrayList<String> userAliases = new ArrayList<>();
        getDummyUsers().forEach(user -> { userAliases.add(user.getAlias()); });
        return userAliases;
    }

    public StatusesResponse getStatuses(StatusesRequest request) {
        // Used in place of assert statements because Android does not support them
        if (BuildConfig.DEBUG) {
            if (request.getLimit() < 0) {
                throw new AssertionError();
            }

            if (request.getUserAliases() == null) {
                throw new AssertionError();
            }
        }

        List<Status> allStatuses = getDummyStatuses(request.getUserAliases()); // sorted by date published
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int statusesIndex = getStatusesStartingIndex(request.getLastStatus(), allStatuses);

            for (int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                responseStatuses.add(allStatuses.get(statusesIndex));
            }

            hasMorePages = statusesIndex < allStatuses.size();
        }

        return new StatusesResponse(responseStatuses, hasMorePages);
    }

    private int getStatusesStartingIndex(Status lastStatus, List<Status> allStatuses) {
        int statusesIndex = 0;

        if (lastStatus != null) {
            for (int i = 0; i < allStatuses.size(); i++) {
                if (lastStatus.equals(allStatuses.get(i))) {
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    List<Status> getDummyStatuses(List<String> userAliases) {
        List<Status> statuses = new ArrayList<>(Arrays.asList(status1, status2, status3, status4, status5, status6,
                status7, status8, status9, status10, status11, status12, status13, status14, status15, status16));
        statuses.addAll(sessionStatuses);
        statuses.removeIf(status -> !userAliases.contains(status.getAuthor().getAlias())); // inefficient, but this is dummy code. The backend will replace this.
        statuses.sort((s1, s2) -> s1.getTimePublished().compareTo(s2.getTimePublished()));
        return statuses;
    }

    public PostResponse savePost(PostRequest postRequest) {
        Status status = new Status(postRequest.getContent(), postRequest.getAuthor(), postRequest.getTimePublished());
        sessionStatuses.add(status);
        return new PostResponse(true, status);
    }

    public FollowUserResponse followUser(FollowUserRequest followUserRequest) {

        if (followUserRequest.getRootUser().getAlias().equals(followUserRequest.getCurrentUser().getAlias())) {
            return new FollowUserResponse(false, "You cannot follow yourself!");
        }

        // make call to backend
        return new FollowUserResponse(true,"Successfully followed user");
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest unfollowUserRequest) {
        // make call to backend
        return new UnfollowUserResponse(true, "Successfully unfollowed user");
    }
}
