package com.example.protocolapp.model;

public class Score {
   private String  comment;
    private Long protocolId;
   private int rating;

    public Score(Long protocolId, String comment, int rating) {
        this.protocolId = protocolId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
