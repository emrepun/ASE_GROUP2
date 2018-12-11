const chai = require("chai");
const expect = chai.expect;
const sinon = require("sinon");
const apiService = require("../services/apiService.js");
const dbService = require("../services/dbService.js");

describe("Get Average Price Service", function(done){
  it("Should return average price for BN1 7JJ", function(done){
    var postcode = "BN1 7JJ";
    var post = sinon.stub(dbService, 'getPostCode');
    post.returns({Average_price:"343432"});
    apiService.getAveragePrice(postcode).then(details =>{
      expect(details).to.equal('343432');
      post.restore();
      done();
    });
  });
  it("Should return undefined", function(done){
    var postcode = "BN1 7JJ";
    var post = sinon.stub(dbService, 'getPostCode');
    post.returns(undefined);
    apiService.getAveragePrice(postcode).then(details =>{
      expect(details).to.equal("No data");
      post.restore();
      done();
    });
  });
});

describe("Get Prices At Around Service", function(done){
  it("Should return price data for radius<1", function(done){
    var lat = '3434';
    var long = '211';
    var radius = 0;

    var post = sinon.stub(dbService, 'getPostCodes');
    var payload = [{_id:"BN1 7JJ", Latitude:"3434", Longitude:"211", Average_price:"3348800"},{_id:"BN1 7AJ", Latitude:"3434", Longitude:"211", Average_price:"5348800"}];
    post.returns(payload);

    apiService.getPricesAtAround(lat,long,radius).then(details =>{
      var first = details[0];
      var second = details[1];
      expect(first.price).to.equal("3348800");
      expect(second.price).to.equal("5348800");
      expect(first.latitude).to.equal("3434");
      expect(second.latitude).to.equal("3434");
      expect(first.longitude).to.equal("211");
      expect(second.longitude).to.equal("211");
      expect(first.postcode).to.equal("BN1 7JJ");
      expect(second.postcode).to.equal("BN1 7AJ");

      post.restore();
      done();
    });
  });
  it("Should return price data for radius > 90 (area)", function(done){
    var lat = '3434';
    var long = '211';
    var radius = 91;

    var post = sinon.stub(dbService, 'getAreas');
    var payload = [{_id:"BN1 7JJ", Latitude:"3434", Longitude:"211", Average_price:"3348800"},{_id:"BN1 7AJ", Latitude:"3434", Longitude:"211", Average_price:"5348800"}];
    post.returns(payload);
    apiService.getPricesAtAround(lat,long,radius).then(details =>{
      var first = details[0];
      var second = details[1];
      expect(first.price).to.equal("3348800");
      expect(second.price).to.equal("5348800");
      expect(first.latitude).to.equal("3434");
      expect(second.latitude).to.equal("3434");
      expect(first.longitude).to.equal("211");
      expect(second.longitude).to.equal("211");
      expect(first.postcode).to.equal("BN1 7JJ");
      expect(second.postcode).to.equal("BN1 7AJ");
      post.restore();
      done();
    });
  });
  it("Should return price data for radius > 40 and <90 (districts)", function(done){
    var lat = '3434';
    var long = '211';
    var radius = 41;

    var post = sinon.stub(dbService, 'getDistricts');
    var payload = [{_id:"BN1 7JJ", Latitude:"3434", Longitude:"211", Average_price:"3348800"},{_id:"BN1 7AJ", Latitude:"3434", Longitude:"211", Average_price:"5348800"}];
    post.returns(payload);
    apiService.getPricesAtAround(lat,long,radius).then(details =>{
      var first = details[0];
      var second = details[1];
      expect(first.price).to.equal("3348800");
      expect(second.price).to.equal("5348800");
      expect(first.latitude).to.equal("3434");
      expect(second.latitude).to.equal("3434");
      expect(first.longitude).to.equal("211");
      expect(second.longitude).to.equal("211");
      expect(first.postcode).to.equal("BN1 7JJ");
      expect(second.postcode).to.equal("BN1 7AJ");
      post.restore();
      done();
    });
  });
  it("Should return price data for radius < 40 and >1 (sectors)", function(done){
    var lat = '3434';
    var long = '211';
    var radius = 21;

    var post = sinon.stub(dbService, 'getSectors');
    var payload = [{_id:"BN1 7JJ", Latitude:"3434", Longitude:"211", Average_price:"3348800"},{_id:"BN1 7AJ", Latitude:"3434", Longitude:"211", Average_price:"5348800"}];
    post.returns(payload);
    apiService.getPricesAtAround(lat,long,radius).then(details =>{
      var first = details[0];
      var second = details[1];
      expect(first.price).to.equal("3348800");
      expect(second.price).to.equal("5348800");
      expect(first.latitude).to.equal("3434");
      expect(second.latitude).to.equal("3434");
      expect(first.longitude).to.equal("211");
      expect(second.longitude).to.equal("211");
      expect(first.postcode).to.equal("BN1 7JJ");
      expect(second.postcode).to.equal("BN1 7AJ");
      post.restore();
      done();
    });
  });
})
