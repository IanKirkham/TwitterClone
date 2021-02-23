package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostPresenterTest {

    private PostRequest postRequest;
    private PostResponse postResponse;
    private PostService mockPostService;
    private PostPresenter presenter;

    private static final LocalDateTime time1 = LocalDateTime.now();

    @BeforeEach
    public void setup() throws Exception {
        User currentUser = new User("FirstName", "LastName", null);

        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";

        Status status1 = new Status(content1, currentUser, time1);

        postRequest = new PostRequest(currentUser, content1, time1);

        postResponse = new PostResponse(true, status1);

        // Create a mock UserService
        mockPostService = Mockito.mock(PostService.class);
        Mockito.when(mockPostService.savePost(postRequest)).thenReturn(postResponse);

        // Wrap a PostPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new PostPresenter(new PostPresenter.View() {
            public void postSaved(PostResponse postResponse) {}
            public void handleException(Exception exception) {}
        }));
        Mockito.when(presenter.getPostService()).thenReturn(mockPostService);
    }

    @Test
    public void testSavePost_returnsServiceResult() throws Exception {
        Mockito.when(mockPostService.savePost(postRequest)).thenReturn(postResponse);

        Assertions.assertEquals(postResponse, presenter.savePost(postRequest));
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws Exception {
        Mockito.when(mockPostService.savePost(postRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.savePost(postRequest);
        });
    }
}
