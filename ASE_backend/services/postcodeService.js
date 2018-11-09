const request = require("request");
const csv = require("csvtojson");

//returns list of postcodes in order of distance

module.exports.getPostcodesNearAround = async (lat, lng, radius) => {
  var all = [];
  var url = "";
  if (radius) {
    url = `https://www.doogal.co.uk/GetPostcodesNear.ashx?lat=${lat}&lng=${lng}&distance=${radius}&output=csv`;
  } else {
    url = `https://www.doogal.co.uk/GetPostcodesNear.ashx?lat=${lat}&lng=${lng}&output=csv`;
  }
  all = await csv()
    .fromStream(request.get(url))
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
  all = all.map(item => {
    return {
      Postcode: item.Postcode,
      Latitude: item.Latitude,
      Longitude: item.Longitude
    };
  });
  return all;
};
