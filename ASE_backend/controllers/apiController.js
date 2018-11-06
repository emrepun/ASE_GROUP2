var apiService = require('../services/apiService');

exports.print_data = function(req, res) {
  apiService.print_data(req.body);
  res.redirect(200, '/');
}
