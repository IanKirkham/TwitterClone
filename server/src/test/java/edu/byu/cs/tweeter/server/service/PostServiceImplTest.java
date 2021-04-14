package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

import static org.mockito.Mockito.doNothing;

public class PostServiceImplTest {

    private PostRequest request;
    private PostResponse expectedResponse;
    private StoryDAO mockStoryDAO;
    private PostServiceImpl postServiceImplSpy;

    private static final LocalDateTime time1 = LocalDateTime.now();

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";
        Status status1 = new Status(content1, currentUser, time1);

        // Setup request objects to use in the tests
        request = new PostRequest(currentUser.getAlias(), content1, time1, new AuthToken());

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new PostResponse(status1);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.savePost(request)).thenReturn(expectedResponse);

        postServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        Mockito.when(postServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);
        doNothing().when(postServiceImplSpy).sendToSQS(request);
    }

    /**
     * Verify that the {@link PostServiceImpl#savePost(PostRequest)}
     * method returns the same result as the {@link StoryDAO} class.
     */
    @Test
    public void testSavePost_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = postServiceImplSpy.savePost(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}

