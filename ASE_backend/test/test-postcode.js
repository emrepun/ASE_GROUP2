const postcodeService =  require('../services/postcodeService.js');
var nock = require('nock');
var expect  = require('chai').expect;
var request = require('request');



describe('Postcode details', function(){

  it('returns the longitute and latitude of a postcode', function(){
    postcodeService.getPostcodeDetails('BN1 7JJ').then(details => {
      
    });
  });
});
