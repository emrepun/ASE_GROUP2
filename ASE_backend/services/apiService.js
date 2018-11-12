//file used to interact with APIs
//also used to interact with other services
const postcodeService = require('./postcodeService.js');
const priceService =  require('./priceService.js');

exports.getPostcodes = async function(lat, long){
  return await postcodeService.getPostcodesNear(lat, long);
}

exports.print_data = function(info) {
  console.log(info);
}

exports.getAveragePrice = async function(postcode){
  return await priceService.getAverageAtPostcode(postcode);
}


exports.getPricesAtAround = async function(lat, long, radius){
  postcodes = await postcodeService.getPostcodesNearAround(lat, long, radius);
  postcodeData = [];
  for (postcode of postcodes) {
    let price = await priceService.getAverageAtPostcode(postcode.Postcode);
    postcodeData.push({
      price,
      postcode: postcode.Postcode,
      latitude: postcode.Latitude,
      longitude: postcode.Longitude
    });
  }

  return postcodeData;
}

exports.getPostcodes = async function(lat, long) {
  postcodes = await postcodeService.getPostcodesNear(lat, long);
  return postcodes;
}

exports.getPostcodeData = async function(postcode){
  postcode = postcode.toUpperCase();
  return await priceService.getAllFromPostcode(postcode);
}
