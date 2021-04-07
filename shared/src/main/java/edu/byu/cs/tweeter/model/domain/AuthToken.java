package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    private String token;
    private String alias;

    public AuthToken(String alias, String uuid) {
        this.alias = alias;
        this.token = uuid;
    }

    public AuthToken() {}

    public String getToken() {
        return token;
    }

    public String getAlias() {
        return alias;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return token.equals(authToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
