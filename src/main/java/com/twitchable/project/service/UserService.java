package com.twitchable.project.service;

import com.twitchable.project.model.User;

import java.util.List;

/**
 * Created by riste on 4/7/2016.
 */
public interface UserService {
    public void createUser(User u);
    public List<User> getAllUsers();
    public User findUserByName(User u);
    public void deleteAll();
    public User getUser(String name);
}
