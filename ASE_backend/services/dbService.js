/**
 * Manages connection with db using mongoose
 */
var connection = require("./mongooseRemote.js").dbRemote;
var { postcodeData_model } = require("../models/postcodeData.js");
var postcodeDataModel = connection.model(
    "postcodeData",
    postcodeData_model.schema
);

/**
 * @param {string} postcode
 * @returns postcode object and associated data
 */
exports.getPostCode = async postcode => {
    postcode = postcode.toUpperCase();
    var data = await postcodeDataModel.find({ _id: postcode });
    return data[0];
};
