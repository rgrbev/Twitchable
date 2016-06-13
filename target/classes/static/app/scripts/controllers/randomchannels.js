'use strict';

/**
 * @ngdoc function
 * @name twitchableFrontEndApp.controller:RandomchannelsCtrl
 * @description
 * # RandomchannelsCtrl
 * Controller of the twitchableFrontEndApp
 */
angular.module('twitchableFrontEndApp')
  .controller('RandomchannelsCtrl',function (RandomChannels, $cookieStore, $scope, $location, $http) {

    this.randomchannels = RandomChannels.getData().query();
    
    
    
    if($cookieStore.get("lastLocation") && $cookieStore.get("lastLocation") != "/"){
    	$location.path($cookieStore.get("lastLocation"));
    	$cookieStore.remove("lastLocation");
    }
    
    $scope.setStreamChannel = function(channel){
    	$cookieStore.put('currentChannel', channel);
    	$location.path("/video");
    }
    
    
    
  });
