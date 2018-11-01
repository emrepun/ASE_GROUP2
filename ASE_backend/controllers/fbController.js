var fbService = require('../services/fbService');

var db = fbService.database();

var get_data = async () => {
  var ref = await db.ref("/");
  var data = await ref.once("value");
  console.log(data.val());
}
