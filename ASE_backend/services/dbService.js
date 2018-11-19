// Put storing logic here with Mongoose etc...
var { mongoose } = require("./mongoose.js");
var { postcode_model } = require("../../models/postcode.js");

// ===========================================================================
// People

exports.storePostcode = async function(data) {
  // wrap data into mongoose model object
  var postcode = new postcode_model(data);

  try {
    var postcode_id = await postcode.save();
    console.log(`postcode stored as _id: ${postcode_id._id}`);
    return postcode_id._id;
  } catch (err) {
    console.log("Unable to insert postcode:");
    console.log(err);
  }
};
