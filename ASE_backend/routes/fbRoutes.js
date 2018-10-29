const express = require("express");
var router = express.Router();

var fbController = require("../controllers/fbController");

router.post('/', fbController.print_data);

module.exports = router;
