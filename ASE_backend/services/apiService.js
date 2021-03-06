/* eslint-disable no-console */
/**
 * File used to interact with APIs
 * Also used to interact with other services
 * Called by apiController
 */
const postcodeService = require("./postcodeService.js");
const priceService = require("./priceService.js");
const dbService = require("./dbService.js");

/**
 * Test function to test server, prints data received
 * @param {string} info
 */
var print_data = function(info) {
    console.log(info);
};

/**
 * Gets the average price of house in a postcode
 * @returns {number} Average price or "No data" if there is no data
 * @param {string} postcode
 */
var getAveragePrice = async function(postcode) {
    console.log(`Getting data for ${postcode} from db`);
    var pcData = await dbService.getPostCode(postcode);
    console.log(`Received data for ${postcode}`);
    if (pcData == undefined) {
        return "No data";
    }
    return pcData.Average_price;
};

/**
 * Gets the postcode data for a point around a certain radius
 * Information is precomputed and stored in a DB, see dbService
 * if radius is above a certain threshold, returns a lower 'resolution'
 * @param {number} lat - latitude
 * @param {number} long - longitude
 * @param {number} radius - radius in kilometers, can be decimal
 * @returns {Array} Array of postcode objects, with each a postcode, latitude and longitude properties
 */
var getPricesAtAround = async function(lat, long, radius) {
    var postcodes = [];
    if (radius < 1 || radius == undefined) {
        postcodes = await dbService.getPostCodes(lat, long, radius);
        postcodes = postcodes.map(postcode => {
            return {
                price: postcode.Average_price,
                postcode: postcode._id,
                latitude: postcode.Latitude + "",
                longitude: postcode.Longitude + ""
            };
        });
    } else if (radius > 90) {
        postcodes = await dbService.getAreas(lat, long, radius);
        postcodes = postcodes.map(postcode => {
            return {
                price: postcode.Average_price,
                postcode: postcode._id,
                latitude: postcode.Latitude + "",
                longitude: postcode.Longitude + ""
            };
        });
    } else if (radius > 40) {
        postcodes = await dbService.getDistricts(lat, long, radius);
        postcodes = postcodes.map(postcode => {
            return {
                price: postcode.Average_price,
                postcode: postcode._id,
                latitude: postcode.Latitude + "",
                longitude: postcode.Longitude + ""
            };
        });
    } else {
        postcodes = await dbService.getSectors(lat, long, radius);
        postcodes = postcodes.map(postcode => {
            return {
                price: postcode.Average_price,
                postcode: postcode._id,
                latitude: postcode.Latitude + "",
                longitude: postcode.Longitude + ""
            };
        });
    }
    return postcodes;
};

/**
 * Retrieves postcodes around a certain point
 * @param {number} lat - latitude
 * @param {number} long - longitude
 * @returns {Array} Array of postcodes and various data
 */
var getPostcodes = async function(lat, long) {
    var postcodes = await postcodeService.getPostcodesNearAround(lat, long);
    return postcodes;
};

/**
 * Retrieves addresses and information of each transaction in a postcode
 * @param {string} postcode
 * @returns {Array} Transaction data of each house
 */
var getPostcodeData = async function(postcode) {
    postcode = postcode.toUpperCase();
    return await priceService.getAllFromPostcode(postcode);
};

module.exports = {
    print_data,
    getAveragePrice,
    getPricesAtAround,
    getPostcodes,
    getPostcodeData
};
