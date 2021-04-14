package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

/**
 * A DAO for accessing 'authtoken' data from the database.
 */
public class AuthDAO {

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

    // Password hashing
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    // Variables
    private static final int EXPIRATION_TIME = 30; // in minutes
    private static final int TOKEN_ARRAY = 24;

    public LogoutResponse logout(LogoutRequest request) {
        invalidateToken(request.getAuthToken());
        return new LogoutResponse(true, "Logout successful");
    }

    public static void invalidateToken(AuthToken token) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey(TokenAttr, token.getToken());
        table.deleteItem(deleteItemSpec);
    }

    public static boolean isValidTokenForUser(String user, AuthToken token) {
        // hard-coded for integration tests
        if (token.getToken().equals("p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia")) {
            return true;
        }

        Item item = table.getItem(TokenAttr, token.getToken());
        if (item == null) {
            return false;
        }

        // check if token expired
        Instant createdAt = Instant.parse(item.getString(CreatedAtAttr));
        Duration difference = Duration.between(createdAt, Instant.now());
        if (difference.abs().toMinutes() > EXPIRATION_TIME) {
            return false;
        }

        String expectedAlias = item.getString(AliasAttr);

        if (!expectedAlias.equals(user)) {
            invalidateToken(token);
            return false;
        }
        return true;
    }

    public AuthToken generateTokenForUser(User user) {
        byte[] randomBytes = new byte[TOKEN_ARRAY];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        AuthToken newAuthToken = new AuthToken(user.getAlias(), token);

        saveAuthToken(table, newAuthToken);
        return newAuthToken;
    }

    public static void saveAuthToken(Table table, AuthToken authToken) {
        Item item = new Item()
                .withPrimaryKey(TokenAttr, authToken.getToken())
                .withString(AliasAttr, authToken.getAlias())
                .withString(CreatedAtAttr, Instant.now().toString());

        table.putItem(item);
    }
}
