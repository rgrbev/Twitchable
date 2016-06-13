'use strict';

/**
 * @ngdoc overview
 * @name twitchableFrontEndApp
 * @description
 * # twitchableFrontEndApp
 *
 * Main module of the application.
 */
angular
  .module('twitchableFrontEndApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngLoadingSpinner',
    'googlechart',
    'ngTable',
    'ngStorage'
  ])
  .config(function ($routeProvider, $sceDelegateProvider) {
    $routeProvider
    .when('/video', {
  	  templateUrl: 'viewstream',
  	  controller: 'ViewstreamCtrl',
  	  controllerAs: 'viewstream'
    })
      .when('/search', {
        templateUrl: 'searchresults',
        controller: 'ShowSearchResultsCtrl',
        controllerAs: 'searchresults'
      })
      .when('/searchtopgames', {
        templateUrl: 'searchtopgames',
        controller: 'SearchTopGamesCtrl',
        controllerAs: 'searchtopgames'
      })
      .when('/topbyfollowers', {
        templateUrl: 'toptenfollowers',
        controller: 'TopTenFollowersCtrl',
        controllerAs: 'toptenfollowers'
      })
      .when('/topbyviews', {
        templateUrl: 'toptenviews',
        controller: 'TopTenViewsCtrl',
        controllerAs: 'toptenviews'
      })
      .when('/risingstars', {
        templateUrl: 'risingstars',
        controller: 'RisingStarsCtrl',
        controllerAs: 'risingstars'
      })
      .when('/', {
        templateUrl: 'randomchannels',
        controller: 'RandomchannelsCtrl',
        controllerAs: 'randomchannels'
      })
      .otherwise({
        redirectTo: '/'
      });
    $sceDelegateProvider.resourceUrlWhitelist(['self', 'https://player.twitch.tv/**', 'https://www.twitch.tv/**']);
  });
