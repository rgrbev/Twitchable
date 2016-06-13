package com.twitchable.project.service.impl;

import com.mongodb.BasicDBObject;
import com.twitchable.project.config.SpringMongoConfig1;
import com.twitchable.project.model.Channel;
import com.twitchable.project.model.Rating;
import com.twitchable.project.model.RecomendedChannel;
import com.twitchable.project.model.User;
import com.twitchable.project.repository.ChannelRepository;
import com.twitchable.project.repository.UserRepository;
import com.twitchable.project.service.ChannelService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpSession;

/**
 * Created by riste on 4/7/2016.
 */

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    ChannelRepository channelRep;
    @Autowired
    UserRepository userRep;
    
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig1.class);
    MongoOperations mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");

    @Override
    public Channel addChannel(Channel c) {
        return channelRep.save(c);
    }

    @Override
    public List<Channel> getAllChannels() {
        List<Channel> lista=channelRep.findAll();
        return channelRep.findAll();
    }

    @Override
    public List<Channel> findChannelForUser(User u) {
        // tuka da se najdat site kanali na koj userot ima napraveno follow
        return null;
    }

    @Override
    public Channel findChannelByName(String display_name) {
        return channelRep.findByDisplayName(display_name);
    }

    @Override
    public Channel findOne(String id) {
        return channelRep.findOne(id);
    }

    @Override
    public void deleteAll() {
        channelRep.deleteAll();
    }

    public List<Channel> findTopChannels(){
        return channelRep.findAll(new Sort(Sort.Direction.DESC, "followersNumber")).subList(0,10);
    }

    public List<Channel> findMostSubscribed(){
        return channelRep.findAll(new Sort(Sort.Direction.DESC, "viewsNumber")).subList(0,10);
    }

    @Override
    public List<String> getAllGames(){
        BasicDBObject distinctCommand = new BasicDBObject("distinct", "channel")
                .append("key", "game");
        List<String> games = (List)mongoOperation.executeCommand(distinctCommand).get("values");
        games.remove("null");
        return games;
    }

    @Override
    public List<String> getAllLanguages(){
        BasicDBObject distinctCommand = new BasicDBObject("distinct", "channel")
                .append("key", "broadcasterLanguage");
        List<String> languages = (List)mongoOperation.executeCommand(distinctCommand).get("values");
        languages.remove("null");
        return languages;
    }


    //DODADENI
    //Novi dodadeni metodi
    //listing the channels
    
    @Override
    public List<Channel> searchChannels(String rating,String broadcasterLanguage,String game,String name){
        Double ratingD=null,ratingDT=null;
        if(rating!=null){
            ratingD=Double.parseDouble(rating)-1;
            ratingDT=ratingD+2;
        }
        List<Channel> channels=channelRep.searchByCriteria(ratingD,ratingDT,broadcasterLanguage,game,name);
        return channels;
    }

    
    @Override
    public List<Channel> getRecomendedChannelsForUser(HttpSession session) throws IOException, InterruptedException, JSONException {
        List<Channel> finalRecomendedOnlineChannels = new ArrayList<>();
        List<Channel> finalRecomendedOfflineChannels = new ArrayList<>();
        if(session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String link = "https://api.twitch.tv/kraken/users/" + user.getName() + "/follows/channels?direction=DESC&limit=100&offset=0&sortby=last_broadcast";
            StringBuilder builder = getData(link);
            JSONObject channelsObject = new JSONObject(builder.toString());
            JSONArray follows = channelsObject.getJSONArray("follows");

            StringBuilder file = getRecomendations();
            JSONObject recomendations = new JSONObject(file.toString());
            List<RecomendedChannel> recomendedChannelList = new ArrayList<>();
            for (int i = 0; i < follows.length(); i++) {
                JSONObject row = follows.getJSONObject(i);
                JSONObject channelObject = row.getJSONObject("channel");
                Channel channel = createChannel(channelObject);
                
                if(recomendations.has(channel.getName())) {
                	
                    JSONObject similarChannels = recomendations.getJSONObject(channel.getName());
                    String[] similarChannelsArray = similarChannels.toString().split(",");
                    for(String s : similarChannelsArray) {
                        String[] rec = s.split(":");
                        String name;
                        name = rec[0].replaceAll("\\{","").replaceAll("\\}","");
                        String similar = rec[1].replaceAll("\\}","");
                        Integer similarity = Integer.parseInt(similar);
                        RecomendedChannel recomendedChannel = new RecomendedChannel(name,similarity);
                        recomendedChannelList.add(recomendedChannel);
                    }
                }
            }
            Set<RecomendedChannel> tmp = new HashSet<>();
            tmp.addAll(recomendedChannelList);
            recomendedChannelList.clear();
            recomendedChannelList.addAll(tmp);
            
            for(RecomendedChannel rc : recomendedChannelList) {
                String searchStream = "https://api.twitch.tv/kraken/search/streams?q=" + rc.getName();
                System.out.println(searchStream);
                StringBuilder sb = getData(searchStream);
                JSONObject channelObj = new JSONObject(sb.toString());
                JSONArray stream = channelObj.getJSONArray("streams");
                if(stream != null & stream.length() > 0) {
                    Channel channel = createChannel(stream.getJSONObject(0).getJSONObject("channel"));
                    if(!finalRecomendedOnlineChannels.contains(channel)) {
                        finalRecomendedOnlineChannels.add(channel);
                    }
                } else {
                    String searchChannel = "https://api.twitch.tv/kraken/search/channels?q=" + rc.getName();
                    StringBuilder sbChannel = getData(searchChannel);
                    System.out.println(sbChannel.toString());
                    JSONObject channelObject = new JSONObject(sbChannel.toString());
                    JSONArray channels = channelObject.getJSONArray("channels");
                    if(channels != null & channels.length() > 0 ) {
                        Channel channel = createChannel(channels.getJSONObject(0));
                        if(!finalRecomendedOfflineChannels.contains(channel)) {
                            finalRecomendedOfflineChannels.add(channel);
                        }
                    }
                }
                if(finalRecomendedOnlineChannels.size() > 3) {
                    break;
                }
            }
        }
        if(finalRecomendedOnlineChannels.size() > 0) {
            return finalRecomendedOfflineChannels;
        } else {
        	Collections.shuffle(finalRecomendedOfflineChannels);
            return finalRecomendedOfflineChannels;
        }
    }

    private StringBuilder getRecomendations() throws IOException {
        File file = new File("");
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()+"\\results.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb;
        } finally {
            br.close();
        }
    }


    @Override
    public Channel rateChannel(String channelName, String userName, double rating) {
        Channel channel=channelRep.findByName(channelName);
        
        User user=userRep.findUserByName(userName);
        if(user==null){
            //Ne treba da e null
            user=new User();
            user.setDisplayName(userName);
            user.setName(userName);
            userRep.save(user);
        }
        Date date=new Date();
        Rating rate=new Rating();
        rate.setUser(user);
        rate.setDate(date);
        rate.setRating(rating);
        
        //System.out.println(channel.toString());

        int alreadyRated=0;
        if(channel.getRaters() != null){
        for(int i=0;i<channel.getRaters().size();i++){
            Rating r=channel.getRaters().get(i);
            if(r.getUser()!=null&&user!=null)
            if(r.getUser().getId()!=null&&user.getId()!=null)
            if(r.getUser().getId().equals(user.getId())){
                    r.setRating(rating);
                    r.setDate(date);
                    alreadyRated=1;
                    System.out.println(channel.getRaters().get(i).toString());
                    List<Rating> ratings=channel.getRaters();
                    System.out.print(r.getDate());
                    r.setRating(rating);
                    ratings.set(i, r);
                    channel.setRaters(ratings);
                    System.out.print(r.getDate());
                    System.out.println(channel.getRaters().get(i).toString());
                    break;
            }
        }
        }
        if(alreadyRated==0) {
            List<Rating> ratings=channel.getRaters();
            ratings.add(rate);
            channel.setRaters(ratings);
        }
        channelRep.save(channel);
        System.out.println("rating");
        return channel;
    }

    //rating na kanali od logiraniot korisnik so daden rating
    //vo isto vreme se update-ira i samiot rating double vrednost za kanalot(vidi vo Channel klasa)
    //vrakame channel za da moze da se prikaze updatiraniot channel rating
    

    //loginUser
    //samo so loginot i potoa vnes i kaj nas



    //random generiranje na lista na prvi kanali


    //generiranje na kanali spored rekomendacija




    //New videos with highest subscribers and ratings
    //razgledajte go samo dali e okej vaka
    //moze da napravime restrikcija samo na posledniot mesec
    //2*views+4*ratings+10*subscribers
    //Od posleden mesec
    @Override
    public List<Channel> risingStars() {
        //samo od posleden mesec
        List<Channel> list=channelRep.findAll();
        Channel temp;
        for(int i=0; i < list.size()-1; i++){
            Channel c=list.get(i);
            double risingStarCalculate=0.0;
            risingStarCalculate=(c.getViewsNumber()*2)+(c.getRating()*4)+(c.getFollowersNumber()*10);
            for(int j=1; j < list.size()-i; j++){
                Channel c1=list.get(j);
                double risingStarCalculate1=0.0;
                risingStarCalculate1=(c1.getViewsNumber()*2)+(c1.getRating()*4)+(c1.getFollowersNumber()*10);
                if(risingStarCalculate > risingStarCalculate1){
                    temp=list.get(i);
                    list.set(i,list.get(j));
                    list.set(j,temp);
                }
            }
        }

        return list.subList(0,10);
    }



    //Create channel
    public Channel createChannel(org.json.JSONObject channelObject) throws JSONException {
        String username = null;
        Channel channel = new Channel();
        if(channelObject.has("mature")) {
            channel.setMature(channelObject.getString("mature"));
        }
        if(channelObject.has("status")) {
            channel.setStatus(channelObject.getString("status"));
        }
        if(channelObject.has("broadcaster_language")) {
            channel.setBroadcasterLanguage(channelObject.getString("broadcaster_language"));
        }
        if(channelObject.has("display_name")) {
            channel.setDisplayName(channelObject.getString("display_name"));
        }
        if(channelObject.has("game")) {
            channel.setGame(channelObject.getString("game"));
        }
        if(channelObject.has("mature")) {
            channel.setMature(channelObject.getString("mature"));
        }
        if(channelObject.has("name")) {
            channel.setName(channelObject.getString("name"));
            username = channel.getName();
        }
        if(channelObject.has("createdAt")) {
            channel.setCreatedAt(channelObject.getString("createdAt"));
        }
        if(channelObject.has("updatedAt")) {
            channel.setUpdatedAt(channelObject.getString("updatedAt"));
        }
        if(channelObject.has("logo")) {
            channel.setLogo(channelObject.getString("logo"));
        }
        if(channelObject.has("url")) {
            channel.setUrl(channelObject.getString("url"));
        }
        if(channelObject.has("views")) {
            channel.setViewsNumber(Long.parseLong(channelObject.getString("views")));
        }
        if(channelObject.has("followers")) {
            channel.setFollowersNumber(Long.parseLong(channelObject.getString("followers")));
        }
        String streamUrl = "https://api.twitch.tv/kraken/streams/"+username;
        String liveStream = "https://player.twitch.tv/?channel="+username;
        String liveChat = "https://www.twitch.tv/"+username+"/chat?popout=";

        channel.setStreamUrl(streamUrl);
        channel.setLiveStream(liveStream);
        channel.setLiveChat(liveChat);

        return channel;
    }



    //Get data from api
    public StringBuilder getData(String link) throws InterruptedException, IOException {
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


    @Override
    public List risingStarsByRating() {
        //zemanje na site kanali
        List<Channel> allChannels=getAllChannels();
        //potrebna hashmapa za sekoj kanal ke ima mapa so koja ke se presmetyva sredna vrednost
        //spored mesec ili den(sega za sega den ke da e)
        HashMap<Channel,TreeMap<Date,ArrayList<Double>>> channelMaps=new HashMap<Channel,TreeMap<Date,ArrayList<Double>>>();
        //za sekoj kanal
        for(Channel channel:allChannels) {
            //kreiranje na poedinechna TreeMap-a
            //izbrana e treemapa zaso sortot spored date sama go pravi grin emoticon
            TreeMap<Date, ArrayList<Double>> channelTreeMap = new TreeMap<Date, ArrayList<Double>>();
            //site rejtinzi treba za kanalot da se izminat
            List<Rating> listRatingsForChannels = channel.getRaters();
            if (listRatingsForChannels != null) {
                for (Rating rating : listRatingsForChannels) {
                    //Dokolku treba ke se ogranici za datym
                    SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
                    String dateFromated = ft.format(rating.getDate());
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    Date dateRating = null;
                    try {
                        dateRating = df.parse(dateFromated);
                        String newDateString = df.format(dateRating);
                        //System.out.println(newDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Double ratingValue = rating.getRating();

                    ArrayList<Double> ratingValues = new ArrayList<Double>();
                    //Ako mapata go sodrzi istiot datym kako klych da se dodade vo nizata
                    if (channelTreeMap.containsKey(dateRating)) {
                        ratingValues = channelTreeMap.get(dateRating);
                    }
                    ratingValues.add(ratingValue);
                    channelTreeMap.put(dateRating, ratingValues);
                }
                if (listRatingsForChannels.size() != 0)
                    channelMaps.put(channel, channelTreeMap);
            }
        }


        //String print=print(channelMaps);
        
        List finalList=caclulateRisingStars(channelMaps);
        
        return finalList;
    }

    private List caclulateRisingStars(HashMap<Channel, TreeMap<Date, ArrayList<Double>>> channelMaps) {
        String print = "";
        HashMap<Channel, TreeMap<Date, Double>> newMap=new HashMap<Channel, TreeMap<Date, Double>>();
        List<Channel> finalListRisingStars=new ArrayList<Channel>();
        for (Channel key : channelMaps.keySet()) {
            System.out.println("Kanalot e " + key);
            //za sekoj kanal prati dole na fynkcijata i vrati rez
            ArrayList<Double> avgList=new ArrayList<Double>();
            TreeMap<Date, Double> dates=new TreeMap<Date, Double>();
            print += "---------------------------" + "\n";
            print += "Key: " + key.getName() + "\n";
            TreeMap<Date, ArrayList<Double>> values = channelMaps.get(key);
            for (Date date : values.keySet()) {
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                String dateFormated = DATE_FORMAT.format(date);
                print += "--->Key: " + dateFormated + "\n";
                ArrayList<Double> valuesDate = values.get(date);
                double valuesD=0.0;
                for (Double d : valuesDate) {
                    print += "----->" + d + "\n";
                    valuesD+=d;
                }
                Double valueToCalculate=valuesD/valuesDate.size();
                System.out.println("Za datymot " + dateFormated + " vrednosta e " + valueToCalculate);
                //ova ke treba za od site sredni vrednosti da se presmeta koj ima najgolem stepen na increase
                avgList.add(valueToCalculate);
                //ova ke treba za filip za da se nacrta
                dates.put(date,valueToCalculate);
            }

            Double increase=calculateAvgIncrease(avgList);
            System.out.println("Rezyltat:::"+key.getName() + " " + increase);
            key.setRisingStar(increase);
            finalListRisingStars.add(key);
            newMap.put(key, dates);
        }

        Collections.sort(finalListRisingStars, new Comparator<Channel>() {
            public int compare(Channel o1, Channel o2) {
                if (o1.getRisingStar() < o2.getRisingStar())
                    return 1;
                return -1;
            }

        });
        if (finalListRisingStars.size() >= 5)
            finalListRisingStars = finalListRisingStars.subList(0, 5);

        HashMap<String, HashMap<Date, Double>> newMapFiltered=new HashMap<String, HashMap<Date, Double>>();

        for(Channel c:finalListRisingStars){
            System.out.println(c.getRisingStar() + " " + c.getName());
            System.out.println(newMap.get(c).toString());
            newMapFiltered.put(c.getName(), new HashMap<Date, Double>(newMap.get(c)));
        }

        List finalList=new ArrayList();
        //lista za top kanali
        finalList.add(finalListRisingStars);
        //lista za nova mapa
       
        finalList.add(newMapFiltered);
        //vrakanje na listata
        return finalList;
    }

    private String print(HashMap<Channel, TreeMap<Date, ArrayList<Double>>> channelMaps) {
        String print="";
        for (Channel key : channelMaps.keySet()) {
            print+="---------------------------"+"\n";
            print+="Key: " + key.getName()+"\n";
            TreeMap<Date, ArrayList<Double>> values=channelMaps.get(key);
            for (Date date : values.keySet()) {
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                String dateFormated = DATE_FORMAT.format(date);
                print+="--->Key: " + dateFormated+"\n";
                ArrayList<Double> valuesDate=values.get(date);
                for(Double d:valuesDate){
                    print+="----->"+d+"\n";
                }
            }
        }
        return print;
    }



    public Double calculateAvgIncrease(List<Double> list){
        List<Double> avgList=new ArrayList<Double>();
        System.out.println("SredniVrednosti:::");
        for(Double d:avgList){
            System.out.print(d);
        }
        Double sum=0.0;

        if(list.size()==1){
            return sum;
        }

        for (int i = 1; i < list.size(); i++) {
            System.out.println("Sobiranja:: " + list.get(i) + " " + list.get(i - 1));
            Double avgValue = list.get(i) - list.get(i - 1);
            avgList.add(avgValue);
            sum += avgValue;
        }


        return sum/avgList.size();
    }


}
