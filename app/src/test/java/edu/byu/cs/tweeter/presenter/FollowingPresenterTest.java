package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

public class FollowingPresenterTest {

    private UserRequest request;
    private UserResponse response;
    private UserService mockUserService;
    private UserPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        currentUser.setFollowees(new ArrayList<>(Arrays.asList(resultUser1.getAlias(), resultUser2.getAlias(), resultUser3.getAlias())));

        request = new UserRequest(currentUser.getFollowees(), 3, null);
        response = new UserResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);

        // Create a mock UserService
        mockUserService = Mockito.mock(UserService.class);
        Mockito.when(mockUserService.getUsers(request)).thenReturn(response);

        // Wrap a UserPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new UserPresenter(new UserPresenter.View() {
            @Override
            public void presentNewUserView(User user) {}
        }));
        Mockito.when(presenter.getUserService()).thenReturn(mockUserService);
    }

    @Test
    public void testGetFollowing_returnsServiceResult() throws IOException {
        Mockito.when(mockUserService.getUsers(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getUsers(request));
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockUserService.getUsers(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getUsers(request);
        });
    }
}
