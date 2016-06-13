package com.twitchable.project.repository;

import com.twitchable.project.model.Channel;
import com.twitchable.project.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by riste on 4/7/2016.
 */
public interface UserRepository extends MongoRepository<User, String> {
    @Query(value="{ 'name' : ?0}")
    User findUserByName(String name);

}
