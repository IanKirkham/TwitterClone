package edu.byu.cs.tweeter.model.net;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowEventResponse;
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
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private static final User testUser = new User("Test", "User",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    private static final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private static final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private static final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private static final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private static final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private static final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private static final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private static final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private static final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private static final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private static final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private static final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private static final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private static final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private static final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private static final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private static final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private static final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private static final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private static final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);

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

    /**
     * Performs a login and if successful, returns the logged in user and an auth token. The current
     * implementation is hard-coded to return a dummy user and doesn't actually make a network
     * request.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) {
        User user = testUser;
        user.setFollowees(getDummyUserAliases());
        user.setFollowers(getDummyUserAliases());
        return new LoginResponse(user, new AuthToken());
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        return new RegisterResponse(user, new AuthToken());
    }

    public LogoutResponse logout(LogoutRequest request) {
        // Invalidate Auth Token
        return new LogoutResponse("Successfully Logged Out");
    }

    public UserResponse getUsers(UserRequest request) {
        // Used in place of assert statements because Android does not support them
        if (BuildConfig.DEBUG) {
            if (request.getLimit() < 0) {
                throw new AssertionError();
            }

            if (request.getUserAliases() == null) {
                throw new AssertionError();
            }
        }

        List<User> allUsers = getRequestedUsers(request.getUserAliases());
        List<User> responseUsers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int userIndex = getUsersStartingIndex(request.getLastUserAlias(), allUsers);

            for (int limitCounter = 0; userIndex < allUsers.size() && limitCounter < request.getLimit(); userIndex++, limitCounter++) {
                responseUsers.add(allUsers.get(userIndex));
            }
            hasMorePages = userIndex < allUsers.size();
        }

        return new UserResponse(responseUsers, hasMorePages);
    }

    /**
     * Determines the index for the first user in the specified 'allUsers' list that should
     * be returned in the current request. This will be the index of the next user after the
     * specified 'lastUser'.
     *
     * @param lastUserAlias the alias of the last user that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allUsers the generated list of users from which we are returning paged results.
     * @return the index of the first user to be returned.
     */
    private int getUsersStartingIndex(String lastUserAlias, List<User> allUsers) {
        int usersIndex = 0;

        if (lastUserAlias != null) {
            for (int i = 0; i < allUsers.size(); i++) {
                if (lastUserAlias.equals(allUsers.get(i).getAlias())) {
                    usersIndex = i + 1;
                    break;
                }
            }
        }

        return usersIndex;
    }

    /**
     * Returns the list of dummy user data. This is written as a separate method to allow
     * mocking of the users.
     *
     * @return the users.
     */
    List<User> getDummyUsers() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

    List<User> getRequestedUsers(List<String> userAliases) {
        ArrayList<User> users = new ArrayList<>(getDummyUsers());
        users.removeIf(user -> !userAliases.contains(user.getAlias()));
        return users;
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
        // make call to backend
        return new FollowUserResponse(true,"Successfully followed user");
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest unfollowUserRequest) {
        // make call to backend
        return new UnfollowUserResponse(true, "Successfully unfollowed user");
    }
}
