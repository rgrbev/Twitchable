package com.twitchable.project.service.impl;

import com.twitchable.project.model.User;
import com.twitchable.project.repository.UserRepository;
import com.twitchable.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by riste on 4/7/2016.
 */

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRep;

    @Override
    public void createUser(User u) {
        //tyka treba da se zimaat za user informacii od HomeController
        userRep.save(u);
    }



    @Override
    public List<User> getAllUsers() {
        return userRep.findAll();
    }

    @Override
    public User findUserByName(User u) {
        return userRep.findOne(u.getId());
    }

    @Override
    public void deleteAll() {
        userRep.deleteAll();
    }

    @Override
    public User getUser(String name) {
        return userRep.findUserByName(name);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRep.findUserByName(s);
        return new org.springframework.security.core.userdetails.User(user.getName(), null, null);
    }
}
