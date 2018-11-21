// Put storing logic here with Mongoose etc...
var connection = require("./mongooseRemote.js").dbRemote;
var { postcodeData_model } = require("../models/postcodeData.js");
var postcodeDataModel = connection.model(
    "postcodeData",
    postcodeData_model.schema
);

// ===========================================================================
// People

exports.getPostCode = async postcode => {
    postcode = postcode.toUpperCase();
    var data = await postcodeDataModel.find({ _id: postcode });
    return data[0];
};
