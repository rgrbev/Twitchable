'use strict';

describe('Controller: RandomchannelsCtrl', function () {

  // load the controller's module
  beforeEach(module('twitchableFrontEndApp'));

  var RandomchannelsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    RandomchannelsCtrl = $controller('RandomchannelsCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(RandomchannelsCtrl.awesomeThings.length).toBe(3);
  });
});
