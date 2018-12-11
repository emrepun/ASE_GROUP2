const postcodeService =  require('../services/postcodeService.js');
var expect  = require('chai').expect;
var request = require('request');


describe('Postcode details', function(){

  it('returns the longitute and latitude of a postcode', function(){
    postcodeService.getPostcodeDetails('BN1 7JJ').then(details => {
       expect(details.Latitude).to.equal('50.845112');
       expect(details.Longitude).to.equal('-0.132927')
    });
  });

  it('returns undefined', function(){
    postcodeService.getPostcodeDetails('0009011').then(details => {
      expect(details.Latitude).to.equal(undefined);
      expect(details.Longitude).to.equal(undefined)
    });
  });

});

describe('Postcodes near around', function(){
  it('does not find any postcodes', function() {
  postcodeService.getPostcodesNearAround('50.845112','-0.132927').then(details => {
    expect(Array.isArray(details)).to.equal(true);
    expect(details).to.have.length.above(1);
  });
  });

  it('returns postcodes within an area', function() {
  postcodeService.getPostcodesNearAround('0','0').then(details => {
    expect(Array.isArray(details)).to.equal(true);
    expect(details.length).to.equal(0);
  });
  });
});
