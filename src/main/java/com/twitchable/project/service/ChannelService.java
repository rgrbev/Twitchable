package com.twitchable.project.service;

import com.twitchable.project.model.Channel;
import com.twitchable.project.model.User;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * Created by riste on 4/7/2016.
 */
public interface ChannelService {
    public Channel addChannel(Channel c);
    public List<Channel> getAllChannels();
    public List<Channel> findChannelForUser(User u);
    public Channel findChannelByName(String display_name);
    public Channel findOne(String id);
    public void deleteAll();

    public List<String> getAllGames();
    public List<String> getAllLanguages();
    public List<Channel> findTopChannels();

    //DODADENI
    Channel rateChannel(String channelName, String userName, double rating);
    List<Channel> searchChannels(String mature,String broadcasterLanguage,String game,String name);
    public List<Channel> risingStars();

    public Channel createChannel(org.json.JSONObject channelObject) throws JSONException;

    public StringBuilder getData(String link) throws InterruptedException, IOException;

    public List risingStarsByRating();

    public List<Channel> findMostSubscribed();
    public List<Channel> getRecomendedChannelsForUser(HttpSession session) throws IOException, InterruptedException, JSONException;

}
