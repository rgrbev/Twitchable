package com.twitchable.project.rest;

import com.twitchable.project.model.Channel;
import com.twitchable.project.model.User;
import com.twitchable.project.service.ChannelService;
import com.twitchable.project.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.json.simple.parser.JSONParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

/**
 * Created by Atanas on 17.04.2016.
 */
@RestController
@RequestMapping(value = "/channel")
@CrossOrigin
public class ChannelRest {

    @Autowired
    private ChannelService service;
    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Channel> findAll(){
        return service.getAllChannels().subList(0, 12);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Channel save(@RequestBody Channel channel){
        return service.addChannel(channel);
    }

    @RequestMapping(value = "/displayName/{name}", method = RequestMethod.GET)
    public Channel findByName(@PathVariable String name){
        return service.findChannelByName(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Channel findOne(@PathVariable String id){
        return service.findOne(id);
    }



    //DODADENI

    //SEARCH
    //ova e samo za testiranje dali raboti
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Channel> searchByCriteria(){
        String mature=null;
        String broadcasterLanguage=null;
        String game="Aftermath";
        String name=null;
        List<Channel> channels=service.searchChannels(mature,broadcasterLanguage,game,name);
        return channels;
    }


    //SEARCH
    //ova ke raboti so angular koga ke my se pratat json podatoci
    @RequestMapping(method = RequestMethod.POST,value="/searchJSON",produces = {"application/json"})
    public List<Channel> getSearchFilteredDrugs(@RequestBody String channelProp)
    {
        //ima dependency vo pom.xml ne zaboravajte :)
        JSONParser parser = new JSONParser();
        JSONObject obj= null;
        List<Channel> channels=new ArrayList<Channel>();
        try {
            obj = (JSONObject) parser.parse(channelProp);
            String rating=null;
            String broadcasterLanguage=null;
            String game=null;
            String name="";
            
            if(obj.get("rating") != null)
            	rating=obj.get("rating").toString();
            
            if(obj.get("broadcasterLanguage") != null)
            	broadcasterLanguage=obj.get("broadcasterLanguage").toString();
            
            if(obj.get("game") != null)
            	game=obj.get("game").toString();
            
            if(obj.get("name") != null)
            	name=obj.get("name").toString();
            
            channels=service.searchChannels(rating,broadcasterLanguage,game,name);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return channels;
    }



    //RATE
    //ova e samo za testiranje dali raboti
    @RequestMapping(value = "/rate", method = RequestMethod.GET)
    public Channel rate(String channelName,String userName,String rating){
        Double ratingD=1.2;//Double.parseDouble(rating);
        Channel ratedChannel=service.rateChannel(channelName,userName,ratingD);
        return ratedChannel;
    }
    @RequestMapping(method = RequestMethod.POST,value="/rateJSON",produces = {"application/json"})
    public Channel rateJSON(@RequestBody String channelProp)
    {

        JSONParser parser = new JSONParser();
        JSONObject obj= null;

        Channel ratedChannel=new Channel();

        try {
            obj = (JSONObject) parser.parse(channelProp);
            String channelName=null;
            String userName=null;
            String rating=null;
            Double ratingD=0.0;

            channelName=obj.get("channelName").toString();
            userName=obj.get("userName").toString();
            rating=obj.get("ratingD").toString();
            ratingD=Double.parseDouble(rating);

            ratedChannel=service.rateChannel(channelName,userName,ratingD);
        } catch (ParseException e) {
            e.printStackTrace();
        } 
        System.out.println("rating");
        System.out.println(ratedChannel.getRating());
        return ratedChannel;
    }


    @RequestMapping(method = RequestMethod.GET,value="/findDistinctGames",produces = {"application/json"})
    public List<String> findDistinctGames(){
        return service.getAllGames();
    }

    @RequestMapping(method = RequestMethod.GET,value="/findDistinctLanguages",produces = {"application/json"})
    public List<String> findDistinctLanguages(){
        return service.getAllLanguages();
    }


    @RequestMapping(method = RequestMethod.GET,value="/findTopChannels",produces = {"application/json"})
    public List<Channel> findTopChannels(){
        return service.findTopChannels();
    }

    @RequestMapping(method = RequestMethod.GET,value="/findMostViewedChannels",produces = {"application/json"})
    public List<Channel> findMostViewedChannels(){
        return service.findMostSubscribed();
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public void addUser(String userName){
        User u=new User();
        u.setName(userName);
        userService.createUser(u);
    }


    @RequestMapping(value = "/random/online", method = RequestMethod.GET)
    public List<Channel> getRandomOnlineChannels() throws IOException, InterruptedException, JSONException {
        List<Channel> randomOnlineChannels = new ArrayList<>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        Random rnd = new Random();
        for(int j=0;j<3;j++) {
            int randomNum = rnd.nextInt(26);
            char c = alphabet[randomNum];
            String link = "https://api.twitch.tv/kraken/search/streams?limit=12&offset=0&q=" + c;
            StringBuilder builder = channelService.getData(link);
            org.json.JSONObject data = new org.json.JSONObject(builder.toString());
            JSONArray streams = data.getJSONArray("streams");

            for (int i = 0; i < 3; i++) {
                org.json.JSONObject row = streams.getJSONObject(i);
                org.json.JSONObject channelObject = row.getJSONObject("channel");
                Channel channel = channelService.createChannel(channelObject);
                randomOnlineChannels.add(channel);
                //tuka treba da se dodavaat kanalive vo bazata za da moze da se pravi rating za niv.

                Channel newChannel = channelService.findChannelByName(channel.getName());
                if (newChannel==null){
                    channelService.addChannel(channel);
                }
                else {
                    channelService.addChannel(newChannel);
                }

            }
        }
        return randomOnlineChannels;
    }

    @RequestMapping(value = "/risingStars", method = RequestMethod.GET)
    public List rate(){
        List ratedChannel=service.risingStarsByRating();
        return ratedChannel;
    }

    @Async
    @RequestMapping(value = "/recomended", method = RequestMethod.GET)
    public List<Channel> getRecomendedChannelsForUser(HttpSession session) throws IOException, InterruptedException, JSONException {
        return service.getRecomendedChannelsForUser(session);
    }

}
