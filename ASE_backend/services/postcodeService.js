const request = require("request");
const csv = require("csvtojson");

//returns list of postcodes in order of distance

module.exports.getPostcodesNear = async (lat, lng) => {
  var all = [];
  all = await csv()
    .fromStream(
      request.get(
        `https://www.doogal.co.uk/GetPostcodesNear.ashx?lat=${lat}&lng=${lng}&output=csv`
      )
    )
    .subscribe(
      async json => {
        all.push(json);
      },
      err => {
        console.log("Error getting data from doogal:", err);
      },
      () => {
        console.log("Received Postcode data");
      }
    );
  all.sort((a, b) => parseFloat(a.DistanceKMs) - parseFloat(b.DistanceKMs));
  return all.map(datum => datum.Postcode);
};
