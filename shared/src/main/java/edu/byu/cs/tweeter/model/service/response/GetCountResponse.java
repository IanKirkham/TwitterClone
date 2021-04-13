package edu.byu.cs.tweeter.model.service.response;

public class GetCountResponse {
    private int count;

    public GetCountResponse(int count) {
        this.count = count;
    }

    public GetCountResponse() {}

    public int getCount() {
        return count;
    }

    public void setAlias(int count) {
        this.count = count;
    }
}