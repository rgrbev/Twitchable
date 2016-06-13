package com.twitchable.project.model;

import java.util.Date;

/**
 * Created by riste on 4/9/2016.
 */
public class Rating {
    private User user;
    private double rating;
    private Date date;

    public Rating() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
