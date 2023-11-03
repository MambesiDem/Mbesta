package com.example.project;

public class CommentsAndRatings {
    String comment;
    String rating;
    public CommentsAndRatings(String comment, String rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public String getRating() {
        return rating;
    }
}
