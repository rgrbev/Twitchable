'use strict';

/**
 * @ngdoc function
 * @name twitchableFrontEndApp.controller:RandomchannelsCtrl
 * @description
 * # RandomchannelsCtrl
 * Controller of the twitchableFrontEndApp
 */
angular.module('twitchableFrontEndApp')
  .controller('RisingStarsCtrl', function ($scope, $http, LiveStream, $location) {

  
	  
	  $http.get("/channel/risingStars")
	    .then(function(d) {
	    	var dict = {};
	    	$scope.data = [];
	    	$scope.colsData = [{"id":"date", "label":"Date", "type":"string"}];
	    	for(var i in d.data[1]){
	    		$scope.data.push(i);
	    		$scope.colsData.push({"id":i, "label":i, "type":"number"});
	    		var ob = d.data[1][i];
	        	var keys = Object.keys(ob);
	        	for(var j=0; j<keys.length; j++){
	        		var di = dict[keys[j]];
	        		if(di == null){
	        			dict[keys[j]] = [{"v":ob[keys[j]]}];
	        		} else {
	        			dict[keys[j]].push({"v":ob[keys[j]]});
	        		}
	        	}
	        }
	    	$scope.datesData = Object.keys(dict);
	    	$scope.finalData = [];
	    	for(var i=0; i<$scope.datesData.length; i++){
	    		var dateF = new Date(Date.parse($scope.datesData[i]));
	    		
	    		var temp = [{"v":dateF.getDate()+"/"+dateF.getMonth()+"/"+dateF.getFullYear()}];
	    		for(var j=0; j<dict[$scope.datesData[i]].length; j++){
	    			temp.push(dict[$scope.datesData[i]][j]);
	    		}
	    		$scope.finalData.push({"c": temp});
	    	}
	    	$scope.myChartObject = {
	    			  "type": "LineChart",
	    			  "displayed": false,
	    			  "data": {
	    			    "cols": $scope.colsData,
	    			    "rows": $scope.finalData
	    			  },
	    			  "options": {
	    			    "title": "",
	    			    "isStacked": "true",
	    			    "fill": 20,
	    			    "displayExactValues": true,
	    			    "vAxis": {
	    			      "title": "Rating",
	    			      "gridlines": {
	    			        "count": 6
	    			      }
	    			    },
	    			    "hAxis": {
	    			      "title": "Date"
	    			    }
	    			  },
	    			  "formatters": {}
	    			}
	    });
	  
	  
	  
    
  });
