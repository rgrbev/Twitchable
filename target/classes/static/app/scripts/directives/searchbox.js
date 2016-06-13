'use strict';


angular.module('twitchableFrontEndApp')
  .directive('searchbox', function () {
	  return {
			restrict : 'E',
			template :
				'<div id="filter-panel" class="collapse filter-panel">'
      +'<div style="padding-left: 17%; padding-top: 18px; padding-bottom:13px">'
          +'<form class="form-inline" role="form">'
              +'<div class="form-group">'
                  +'<label for="selectRating" style="color: white">Rating:</label>'
              +'&nbsp;<select ng-model="rating" id="selectRating" class="form-control">'
                  +'<option value="1">1 star</option>'
                  +'<option value="2">2 stars</option>'
                  +'<option value="3">3 stars</option>'
                  +'<option value="4">4 stars</option>'
                  +'<option value="5">5 stars</option>'
              +'</select>'                               
              +'</div>'
             + '<div class="form-group" style="margin-left: 20px">'
                 + '<label for="selectLanguage" style="color: white">Language:</label>'
             + '&nbsp;<select ng-model="broadcasterLanguage" id="selectLanguage" class="form-control">'
                  +'<option ng-repeat=" dLang in distinctLang" value="{{dLang[0]}}">{{dLang[1]}}</option>'
                  
              +'</select>'                             
              +'</div>'
              +'<div class="form-group" style="margin-left: 20px">'
                  +'<label for="selectGame" style="color: white">Game:</label>'
              +'&nbsp;<select ng-model="game" id="selectGame" class="form-control">'
                  + '<option ng-repeat="dGame in distinctGames" value="{{dGame}}">{{dGame}}</option>'
              +'</select>'                                
             +' </div>'
          +'</form>'
      +'</div>'
 +' </div>'
+'<form style="margin-left: 32%" class="navbar-form navbar-left" role="search">'
  +'<div class="form-group">'
      +'<input type="text" ng-model="name" class="form-control" id="navbar-search-input" placeholder="Search"/>'
  +'</div>'
  +'<button class="btn btn-info btn-flat" ng-click="searchFor(query)">Search</button>'
+'</form>'
+'<ul class="nav navbar-nav">'
 +'<li>'
     +'<a data-toggle="collapse" ng-click="clearFilter()" data-target="#filter-panel" href="">Filter</a>'
  +'</li>'
+'</ul>'
+'<div class="navbar-custom-menu">'
	+'<ul class="nav navbar-nav">'
		+'<li ng-model="loggedinUser.data" ng-if="loggedinUser.data ==' + "''" + '">'
			+'<a ng-href="/login/twitch" ng-click="loginUser()">'
				+'Login'
			+'</a>'
		+'</li>'
		+ '<li ng-model="loggedinUser.data" ng-if="loggedinUser.data !=' + "''" + '" class="dropdown user user-menu">'
			+ '<a href="" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">'
				+ '<img ng-if="loggedinUser.data.logo != ' + "'null'" + '" class="user-image" ng-src="{{loggedinUser.data.logo}}" alt="User Image" />'
				+ '<img ng-src="https://image.freepik.com/free-icon/user-male-shape-in-a-circle--ios-7-interface-symbol_318-35357.png" ng-if="loggedinUser.data.logo == ' + "'null'" + '" class="user-image" alt="User Image" />'
				+ '<span class="hidden-xs">{{loggedinUser.data.displayName}}</span>'
			+ '</a>'
			+ '<ul class="dropdown-menu">'
				+ '<li class="user-header">'
				+ '<img ng-if="loggedinUser.data.logo != ' + "'null'" + '" class="img-circle" ng-src="{{loggedinUser.data.logo}}" alt="User Image" />'
				+ '<img ng-src="https://image.freepik.com/free-icon/user-male-shape-in-a-circle--ios-7-interface-symbol_318-35357.png" ng-if="loggedinUser.data.logo == ' + "'null'" + '" class="img-circle" alt="User Image" />'
					+ '<p>'
						+ '{{loggedinUser.data.displayName}}'
						+ '<small>'
							+ 'Member since: {{loggedinUser.data.createdAt|date:"MMMM yyyy"}}'
						+'</small>'
					+'</p>'
				+'</li>'
				+ '<li class="user-footer">'
					+ '<div class="pull-left">'
						+ '<a ng-href="http://www.twitch.com/{{loggedinUser.data.name}}/profile" class="btn btn-default btn-flat">Profile' +'</a>'
					+'</div>'
					+ '<div class="pull-right">'
					+ '<a href="" ng-click="logoutUser()" class="btn btn-default btn-flat">Log out' +'</a>'
				+'</div>'
				+'</li>'
			+ '</ul>'
		+ '</li>'
	+'</ul>'
+'</div>',

			controller: ['$scope','$http','SearchResultsService', '$location', '$cookieStore', '$localStorage', function($scope, $http, SearchResultsService, $location, $cookieStore, $localStorage){
				
					$scope.name = null;
					$scope.game = null;
					$scope.rating = null;
					$scope.broadcasterLanguage = null;
					
					if(!$scope.channels && $location.path() == '/search'){
						$location.path("/");
					}
					
					var lang = {"en": "English", "tr":"Turkish", 
							"fr":"French", "de":"German", "ru":"Russian", 
							"pl":"Polish", "zh":"Chinese", "da":"Danish",
							"es":"Spanish","pt":"Portuguese", "sv":"Swedish",
							"bg":"Bulgarian", "cs":"Czech", "it":"Italian", 
							"other":"Other", "ja":"Japanese", "nl":"Dutch", "fi":"Finnish",
							"ar":"Arabic", "sk":"Slovak", "no":"Norwegian", "ko":"Korean"};
				
					 $http.get("/user/login")
					    .then(function(response) {
					        $scope.loggedinUser = response;
					    });
					 
					 $http.get("/channel/findDistinctGames")
					    .then(function(response) {
					        $scope.distinctGames = response.data;
					    }); 
					    
					 $http.get("/channel/findDistinctLanguages")
					    .then(function(response) {
					        $scope.distinctLang = [];
					        for(var i=0; i<response.data.length; i++){
					        	$scope.distinctLang.push([response.data[i], lang[response.data[i]]]);
					        }
					        
					    });
					 
					$scope.clearFilter = function(){
						
						$scope.game = null;
						$scope.rating = null;
						$scope.broadcasterLanguage = null;
						
					}
					
					$scope.loginUser = function(){
						$cookieStore.put("lastLocation", $location.path());
					}
					
					$scope.logoutUser = function(){
						$http.post("/logout")
					    .then(function() {
					        $scope.loggedinUser.data = "";
					        $localStorage.$reset();
					    });
					}
					
					$scope.searchFor = function(){
						
						var query = {
								channelProp: '',
								name: $scope.name,
								game: $scope.game,
								rating: $scope.rating,
								broadcasterLanguage: $scope.broadcasterLanguage
								
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
				        	  $scope.channels = response.data;
				        	  //SearchResultsService.setChannels(response.data);
				        	  if(response.data.length === 0){
				        		  $scope.message = "No channels found :(";
				        	  } else {
				        		  $scope.message = "";
				        	  }
				        	  $scope.name = null;
				        	  $location.path("/search");
				        	  
				          });
					}
				}
			             
			             
			]
	  
					
			
		};
  });
