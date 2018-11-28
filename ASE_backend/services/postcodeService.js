const request = require("request-promise-native");
const csv = require("csvtojson");

/**
 * Get geographical data of a postcode from doogal.co.uk
 * @param {string} postcode
 * @returns {Object} json of data regarding the postcode
 */
var getPostcodeDetails = async postcode => {
    postcode = postcode.toUpperCase();
    var options = {
        method: "GET",
        url: "https://www.doogal.co.uk/GetPostcode.ashx",
        qs: { postcode }
    };
    var res = await request(options);
    res = res.toString().split("\t");
    var data = {
        _id: res[0],
        Latitude: res[1],
        Longitude: res[2]
    };
    return data;
};

/**
 * @param {number} lat - latitude
 * @param {number} lng - longitude
 * @param {number} radius - radius in km, can be decimal
 * @returns {Array} Array of postcodes and their geocoordinates
 */
var getPostcodesNearAround = async (lat, lng, radius) => {
    var all = [];
    var url = "";
    if (radius) {
        url = `https://www.doogal.co.uk/GetPostcodesNear.ashx?lat=${lat}&lng=${lng}&distance=${radius}&output=csv`;
    } else {
        url = `https://www.doogal.co.uk/GetPostcodesNear.ashx?lat=${lat}&lng=${lng}&output=csv`;
    }
    all = await csv()
        .fromStream(request.get(url))
        .subscribe(
            async json => {
                all.push(json);
            },
            err => {
                console.log("Error getting data from doogal:", err);
            },
            () => {
                console.log("Received Postcode data");
            }
        );
    all.sort((a, b) => parseFloat(a.DistanceKMs) - parseFloat(b.DistanceKMs));
    all = all.map(item => {
        return {
            Postcode: item.Postcode,
            Latitude: item.Latitude,
            Longitude: item.Longitude
        };
    });
    return all;
};

module.exports = {
    getPostcodeDetails,
    getPostcodesNearAround
};
