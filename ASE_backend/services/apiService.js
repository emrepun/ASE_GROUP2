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
    console.log(pcData);
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
    for (let postcode of postcodes) {
        //let price = await priceService.getAverageAtPostcode(postcode.Postcode);
        let price = await exports.getAveragePrice(postcode.Postcode);
        postcodeData.push({
            price,
            postcode: postcode.Postcode,
            latitude: postcode.Latitude,
            longitude: postcode.Longitude
        });
    }

    return postcodeData;
};

exports.getPostcodes = async function(lat, long) {
    postcodes = await postcodeService.getPostcodesNearAround(lat, long);
    return postcodes;
};

exports.getPostcodeData = async function(postcode) {
    postcode = postcode.toUpperCase();
    return await priceService.getAllFromPostcode(postcode);
};
