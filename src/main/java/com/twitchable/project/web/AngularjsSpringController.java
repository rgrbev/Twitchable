package com.twitchable.project.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AngularjsSpringController {

	@RequestMapping(value = "/randomchannels", method = RequestMethod.GET)
	public String randomChannelsView(Model m){
		return "randomchannels";
	}
	
	@RequestMapping(value = "/viewstream", method = RequestMethod.GET)
	public String streamView(Model m){
		return "viewstream";
	}
	
	
	@RequestMapping(value = "/searchresults", method = RequestMethod.GET)
	public String searchresuls(Model m){
		return "searchresults";
	}
	
	
	@RequestMapping(value = "/searchtopgames", method = RequestMethod.GET)
	public String searchtopgames(Model m){
		return "searchtopgames";
	}
	
	@RequestMapping(value = "/toptenfollowers", method = RequestMethod.GET)
	public String mostFollowed(Model m){
		return "toptenfollowers";
	}
	
	@RequestMapping(value = "/toptenviews", method = RequestMethod.GET)
	public String mostViewed(Model m){
		return "toptenviews";
	}
	
	@RequestMapping(value = "/risingstars", method = RequestMethod.GET)
	public String risingStars(Model m){
		return "risingStars";
	}
}
