const chai = require("chai");
const expect = chai.expect;
const sinon = require("sinon");
const controller = require("../controllers/apiController.js");
const apiService = require("../services/apiService.js")


describe("API Controller", function(){
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

   })
})
