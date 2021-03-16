package edu.byu.cs.tweeter.server.dao;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class AuthDAO {
    private static final UUID AUTH_TOKEN_UUID = UUID.fromString("8af4d1be-f1fa-40a6-b56d-f741a31f8421");

    public LogoutResponse logout(LogoutRequest request) {
        // TODO: invalidate token
        return new LogoutResponse("Logout successful");
    }

    public boolean isValidTokenForUser(User user, AuthToken token) {
        return token.getToken().equals(AUTH_TOKEN_UUID);
    }

    public AuthToken generateTokenForUser(User user) {
        return new AuthToken(AUTH_TOKEN_UUID);
    }
}
