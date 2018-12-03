//const controllers =  require('../routes/apiRoutes.js');
//const http_mocks = require('node-mocks-http');
//const should = require('should');

//function buildResponse() {
//  return http_mocks.createResponse({eventEmitter: require('events').EventEmitter})
//}

//describe('Bad request tests', function(){

//  it('Get prices around postcode', function(done){
//    var response = buildResponse()
//    var request  = http_mocks.createRequest({
//       method: 'GET',
    //   url: '/pcprices',
  //  });
//    response.on('end', function() {
//      console.log(response)
//      response.statusCode.should.equal(200);
//      done()
//     });

//    controllers.handle(request, response)
//  });
//})
