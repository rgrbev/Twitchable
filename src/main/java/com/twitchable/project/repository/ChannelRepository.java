package com.twitchable.project.repository;

import com.twitchable.project.model.Channel;
import com.twitchable.project.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by riste on 4/7/2016.
 */

public interface ChannelRepository extends MongoRepository<Channel, String> {

    public Channel findByDisplayName(String displayName);

    //DODADENI

    //@Query(value="{ 'mature' : ?0, 'broadcasterLanguage' : ?1 ,'game' : ?2 ,'name' : ?3}")
    @Query("{ $and:[" +
            "{ $or : [ { $where: '?0 == null' } , { rating :  { $gt : ?0} } ] }," +
            "{ $or : [ { $where: '?1 == null' } , { rating :  { $lt : ?1} } ] }," +
            "{ $or : [ { $where: '?2 == null' } , { broadcasterLanguage : ?2 } ] }," +
            "{ $or : [ { $where: '?3 == null' } , { game : ?3 } ] }," +
            "{ $or : [ { $where: '?4 == null' } , { name : { $regex: ?4, $options: 'm' } } ] }" +
            "]}")
    List<Channel> searchByCriteria(Double ratingFrom,Double ratingTo,String broadcasterLanguage,String game,String name);


    @Query(value="{ 'name' : ?0 }")
    public Channel findByName(String name);


    @Query("{ distinct : 'channel', key : 'game'}")
    public JSONArray listDistinctGames();


    public List<Channel> findDistinctChannelByGame(String game);

}
