package com.api.assemblyvoter.enums;

public enum CanVote {
    ABLE_TO_VOTE("ABLE_TO_VOTE"),
    UNABLE_TO_VOTE("UNABLE_TO_VOTE");

    private String status;

    CanVote(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
