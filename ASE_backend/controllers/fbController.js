var fbService = require('../services/fbService');

exports.print_data = function(req, res) {
  fbService.print_data(req.body);
  res.redirect(200, '/');
}
