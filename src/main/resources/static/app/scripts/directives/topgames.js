'use strict';


angular.module('twitchableFrontEndApp')
  .directive('topgames', function () {
	  return {
			restrict : 'E',
			template : '<div ng-repeat="g in topGames" class="col-md-4">'
			+'<div class="box box-default text-center">'
			+'<div class="box-header with-border">'
			+'<h3 class="box-title">'
				+'{{g.game.name}}'
			+'</h3>'
			+'</div>'
			+'<div class="box-body">'
				+'<img ng-src="{{g.game.box.large}}" />'
			+'</div>'
			+'<a href="" ng-click="findTopGames(g.game.name)" class="btn btn-primary btn-flat"><i class="fa fa-search"></i> Find channels</a><br/>'
		+'</div>'
		
	+'</div>',

			controller: ['$scope','$http','SearchResultsService', '$location', '$localStorage', '$cookieStore', '$route', function($scope, $http, SearchResultsService, $location, $localStorage, $cookieStore, $route){
				
				$http.get("https://api.twitch.tv/kraken/games/top?limit=10&offset=0")
			    .then(function(data) {
			    	$scope.topGames = data.data.top;
			    });
				
					$scope.findTopGames = function(game){
						var query = {
								channelProp: '',
								name: null,
								game: game,
								rating: null,
								broadcasterLanguage: null
								
						}
						var d=JSON.stringify(query);
						console.log(d);
						$http({
					          method  : 'POST',
					          dataType: 'json',
					          url     : '/channel/searchJSON',
					          data    :  d,
					          headers : {'Content-Type': 'application/json'} 
					         })
				          .then(function (response) {
				        	  $scope.$storage = $localStorage;
				        	  $scope.$storage.topGamesChannels = response.data;
				        	  $location.path("/searchtopgames");
				        	  
				          });
					}
					
				}
			             
			             
			]
	  
					
			
		};
  });
