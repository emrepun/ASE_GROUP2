//file used to interact with APIs
//also used to interact with other services
const postcodeService = require("./postcodeService.js");
const priceService = require("./priceService.js");
const dbService = require("./dbService.js");

exports.getPostcodes = async function(lat, long) {
    return await postcodeService.getPostcodesNear(lat, long);
};

exports.print_data = function(info) {
    console.log(info);
};

exports.getAveragePrice = async function(postcode) {
    console.log(`getting data for ${postcode} from db`);
    var pcData = await dbService.getPostCode(postcode);
    console.log(`Received data for ${postcode}`);
    if (pcData == undefined) {
        return "No data";
    }
    return pcData.Average_price;
};

exports.getPricesAtAround = async function(lat, long, radius) {
    var postcodes = await postcodeService.getPostcodesNearAround(
        lat,
        long,
        radius
    );
    var postcodeData = [];
    var asyncPrices = postcodes.map(postcode => exports.getAveragePrice(postcode.Postcode));
    var prices = await Promise.all(asyncPrices);
    postcodes = postcodes.map((postcode, i) => {
      return {
        price: prices[i],
        postcode: postcode.Postcode,
        latitude: postcode.Latitude,
        longitude: postcode.Longitude
      }
    })
    return postcodes
};

exports.getPostcodes = async function(lat, long) {
    postcodes = await postcodeService.getPostcodesNearAround(lat, long);
    return postcodes;
};

exports.getPostcodeData = async function(postcode) {
    postcode = postcode.toUpperCase();
    return await priceService.getAllFromPostcode(postcode);
};
