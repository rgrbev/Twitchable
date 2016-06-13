'use strict';

/**
 * @ngdoc service
 * @name twitchableFrontEndApp.LiveStream
 * @description
 * # LiveStream
 * Factory in the twitchableFrontEndApp.
 */
angular.module('twitchableFrontEndApp')
  .factory('LiveStream', function () {
	  
	  var service = {};
	  service.streamChannel = null;
	
	  service.setStreamChannel = function(channel){
		  service.streamChannel = channel;
	  }
	  
	  service.getStreamLink = function(){
		  return service.streamChannel.liveStream;
	  }
	  
	  service.getStreamChannel = function(){
		  return service.streamChannel;
	  }

    return service;

  });
