//MongoDB Person schema file, used to access postcodeDatas

const mongoose = require("mongoose");
require('mongoose-double')(mongoose);

var SchemaTypes = mongoose.Schema.Types;

var postcode = mongoose.model("postcodeData", {
  _id: { type: String, trim: true },
  Latitude: { type: SchemaTypes.Double },
  Longitude: { type: SchemaTypes.Double },
  Average_price: { type: SchemaTypes.Double }
});

module.exports = { postcodeData_model: postcode };