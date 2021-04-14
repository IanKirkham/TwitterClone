package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class FeedDAOTest {
    private static FeedDAO dao;
    private static AuthToken authToken;
    private static String userAlias1 = "@AllenAnderson";
    private static String userAlias2 = "@nonexistent";

    @BeforeAll
    static void setup() {
        dao = new FeedDAO();
        authToken = new AuthToken("@TestUser", "p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia");
    }

    @Test
    void testGetFeed_noStatusesForUser() {
        FeedRequest request = new FeedRequest(userAlias2, 10, null, authToken);
        StatusesResponse response = dao.getFeed(request);

        Assertions.assertEquals(0, response.getStatuses().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_limitGreaterThanStatuses() {
        FeedRequest request = new FeedRequest(userAlias1, 25, null, authToken);
        StatusesResponse response = dao.getFeed(request);

        Assertions.assertTrue(response.getStatuses().size() > 0);
        Assertions.assertTrue(response.getStatuses().size() < 25);
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_limitLessThanStatuses() {
        FeedRequest request = new FeedRequest(userAlias1, 2, null, authToken);
        StatusesResponse response = dao.getFeed(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FeedRequest(userAlias1, 2, response.getLastKey(), authToken);
        response = dao.getFeed(request);

        Assertions.assertEquals(2, response.getStatuses().size());
        Assertions.assertFalse(response.getHasMorePages());
    }
}
