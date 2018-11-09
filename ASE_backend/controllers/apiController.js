var apiService = require('../services/apiService');

exports.print_data = function(req, res) {
  apiService.print_data(req.body);
  res.redirect(200, '/');
}

exports.getPricesAtAround = async function(req, res) {
  prices = await apiService.getPricesAtAround(req.params.lat, req.params.long, req.params.radius);
  res.send(JSON.stringify(prices, null, 2));
}

exports.getPostcodeData = async function(req, res) {
  data = await apiService.getPostcodeData(req.params.postcode);
  res.send(JSON.stringify(data, null, 2));
}

exports.getPostcodes = async function(req, res) {
  data = await apiService.getPostcodes(req.params.lat, req.params.long);
  res.send(JSON.stringify(data, null, 2));
}
