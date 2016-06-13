package com.twitchable.project.rest;

import com.twitchable.project.model.Channel;
import com.twitchable.project.model.User;
import com.twitchable.project.repository.UserRepository;
import com.twitchable.project.service.ChannelService;
import com.twitchable.project.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by riste on 6/7/2016.
 */

@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class UserRest {
    @Autowired
    private ChannelService service;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET,produces = {"application/json"})
    public User searchByCriteria(HttpSession session) throws InterruptedException, JSONException, IOException {
        User user = new User();
        User newUser = null;
        if (session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
            newUser = userService.getUser(user.getName());
            if(newUser.getDisplayName() == null) {
                newUser = createUser(user);
                userRepository.delete(newUser);
                userRepository.save(newUser);
                newUser = userService.getUser(newUser.getName());
            }
            return newUser;
        }
        return newUser;
    }

    /*@RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpSession session) {
        if(session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }
    }*/

    private User createUser(User user) throws IOException, InterruptedException, JSONException {
        String userLink = "https://api.twitch.tv/kraken/users/" + user.getName();
        StringBuilder builder = getUserDetails(userLink);
        JSONObject userObject = new JSONObject(builder.toString());
        User newUser = new User();
        if(userObject.has("display_name")) {
            newUser.setDisplayName(userObject.getString("display_name"));
        }
        if(userObject.has("name")) {
            newUser.setName(userObject.getString("name"));
        }
        if(userObject.has("type")) {
            newUser.setType(userObject.getString("type"));
        }
        if(userObject.has("bio")) {
            newUser.setBio(userObject.getString("bio"));
        }
        if(userObject.has("created_at")) {
            newUser.setCreatedAt(userObject.getString("created_at"));
        }
        if(userObject.has("updated_at")) {
            newUser.setUpdatedAt(userObject.getString("updated_at"));
        }
        if(userObject.has("logo")) {
            newUser.setLogo(userObject.getString("logo"));
        }
        newUser.setId(user.getId());
        newUser.setSelfLink(userLink);
        return newUser;
    }

    private StringBuilder getUserDetails(String link) throws InterruptedException, IOException {
        StringBuilder builder=new StringBuilder();
        //try{
        URL url = new URL(link);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null){
            builder.append(inputLine);
            builder.append("\n");
        }
        return builder;
    }
}
