'use strict';

describe('Service: RandomChannels', function () {

  // load the service's module
  beforeEach(module('twitchableFrontEndApp'));

  // instantiate service
  var RandomChannels;
  beforeEach(inject(function (_RandomChannels_) {
    RandomChannels = _RandomChannels_;
  }));

  it('should do something', function () {
    expect(!!RandomChannels).toBe(true);
  });

});
