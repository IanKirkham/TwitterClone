package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user in the system.
 */
public class User implements Comparable<User>, Serializable {

    private final String firstName;
    private final String lastName;
    private final String alias;
    private final String imageUrl;
    private byte [] imageBytes;

    private List<String> followers; // list of userAliases?
    private List<String> followees; // list of userAliases?

    public User(String firstName, String lastName, String imageURL) {
        this(firstName, lastName, String.format("@%s%s", firstName, lastName), imageURL);
    }

    public User(String firstName, String lastName, String alias, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageURL;
        this.followers = new ArrayList<>();
        this.followees = new ArrayList<>();
    }

    public User(String firstName, String lastName, String alias, String imageURL, List<String> followers, List<String> followees) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageURL;
        this.followers = followers;
        this.followees = followees;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getAlias() {
        return alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public byte [] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getFollowees() {
        return followees;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public void setFollowees(List<String> followees) {
        this.followees = followees;
    }

    public void addFollower(String userAlias) {
        followers.add(userAlias);
    }

    public void addFollowee(String userAlias) {
        followers.add(userAlias);
    }

    public void removeFollower(String userAlias) {
        followers.remove(userAlias);
    }

    public void removeFollowees(String userAlias) {
        followees.remove(userAlias);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return alias.equals(user.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int compareTo(User user) {
        return this.getAlias().compareTo(user.getAlias());
    }
}
