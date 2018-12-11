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
        var output = res.send.firstCall.args[0];
        expect(output).to.be.a('string');
        var output = JSON.parse(output);
        expect(output.lat).to.equal("3442");
        expect(output.long).to.equal("343432");
        post.restore();
        done();
      });
  })

})
