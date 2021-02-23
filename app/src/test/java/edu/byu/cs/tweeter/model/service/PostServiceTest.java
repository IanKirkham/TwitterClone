package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.PostService;

public class PostServiceTest {

    private PostRequest validRequest;
    private PostRequest invalidRequest;

    private PostResponse successResponse;
    private PostResponse failureResponse;

    private PostService postServiceSpy;

    private static final LocalDateTime time1 = LocalDateTime.now();

    /**
     * Create a PostService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";

        Status status1 = new Status(content1, currentUser, time1);

        // Setup request objects to use in the tests
        validRequest = new PostRequest(currentUser, content1, time1);

        invalidRequest = new PostRequest(null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostResponse(true, status1);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.savePost(validRequest)).thenReturn(successResponse);

        failureResponse = new PostResponse(false, null);
        Mockito.when(mockServerFacade.savePost(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        postServiceSpy = Mockito.spy(new PostService());
        Mockito.when(postServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link PostService#savePost(PostRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws Exception if an database error occurs.
     */
    @Test
    public void testSavePost_validRequest_correctResponse() throws Exception {
        PostResponse response = postServiceSpy.savePost(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the {@link PostService#savePost(PostRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testSavePost_invalidRequest_returnsNoStatus() throws Exception {
        PostResponse response = postServiceSpy.savePost(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
