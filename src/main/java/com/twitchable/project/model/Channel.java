package com.twitchable.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riste on 4/7/2016.
 */

@Document(collection = "channel")
public class Channel {
	
	@Id
	private String id;
    private String mature;

    private String status;

    private String broadcasterLanguage;

    private String displayName;

    private String game;

    private String name;

    private String createdAt;

    private String updatedAt;

    private String logo;

    private String url;

    private String streamUrl;

    private String liveStream;

    private String liveChat;

    private long viewsNumber;

    private long followersNumber;

    private double risingStar;

    private double rating; // ova ke bide ocenkata za kanalot presmetana po formula

    private List<Rating> raters; // ovde ke se chuva koj glasal so koja ocenka i na koj datum.

    public Channel() {
    	raters = new ArrayList<Rating>();
    }

    public double getRisingStar() {
        return risingStar;
    }

    public void setRisingStar(double risingStar) {
        this.risingStar = risingStar;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getMature() {
        return mature;
    }

    public void setMature(String mature) {
        this.mature = mature;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLiveStream() {
        return liveStream;
    }

    public void setLiveStream(String liveStream) {
        this.liveStream = liveStream;
    }

    public String getLiveChat() {
        return liveChat;
    }

    public void setLiveChat(String liveChat) {
        this.liveChat = liveChat;
    }

    public long getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(long viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public long getFollowersNumber() {
        return followersNumber;
    }

    public void setFollowersNumber(long followersNumber) {
        this.followersNumber = followersNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Rating> getRaters() {
        return raters;
    }

    public void setRaters(List<Rating> raters) {
        calculateRating();
        this.raters = raters;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "mature='" + mature + '\'' +
                ", status='" + status + '\'' +
                ", broadcasterLanguage='" + broadcasterLanguage + '\'' +
                ", displayName='" + displayName + '\'' +
                ", game='" + game + '\'' +
                ", name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", logo='" + logo + '\'' +
                ", url='" + url + '\'' +
                ", streamUrl='" + streamUrl + '\'' +
                ", liveStream='" + liveStream + '\'' +
                ", liveChat='" + liveChat + '\'' +
                ", viewsNumber=" + viewsNumber +
                ", followersNumber=" + followersNumber +
                ", rating=" + rating +
                ", raters=" + raters +
                '}';
    }

    public String getBroadcasterLanguage() {
        return broadcasterLanguage;
    }

    public void setBroadcasterLanguage(String broadcasterLanguage) {
        this.broadcasterLanguage = broadcasterLanguage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void calculateRating(){
        double ratingR=0.0;
        for(int i=0;i<raters.size();i++){
            ratingR+=raters.get(i).getRating();
        }
        rating=ratingR/raters.size();
    }
}
