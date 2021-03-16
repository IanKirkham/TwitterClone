package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

/**
 * A DAO for accessing 'statuses' data from the database.
 */
public class StatusesDAO {

    // This is the hard coded statuses data returned by the 'getStatuses()' method
    private static final User testUser = new User("Test", "User",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

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

    public StatusesResponse getStatuses(StatusesRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUserAliases() != null;

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
        statuses.removeIf(status -> !userAliases.contains(status.getAuthor().getAlias())); // inefficient, but this is dummy code. The database will replace this.
        statuses.sort((s1, s2) -> s1.getTimePublished().compareTo(s2.getTimePublished()));
        return statuses;
    }

    public PostResponse savePost(PostRequest request) {
        Status status = new Status(request.getContent(), request.getAuthor(), request.getTimePublished());
        return new PostResponse(status);
    }
}
