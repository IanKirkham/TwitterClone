package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.StatusesDAO;

public class StatusesServiceImplTest {

    private StatusesRequest request;
    private StatusesResponse expectedResponse;
    private StatusesDAO mockStatusesDAO;
    private StatusesServiceImpl statusesServiceImplSpy;

    private static final LocalDateTime time1 = LocalDateTime.now();

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user1 = new User("Allen", "Anderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";
        String content2 = "Content: Hello World!, Mentions: @BobBobson, URLs: https://www.google.com";

        Status status1 = new Status(content1, currentUser, time1);
        Status status2 = new Status(content2, currentUser, time1.plus(Duration.ofSeconds(1)));

        currentUser.setFollowees(new ArrayList<String>(Arrays.asList(user1.getAlias())));

        // Setup request objects to use in the tests
        request = new StatusesRequest(currentUser.getFollowees(), 2, null);

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new StatusesResponse(Arrays.asList(status1, status2), false);
        mockStatusesDAO = Mockito.mock(StatusesDAO.class);
        Mockito.when(mockStatusesDAO.getStatuses(request)).thenReturn(expectedResponse);

        statusesServiceImplSpy = Mockito.spy(StatusesServiceImpl.class);
        Mockito.when(statusesServiceImplSpy.getStatusesDAO()).thenReturn(mockStatusesDAO);
    }

    /**
     * Verify that the {@link StatusesServiceImpl#getStatuses(StatusesRequest)}
     * method returns the same result as the {@link StatusesDAO} class.
     */
    @Test
    public void testGetStatuses_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceImplSpy.getStatuses(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}

