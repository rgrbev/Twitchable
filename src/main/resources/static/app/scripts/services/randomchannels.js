'use strict';

/**
 * @ngdoc service
 * @name twitchableFrontEndApp.RandomChannels
 * @description
 * # RandomChannels
 * Factory in the twitchableFrontEndApp.
 */
angular.module('twitchableFrontEndApp')
  .factory('RandomChannels', function ($resource) {
	  
	  var service = {};
	  
	  service.data = $resource('/channel/random/online', {}, {});
	  
	  service.getData = function(){
		  return service.data;
	  };
	  
	  
    return service;

  });
