'use strict';

/**
 * @ngdoc function
 * @name twitchableFrontEndApp.controller:RandomchannelsCtrl
 * @description
 * # RandomchannelsCtrl
 * Controller of the twitchableFrontEndApp
 */
angular.module('twitchableFrontEndApp')
  .controller('ShowSearchResultsCtrl', function (SearchResultsService, $cookieStore, LiveStream, $scope, $location) {

	  
	  console.log($cookieStore.get("findTopGames"));
	  
	  if($cookieStore.get('topGamesMessage')){
		  $scope.message = $cookieStore.get('topGamesMessage');
	  }
	  if($cookieStore.get("findTopGames")){
		  $scope.channels = $cookieStore.get("findTopGames");
	  }
    
	  $scope.setStreamChannel = function(channel){
		    $cookieStore.put('currentChannel', channel);
	    	$location.path("/video");
	    }
  });
