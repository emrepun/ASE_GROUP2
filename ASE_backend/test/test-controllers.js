const chai = require("chai");
const expect = chai.expect;
const sinon = require("sinon");
const controller = require("../controllers/apiController.js");
const apiService = require("../services/apiService.js");

var {dbRemote} = require('../services/mongooseRemote.js');
dbRemote.close();

describe("Get Prices At Around Controller", function(done){
  it("should return postcode details", function(done){
      var post = sinon.stub(apiService, 'getPricesAtAround');
      post.returns({lat:"3442", long:"343432", radius:"1"});
      let res = {
      send: sinon.spy()
    }
      var req = {
        params: {lat:"3442", long:"343432", radius:"1"}
      }
      controller.getPricesAtAround(req, res).then(details => {
        expect(res.send.calledOnce).to.be.true;
        var output = res.send.firstCall.args[0];
        expect(output).to.be.a('string');
        var output = JSON.parse(output);

        expect(output.lat).to.equal("3442");
        expect(output.long).to.equal("343432");
        post.restore();
        done();
      });
  });

  it("should return a 400 error", function(done){
      let res = {
        send: sinon.spy(),
        status: sinon.spy()
      }
      var req = {
        params: {lat:undefined, long:undefined, radius:undefined}
      }
      controller.getPricesAtAround(req, res).then(details => {
        expect(res.status.firstCall.args[0]).to.equal(400);
        expect(res.send.calledOnce).to.be.true;
        expect(res.send.firstCall.args[0]).to.be.a('string');
        expect(res.send.firstCall.args[0]).to.equal("Lat or long arguments missing");
        done();
      });
   });

   it("should return a 500 error", function(done){
     var post = sinon.stub(apiService, 'getPricesAtAround').throws(new TypeError());

     let res = {
     send: sinon.spy(),
     status: sinon.spy()
   };
     var req = {
     params: {lat:"3442", long:"343432", radius:"1"}
   };

     controller.getPricesAtAround(req, res).then(details => {
       expect(res.status.firstCall.args[0]).to.equal(500);
       expect(res.send.calledOnce).to.be.true;
       expect(res.send.firstCall.args[0]).to.be.a('string');
       expect(res.send.firstCall.args[0]).to.equal("Error getting prices");
       done();
     });
   });
});

describe("Get Postcode Data Controller", function(done){
  it("should return postcode data", function(done){
    var post = sinon.stub(apiService, 'getPostcodeData');
    post.returns({lat:"3442", long:"343432", id:"BN1 7JJ"});

    let res = {
    send: sinon.spy()
  }
    var req = {
      params:{postcode:"BN1 7JJ"}
    }

    controller.getPostcodeData(req, res).then(details => {
      expect(res.send.calledOnce).to.be.true;
      var output = res.send.firstCall.args[0];
      expect(output).to.be.a('string');
      var output = JSON.parse(output);
      expect(output.id).to.equal("BN1 7JJ");
      expect(output.lat).to.equal("3442");
      expect(output.long).to.equal("343432");
      post.restore();
      done();
    });
  });
  it("should return a 400 error", function(done){
    let res = {
      send: sinon.spy(),
      status: sinon.spy()
    }
    var req = {
      params: {postcode:undefined}
    }
    controller.getPostcodeData(req, res).then(details => {
      expect(res.status.firstCall.args[0]).to.equal(400);
      expect(res.send.calledOnce).to.be.true;
      expect(res.send.firstCall.args[0]).to.be.a('string');
      expect(res.send.firstCall.args[0]).to.equal("Postcode argument missing");
      done();
    });
  });

  it("should return a 500 error", function(done){
    var post = sinon.stub(apiService, 'getPostcodeData').throws(new TypeError());

    let res = {
    send: sinon.spy(),
    status: sinon.spy()
  };
    var req = {
    params: {postcode:"BN1 7JJ"}
  };

    controller.getPostcodeData(req, res).then(details => {
      expect(res.status.firstCall.args[0]).to.equal(500);
      expect(res.send.calledOnce).to.be.true;
      expect(res.send.firstCall.args[0]).to.be.a('string');
      expect(res.send.firstCall.args[0]).to.equal("Error getting addresses");
      done();
    });
  });
});

describe("Get Average Price Controller", function(done){
  it("should return average price data", function(done){
    var post = sinon.stub(apiService, 'getAveragePrice');
    post.returns({average:"3434000", postcode:"BN1 7JJ"});

    let res = {
    send: sinon.spy()
  }
    var req = {
      params:{postcode:"BN1 7JJ"}
    }

    controller.getAveragePrice(req, res).then(details => {
      expect(res.send.calledOnce).to.be.true;
      var output = res.send.firstCall.args[0];
      expect(output).to.be.a('string');
      var output = JSON.parse(output);
      expect(output.postcode).to.equal("BN1 7JJ");
      expect(output.average).to.equal("3434000");
      post.restore();
      done();
    });
  });
  it("should return a 400 error", function(done){
    let res = {
      send: sinon.spy(),
      status: sinon.spy()
    }
    var req = {
      params: {postcode:undefined}
    }
    controller.getAveragePrice(req, res).then(details => {
      expect(res.status.firstCall.args[0]).to.equal(400);
      expect(res.send.calledOnce).to.be.true;
      expect(res.send.firstCall.args[0]).to.be.a('string');
      expect(res.send.firstCall.args[0]).to.equal("Postcode argument missing");
      done();
    });
  });
  it("should return a 500 error", function(done){
    var post = sinon.stub(apiService, 'getAveragePrice').throws(new TypeError());

    let res = {
    send: sinon.spy(),
    status: sinon.spy()
  };
    var req = {
    params: {postcode:"BN1 7JJ"}
  };

    controller.getAveragePrice(req, res).then(details => {
      expect(res.status.firstCall.args[0]).to.equal(500);
      expect(res.send.calledOnce).to.be.true;
      expect(res.send.firstCall.args[0]).to.be.a('string');
      expect(res.send.firstCall.args[0]).to.equal("Error getting average");
      done();
    });
  });
});

describe("Get Postcodes Controller", function(done){
  it("should return postcodes", function(done){
    var post = sinon.stub(apiService, 'getPostcodes');
    post.returns([{lat:"3442", long:"343432", postcode: "BN1 7JJ"},{lat:"3442", long:"343432", postcode: "BN1 7AJ"}]);

    let res = {
    send: sinon.spy()
    }
    var req = {
      params:{lat:"3442", long:"343432"}
    }
    controller.getPostcodes(req, res).then(details => {
      expect(res.send.calledOnce).to.be.true;
      var output = res.send.firstCall.args[0];
      expect(output).to.be.a('string');
      var output = JSON.parse(output);
      post.restore();
      done();
    });
  });
  it("should return a 400 error", function(done){
    let res = {
      send: sinon.spy(),
      status: sinon.spy()
    }
    var req = {
      params: {lat:undefined, long:undefined}
    }
    controller.getPostcodes(req, res).then(details => {
      expect(res.status.firstCall.args[0]).to.equal(400);
      expect(res.send.calledOnce).to.be.true;
      expect(res.send.firstCall.args[0]).to.be.a('string');
      expect(res.send.firstCall.args[0]).to.equal("Lat or long arguments missing");
      done();
    });
  });
  it("should return a 500 error", function(done){
    var post = sinon.stub(apiService, 'getPostcodes').throws(new TypeError());

    let res = {
    send: sinon.spy(),
    status: sinon.spy()
  };
    var req = {
      params:{lat:"3442", long:"343432"}
  };

    controller.getPostcodes(req, res).then(details => {
      expect(res.status.firstCall.args[0]).to.equal(500);
      expect(res.send.calledOnce).to.be.true;
      expect(res.send.firstCall.args[0]).to.be.a('string');
      expect(res.send.firstCall.args[0]).to.equal("Error getting postcodes");
      done();
    });
  });

})
