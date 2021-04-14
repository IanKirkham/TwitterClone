package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class StoryDAOTest {
    private static StoryDAO dao;
    private static AuthToken authToken;
    private static String userAlias1 = "@TestUser";
    private static String userAlias2 = "@nonexistent";

    @BeforeAll
    static void setup() {
        dao = new StoryDAO();
        authToken = new AuthToken("@TestUser", "p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia");
    }

    @Test
    void testGetStory_noStatusesForUser() {
        StoryRequest request = new StoryRequest(userAlias2, 10, null, authToken);
        StatusesResponse response = dao.getStory(request);

        Assertions.assertEquals(0, response.getStatuses().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_limitGreaterThanStatuses() {
        StoryRequest request = new StoryRequest(userAlias1, 25, null, authToken);
        StatusesResponse response = dao.getStory(request);

        Assertions.assertTrue(response.getStatuses().size() > 0);
        Assertions.assertTrue(response.getStatuses().size() < 25);
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_limitLessThanStatuses() {
        StoryRequest request = new StoryRequest(userAlias1, 2, null, authToken);
        StatusesResponse response = dao.getStory(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new StoryRequest(userAlias1, 2, response.getLastKey(), authToken);
        response = dao.getStory(request);

        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getHasMorePages());
    }

    @Test
    void testSavePost() {
        String userAlias4 = UUID.randomUUID().toString().substring(0, 8);
        StoryRequest request = new StoryRequest(userAlias4, 10, null, authToken);
        StatusesResponse response = dao.getStory(request);

        Assertions.assertEquals(0, response.getStatuses().size());
        Assertions.assertFalse(response.getHasMorePages());

        PostRequest postRequest = new PostRequest(userAlias4, "Delete me", LocalDateTime.now(), authToken);
        PostResponse postResponse = dao.savePost(postRequest);

        Assertions.assertNotNull(postResponse.getStatus());

        response = dao.getStory(request);

        Assertions.assertEquals(1, response.getStatuses().size());
    }
}
