const express = require("express");
var router = express.Router();

var apiController = require("../controllers/apiController");

router.post("/", apiController.print_data);

router.get("/pcprices/:lat/:long", apiController.getPricesAt);

module.exports = router;
