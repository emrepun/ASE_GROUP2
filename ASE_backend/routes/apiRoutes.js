/**
 * This file contains all the routes for the /api/ endpoint
 * Talks to the apiController
 */

const express = require("express");
var router = express.Router();

var apiController = require("../controllers/apiController");

/** Testing endpoint, prints input to console*/
router.post("/", apiController.print_data);

/** Endpoint to receive postcodes and their averages around a point*/
router.get("/pcprices/:lat/:long", apiController.getPricesAtAround);

/** Endpoint to receive postcodes and their averages around a point, given a radius*/
router.get("/pcprices/:lat/:long/:radius", apiController.getPricesAtAround);

/** Endpoint to receive the average price of a postcode*/
router.get("/average/:postcode", apiController.getAveragePrice);

/** Endpoint to receive the transaction data of a postcode*/
router.get("/addresses/:postcode", apiController.getPostcodeData);

/** Endpoint to receive the postcodes (and nothing else) at a geo coordinate*/
router.get("/postcodes/:lat/:long", apiController.getPostcodes);

module.exports = router;
