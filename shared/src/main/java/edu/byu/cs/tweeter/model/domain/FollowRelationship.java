package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;

public class FollowRelationship implements Serializable {
    private String follower_handle;
    private String followee_handle;

    private String follower_name;
    private String followee_name;

    public FollowRelationship(String follower_handle, String follower_name, String followee_handle, String followee_name) {
        this.follower_name = follower_name;
        this.followee_name = followee_name;
        this.follower_handle = follower_handle;
        this.followee_handle = followee_handle;
    }

    public FollowRelationship() {}

    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    public String getFollowee_name() {
        return followee_name;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name = followee_name;
    }

    @Override
    public String toString() {
        return "FollowRelatationship{" +
                "follower_handle='" + follower_handle + '\'' +
                ", followee_handle='" + followee_handle + '\'' +
                ", follower_name='" + follower_name + '\'' +
                ", followee_name='" + followee_name + '\'' +
                '}';
    }
}

