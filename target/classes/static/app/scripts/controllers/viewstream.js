'use strict';

/**
 * @ngdoc function
 * @name twitchableFrontEndApp.controller:ViewstreamCtrl
 * @description
 * # ViewstreamCtrl
 * Controller of the twitchableFrontEndApp
 */
angular.module('twitchableFrontEndApp')
  .controller('ViewstreamCtrl', function (LiveStream, $scope, $http, $cookieStore) {
	  
	  var channel = $cookieStore.get('currentChannel');
	  
	  var ob = this;
	  
	  $scope.rating = 5;
	  
    this.streamLink = channel.liveStream;
    
    this.streamTitle = channel.status;
    
    this.channelName = channel.name;
    
    this.channelLink = channel.url + "/profile";
    
    this.channelLogo = channel.logo;
    
    this.numberFollowers = channel.followersNumber;
    
    this.numberViewers = channel.viewsNumber;
    
    if($cookieStore.get('currentRating')){
    	this.rating = $cookieStore.get('currentRating');
    } else {
    	this.rating = channel.rating;
    }
    
    
    this.chatLink = channel.url + "/chat";

    $scope.rateFunction = function(rating) {
    	var ratingData = JSON.stringify({channelProp:"", channelName: ob.channelName, userName: $scope.loggedinUser.data.name, ratingD: rating});
    	
		  $http.post('/channel/rateJSON', ratingData)
		  .success(function(data, status, headers, config){
			  $scope.rating = data.rating;
			  $cookieStore.put('currentChannel', channel);
			  $cookieStore.put('currentRating', data.rating);
		  }).error(function(data, status, headers, config){
			  console.log('error');
		  })
	};

  });
