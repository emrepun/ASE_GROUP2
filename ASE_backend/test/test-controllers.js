const controllers =  require('../controllers/apiController.js');
const http_mocks = require('node-mocks-http');
const should = require('should');


describe('Bad request tests', function(){

  it('Get prices around postcode', function(){
    var response = buildResponse()
    var request  = http_mocks.createRequest({
       method: 'GET',
       url: '/pcprices/:lat/:long',
    });
    response.on('end', function() {
      response._getData().should.equal('yes');
       done()
     });

    controllers.getPricesAtAround.handle(request, response)
  });
})
