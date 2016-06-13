package com.twitchable.project.web;

import com.twitchable.project.model.Channel;
import com.twitchable.project.service.ChannelService;
import com.twitchable.project.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;


/**
 * Created by riste on 4/7/2016.
 */

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    ChannelService channelService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model, HttpSession session) throws IOException, InterruptedException, JSONException {
    	
        return "index";
    }



    private void allLinksChannels() throws JSONException, InterruptedException, IOException{
        boolean firstTime=true;
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(char c : alphabet) {
            String nextLink = "https://api.twitch.tv/kraken/search/channels?limit=100&offset=0&q="+c;
            //List all Links
            //SEUSHTE NEMA STAVENO KRAEN USLOV ZASO NE DOJDOV DO TAKOV
            boolean flag = true;
            int i = 0;
            while (flag) {
                System.out.println("LINK:" + nextLink);
                //getAllLinks gets the page
                StringBuilder builder = channelService.getData(nextLink);
                //FULLPAGE JSON

                JSONObject fullPage = new JSONObject(builder.toString());
                parseJSONChannels(builder, fullPage);
                //flag = false;
                //GET THE NEXT LINK -krajniot uslov treba tyka da se definira
                nextLink = fullPage.getJSONObject("_links").get("next").toString();
                i++;
                if (i > 2) {
                    flag = false;
                }
            }
        }
    }

    private void parseJSONChannels(StringBuilder builder,JSONObject fullPage) throws JSONException {
        JSONArray allChannels=fullPage.getJSONArray("channels");
        for(int i=0;i<allChannels.length();i++){
            JSONObject channelObject=allChannels.getJSONObject(i);
            //RESTRICTIONS-dopolnitelno moze i da ima nad nekoj broj na followers
            if(!channelObject.get("followers").toString().equals("0")&&!channelObject.get("game").toString().equals("null")){
                Channel channel=channelService.createChannel(channelObject);
                if(channel.getFollowersNumber() > 50) {
                    channelService.addChannel(channel);
                }
            }
        }
    }


}
