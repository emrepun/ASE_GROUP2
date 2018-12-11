const priceService =  require('../services/priceService.js');
var expect  = require('chai').expect;
var request = require('request');


describe('Average Prices', function() {

    it('calculates average prices per postcode', function() {
        priceService.getAverageAtPostcode('BN1 7JJ').then(avg => {
            expect(avg).to.equal('157661.39')
        });
    });
  });
