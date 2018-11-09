var apiService = require('../services/apiService');

exports.print_data = function(req, res) {
  apiService.print_data(req.body);
  res.redirect(200, '/');
}

exports.getPricesAt = async function(req, res) {
  prices = await apiService.getPricesAt(req.params.lat, req.params.long);
  res.send(JSON.stringify(prices, null, 2));
}
