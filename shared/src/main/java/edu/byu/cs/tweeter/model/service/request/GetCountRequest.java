package edu.byu.cs.tweeter.model.service.request;

public class GetCountRequest {
    private String alias;

    public GetCountRequest(String alias) {
        this.alias = alias;
    }

    public GetCountRequest() {}

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
