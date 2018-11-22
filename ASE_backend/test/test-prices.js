
const priceService =  require('../services/priceService.js');
var nock = require('nock');
var expect  = require('chai').expect;
var request = require('request');


describe('Average Prices', function() {
  beforeEach(() => {
  nock('http://landregistry.data.gov.uk/')
  .get('data/ppi/transaction-record.json')
  .reply(200,{
    "format": "linked-data-api",
    "version": "0.2",
    "result": {
      "items": [
        {
          "pricePaid":69,
          "propertyAddress":"here and there",
          "transactionDate":"about now"
        },
        {
          "pricePaid":100,
          "propertyAddress":"here and there",
          "transactionDate":"about now"
        }]}
      });
        });
    it('calculates average prices per postcode', function(done) {
      // Increase the default timeout for this test
      // If the test takes longer than this, it will fail

      return priceService.getAverageAtPostcode('BN1 7JJ')
      .then(response => {
      expect(avg==84.5);
    });
  });
});
