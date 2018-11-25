const express = require("express");
var router = express.Router();

var apiController = require("../controllers/apiController");

router.post("/", apiController.print_data);

router.get("/pcprices/:lat/:long", apiController.getPricesAtAround);

router.get("/pcprices/:lat/:long/:radius", apiController.getPricesAtAround);

router.get("/average/:postcode", apiController.getAveragePrice);

router.get("/addresses/:postcode", apiController.getPostcodeData);

router.get("/postcodes/:lat/:long", apiController.getPostcodes);

module.exports = router;
