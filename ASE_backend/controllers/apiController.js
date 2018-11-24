/**
 * Abstracts the http request away from the function itself
 * Simply passes the data to the appriate function
 * Returns the appropriate HTTP response
 * Is called by apiRoutes
 * See apiRoutes for what each method actually does
 */

var apiService = require("../services/apiService");

exports.print_data = function(req, res) {
    apiService.print_data(req.body);
    res.redirect(200, "/");
};

/**
 * Returns 400 if arguments are missing
 * Returns 500 if there was an error processing the request
 * @param {Express request} req
 * @param {Express response} res
 */
exports.getPricesAtAround = async function(req, res) {
    if (req.params.lat == undefined || req.params.long == undefined) {
        res.status(400);
        res.send("Lat or long arguments missing");
    } else {
        try {
            var prices = await apiService.getPricesAtAround(
                req.params.lat,
                req.params.long,
                req.params.radius
            );
            res.send(JSON.stringify(prices, null, 2));
        } catch (err) {
            // eslint-disable-next-line no-console
            console.log(err);
            res.status(500);
            res.send("Error getting prices");
        }
    }
};

/**
 * Returns 400 if arguments are missing
 * Returns 500 if there was an error processing the request
 * @param {Express request} req
 * @param {Express response} res
 */
exports.getPostcodeData = async function(req, res) {
    if (req.params.postcode == undefined) {
        res.status(400);
        res.send("Postcode argument missing");
    } else {
        try {
            var data = await apiService.getPostcodeData(req.params.postcode);
            res.send(JSON.stringify(data, null, 2));
        } catch (err) {
            // eslint-disable-next-line no-console
            console.log(err);
            res.status(500);
            res.send("Error getting addresses");
        }
    }
};

/**
 * Returns 400 if arguments are missing
 * Returns 500 if there was an error processing the request
 * @param {Express request} req
 * @param {Express response} res
 */
exports.getAveragePrice = async function(req, res) {
    if (req.params.postcode == undefined) {
        res.status(400);
        res.send("Postcode argument missing");
    } else {
        try {
            var data = await apiService.getAveragePrice(req.params.postcode);
            res.send(JSON.stringify(data, null, 2));
        } catch (err) {
            // eslint-disable-next-line no-console
            console.log(err);
            res.status(500);
            res.send("Error getting average");
        }
    }
};

/**
 * Returns 400 if arguments are missing
 * Returns 500 if there was an error processing the request
 * @param {Express request} req
 * @param {Express response} res
 */
exports.getPostcodes = async function(req, res) {
    if (req.params.lat == undefined || req.params.long == undefined) {
        res.status(400);
        res.send("Lat or long arguments missing");
    } else {
        try {
            var data = await apiService.getPostcodes(
                req.params.lat,
                req.params.long
            );
            res.send(JSON.stringify(data, null, 2));
        } catch (err) {
            // eslint-disable-next-line no-console
            console.log(err);
            res.status(500);
            res.send("Error getting postcodes");
        }
    }
};
