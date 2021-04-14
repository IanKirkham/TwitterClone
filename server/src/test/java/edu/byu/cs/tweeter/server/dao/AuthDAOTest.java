package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class AuthDAOTest {

    private AuthDAO authDAO;
    private AuthToken authToken;
    private String userAlias;

    // Table
    private static final String TableName = "authtoken";

    // Attributes
    private static final String TokenAttr = "token";
    private static final String CreatedAtAttr = "createdAt";
    private static final String AliasAttr = "alias";

    // DynamoDB client
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable(TableName);

    @BeforeEach
    void setup() {
        authDAO = new AuthDAO();
        authToken = new AuthToken("@TestUser", "123456789");
        userAlias = "@TestUser";
    }

    @Test
    void testLogout_successResponse() {
        Item item = new Item()
                .withPrimaryKey(TokenAttr, authToken.getToken())
                .withString(AliasAttr, authToken.getAlias())
                .withString(CreatedAtAttr, Instant.now().toString());

        table.putItem(item);

        LogoutRequest logoutRequest = new LogoutRequest("@TestUser", authToken);
        LogoutResponse logoutResponse = authDAO.logout(logoutRequest);

        Assertions.assertTrue(logoutResponse.isSuccess());

        Item retrievedItem = table.getItem(TokenAttr, authToken.getToken());
        Assertions.assertNull(retrievedItem);
    }

    @Test
    void testInvalidateToken_removedFromDatabase() {
        Item item = new Item()
                .withPrimaryKey(TokenAttr, authToken.getToken())
                .withString(AliasAttr, authToken.getAlias())
                .withString(CreatedAtAttr, Instant.now().toString());

        table.putItem(item);

        Item retrievedItem = table.getItem(TokenAttr, authToken.getToken());
        Assertions.assertNotNull(retrievedItem);

        AuthDAO.invalidateToken(authToken);

        retrievedItem = table.getItem(TokenAttr, authToken.getToken());
        Assertions.assertNull(retrievedItem);

    }

    @Test
    void testIsValidTokenForUser_correctResponse() {
        Assertions.assertFalse(AuthDAO.isValidTokenForUser(userAlias, authToken));

        Item item = new Item()
                .withPrimaryKey(TokenAttr, authToken.getToken())
                .withString(AliasAttr, authToken.getAlias())
                .withString(CreatedAtAttr, Instant.now().toString());

        table.putItem(item);

        Assertions.assertTrue(AuthDAO.isValidTokenForUser(userAlias, authToken));
    }

    @Test
    void testGenerateToken_returnsUniqueToken() {
        User user = new User("Test", "User", userAlias, "");
        AuthToken newToken = authDAO.generateTokenForUser(user);
        AuthToken anotherToken = authDAO.generateTokenForUser(user);

        Assertions.assertNotEquals(newToken, anotherToken);

        AuthDAO.invalidateToken(newToken);
        AuthDAO.invalidateToken(anotherToken);
    }
}
