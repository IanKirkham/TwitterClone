package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostPresenterTest {

    private PostRequest postRequest;
    private PostResponse postResponse;
    private PostServiceProxy mockPostService;
    private PostPresenter presenter;

    private boolean viewWasCalled = false;

    private static final LocalDateTime time1 = LocalDateTime.now();

    @BeforeEach
    public void setup() throws Exception {
        User currentUser = new User("FirstName", "LastName", null);

        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";

        Status status1 = new Status(content1, currentUser, time1);

        postRequest = new PostRequest(currentUser, content1, time1);

        postResponse = new PostResponse(status1);

        // Create a mock UserService
        mockPostService = Mockito.mock(PostServiceProxy.class);
        Mockito.when(mockPostService.savePost(postRequest)).thenReturn(postResponse);

        // Wrap a PostPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new PostPresenter(new PostPresenter.View() {
            public void postSaved(PostResponse postResponse) {
                viewWasCalled = true;
            }
            public void handleException(Exception exception) {}
        }));
        Mockito.when(presenter.getPostService()).thenReturn(mockPostService);

        viewWasCalled = false;
    }

    @Test
    public void testSavePost_returnsServiceResult() throws Exception {
        Mockito.when(mockPostService.savePost(postRequest)).thenReturn(postResponse);

        Assertions.assertEquals(postResponse, presenter.savePost(postRequest));
    }

    @Test
    public void testViewMethod_wasCalled() throws Exception {
        presenter.postSaved(postResponse);
        Assertions.assertTrue(viewWasCalled);
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws Exception {
        Mockito.when(mockPostService.savePost(postRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.savePost(postRequest);
        });
    }
}
