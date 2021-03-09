package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * A DAO for accessing 'user' data from the database.
 */
public class UserDAO {
    // This is the hard coded user data returned by the 'getUsers()' method
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

    public UserResponse getUsers(UserRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUserAliases() != null;

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
}
