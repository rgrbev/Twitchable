'use strict';

/**
 * @ngdoc service
 * @name twitchableFrontEndApp.RandomChannels
 * @description
 * # RandomChannels
 * Factory in the twitchableFrontEndApp.
 */
angular.module('twitchableFrontEndApp')
  .factory('SearchResultsService', function () {
	  
	  var service = {};
	  service.channels = null;
	  
	  service.setChannels = function(c){
		  service.channels = c;
	  }
	  
	  service.getChannels = function(){
		  return service.channels;
	  }
	  
	  
    return service;

  });
