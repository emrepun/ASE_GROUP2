const express = require("express");
var router = express.Router();

var apiController = require("../controllers/apiController");

router.post('/', apiController.print_data);

module.exports = router;
