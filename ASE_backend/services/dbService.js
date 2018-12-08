/**
 * Manages connection with db using mongoose
 */
var connection = require("./mongooseRemote.js").dbRemote;
var { postcodeData_model } = require("../models/postcodeData.js");
var postcodeDataModel = connection.model(
    "postcodeData",
    postcodeData_model.schema
);

var { area } = require("../models/area");
area = connection.model("area", area.schema);
var { district } = require("../models/district");
district = connection.model("district", district.schema);
var { sector } = require("../models/sector");
sector = connection.model("sector", sector.schema);

/**
 * @param lat Latitude
 * @param long Longitude
 * @param radius Radius in km
 * Converts lat and long with radius to a lat/long bracket and searches the db for
 * areas in that range
 */
module.exports.getAreas = async (lat, long, radius) => {
    lat = parseFloat(lat);
    long = parseFloat(long);
    console.log(lat, long, Math.cos(lat));
    var latdiff = radius / 110.574;
    var longdiff = 111.32 * Math.abs(Math.cos(lat));
    longdiff = radius / longdiff;
    console.log(latdiff, longdiff);
    var data = await area.find({
        Latitude: { $gte: lat - latdiff / 2, $lte: lat + latdiff / 2 },
        Longitude: { $gte: long - longdiff / 2, $lte: long + longdiff / 2 }
    });
    return data;
};

/**
 * @param lat Latitude
 * @param long Longitude
 * @param radius Radius in km
 * Converts lat and long with radius to a lat/long bracket and searches the db for
 * districts in that range
 */
exports.getDistricts = async (lat, long, radius) => {
    lat = parseFloat(lat);
    long = parseFloat(long);
    var latdiff = radius / 110.574;
    var longdiff = 111.32 * Math.abs(Math.cos(lat));
    longdiff = radius / longdiff;
    var data = await district.find({
        Latitude: { $gte: lat - latdiff / 2, $lte: lat + latdiff / 2 },
        Longitude: { $gte: long - longdiff / 2, $lte: long + longdiff / 2 }
    });
    return data;
};

/**
 * @param lat Latitude
 * @param long Longitude
 * @param radius Radius in km
 * Converts lat and long with radius to a lat/long bracket and searches the db for
 * sectors in that range
 */
exports.getSectors = async (lat, long, radius) => {
    radius *= 1.5;
    lat = parseFloat(lat);
    long = parseFloat(long);
    var latdiff = radius / 110.574;
    var longdiff = 111.32 * Math.abs(Math.cos(lat));
    longdiff = radius / longdiff;
    var data = await sector.find({
        Latitude: { $gte: lat - latdiff / 2, $lte: lat + latdiff / 2 },
        Longitude: { $gte: long - longdiff / 2, $lte: long + longdiff / 2 }
    });
    return data;
};

/**
 * @param lat Latitude
 * @param long Longitude
 * @param radius Radius in km
 * Converts lat and long with radius to a lat/long bracket and searches the db for
 * postcodes in that range
 */
exports.getPostCodes = async (lat, long, radius) => {
    radius *= 1.5
    lat = parseFloat(lat);
    long = parseFloat(long);
    var latdiff = radius / 110.574;
    var longdiff = 111.32 * Math.abs(Math.cos(lat));
    longdiff = radius / longdiff;
    var req = {
        Latitude: { $gte: lat - latdiff / 2, $lte: lat + latdiff / 2 },
        Longitude: { $gte: long - longdiff / 2, $lte: long + longdiff / 2 }
    };
    var data = await postcodeDataModel.find(req);
    return data;
};

/**
 * @param {string} postcode
 * @returns postcode object and associated data
 */
exports.getPostCode = async postcode => {
    postcode = postcode.toUpperCase();
    var data = await postcodeDataModel.find({ _id: postcode });
    return data[0];
};
