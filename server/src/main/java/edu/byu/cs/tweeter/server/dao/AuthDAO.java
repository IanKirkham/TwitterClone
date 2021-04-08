package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class AuthDAO {
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable("authtoken");

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public LogoutResponse logout(LogoutRequest request) {
        invalidateToken(table, request.getAuthToken());
        return new LogoutResponse(true, "Logout successful");
    }

    public static void invalidateToken(Table table, AuthToken token) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("token", token.getToken());
        table.deleteItem(deleteItemSpec);
    }

    public boolean isValidTokenForUser(User user, AuthToken token) {
        Item item = table.getItem("token", token.getToken());
        String expectedAlias = item.getString("alias");
        return expectedAlias.equals(user.getAlias());
    }

    public AuthToken generateTokenForUser(User user) {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        AuthToken newAuthToken = new AuthToken(user.getAlias(), token);

        saveAuthToken(table, newAuthToken);
        return newAuthToken;
    }

    public static void saveAuthToken(Table table, AuthToken authToken) {
        Item item = new Item()
                .withPrimaryKey("token", authToken.getToken())
                .withString("alias", authToken.getAlias());

        table.putItem(item);
    }
}
