'use strict';


angular.module('twitchableFrontEndApp')
  .directive('recommended', function () {
	  return {
			restrict : 'E',
			template : '<div ng-repeat="rec in $storage.recommendedChannels | limitTo:3 " class="user-panel bg-black-active color-palette">'
	        +'<div class="pull-left image">'
	         + '<img ng-if="rec.logo != ' + "'null'" + '" ng-src="{{rec.logo}}" class="img-circle" alt="User Image"/>'
	         + '<img ng-if="rec.logo == ' + "'null'" + '" ng-src="http://u.o0bc.com/avatars/no-user-image.gif" class="img-circle" alt="User Image"/>'
	        +'</div>'
	        +'<div class="pull-left info">'
	         + '<p>{{rec.displayName}}</p>'
	         + '<a href="" ng-click="setRecommendedChannel(rec)">View</a>'
	        +'</div>'
	      +'</div>',

			controller: ['$scope','$http','SearchResultsService', '$location', '$localStorage', '$cookieStore', '$route', function($scope, $http, SearchResultsService, $location, $localStorage, $cookieStore, $route){
				
				$scope.$storage = $localStorage;
				if(!$scope.$storage.recommendedChannels){
					$scope.$storage.recommendedChannels = [];
				}
				if($scope.$storage.recommendedChannels && $scope.$storage.recommendedChannels.length == 0){
					$http.get("/channel/recomended")
				    .then(function(data) {
				        $scope.$storage.recommendedChannels = data.data;
				        
				    });
				}
				
				$scope.setRecommendedChannel = function(channel){
			    	$cookieStore.put('currentChannel', channel);
			    	$cookieStore.put('currentRating', channel.rating);
			    	if($location.path() == "/video"){
			    		$route.reload();
			    	} else {
				    	$location.path("/video");
			    		
			    	}
			    }
				
					
				
				
						
					
				
				}
			             
			             
			]
	  
					
			
		};
  });
