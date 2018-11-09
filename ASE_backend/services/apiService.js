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

exports.getPricesAt = async function(lat, long){
  postcodes = await postcodeService.getPostcodesNear(lat, long);
  postcodeData = await priceService.getAllFromPostcode(postcodes[0]);
  console.log(postcodeData)
  return postcodeData;
}
