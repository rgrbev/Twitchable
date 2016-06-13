'use strict';

/**
 * @ngdoc function
 * @name twitchableFrontEndApp.controller:RandomchannelsCtrl
 * @description
 * # RandomchannelsCtrl
 * Controller of the twitchableFrontEndApp
 */
angular.module('twitchableFrontEndApp')
  .controller('SearchTopGamesCtrl', function (SearchResultsService, $cookieStore, $localStorage, $scope, $location) {

	  $scope.$storage = $localStorage;
	  $scope.channels = $scope.$storage.topGamesChannels;
	  if($scope.channels.length == 0){
		  $scope.message = "No channesl found :(";
	  } else {
		  $scope.message = "";
	  }
	  
	  $scope.setStreamChannel = function(channel){
		    $cookieStore.put('currentChannel', channel);
	    	$location.path("/video");
	    }
	  
  });
