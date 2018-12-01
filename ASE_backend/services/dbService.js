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

exports.getAreas = async (lat, long, radius) => {
    lat = parseFloat(lat);
    long = parseFloat(long);
    var latdiff = radius / 110.574;
    var longdiff = 111.32 * Math.cos(lat);
    longdiff = radius / longdiff;
    var data = await area.find({
        Latitude: { $gte: lat - latdiff / 2, $lte: lat + latdiff / 2 },
        Longitude: { $gte: long - longdiff / 2, $lte: long + longdiff / 2 }
    });
    return data;
};

exports.getDistricts = async (lat, long, radius) => {
    lat = parseFloat(lat);
    long = parseFloat(long);
    var latdiff = radius / 110.574;
    var longdiff = 111.32 * Math.cos(lat);
    longdiff = radius / longdiff;
    var data = await district.find({
        Latitude: { $gte: lat - latdiff / 2, $lte: lat + latdiff / 2 },
        Longitude: { $gte: long - longdiff / 2, $lte: long + longdiff / 2 }
    });
    return data;
};

exports.getSectors = async (lat, long, radius) => {
    lat = parseFloat(lat);
    long = parseFloat(long);
    var latdiff = radius / 110.574;
    var longdiff = 111.32 * Math.cos(lat);
    longdiff = radius / longdiff;
    var data = await sector.find({
        Latitude: { $gte: lat - latdiff / 2, $lte: lat + latdiff / 2 },
        Longitude: { $gte: long - longdiff / 2, $lte: long + longdiff / 2 }
    });
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
