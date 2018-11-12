var request = require("request-promise-native");

//return array of data from the land registry, more specifically in this format:
// { pricePaid: 285000,
//   propertyAddress:
//    { _about:
//       'http://landregistry.data.gov.uk/data/ppi/address/e59be9d9ba14f5828b07fb939df7008044adaba8',
//      county: 'BRIGHTON AND HOVE',
//      district: 'BRIGHTON AND HOVE',
//      paon: '9A',
//      postcode: 'BN2 1EL',
//      street: 'BELGRAVE PLACE',
//      town: 'BRIGHTON',
//      type: [Array] },
//   transactionDate: 'Wed, 08 May 2013'
// }


module.exports.getAllFromPostcode = async postcode => {
  postcode = postcode.toUpperCase();
  var options = {
    method: "GET",
    url: "http://landregistry.data.gov.uk/data/ppi/transaction-record.json",
    qs: { _page: "0", "propertyAddress.postcode": `${postcode}` }
  };

  var props = [];

// www.royalmail.com/portal/rm/content1?catId=400044&mediaId=9200078#3400054
// max 80 houses per postcode
  for (var i = 0; i < 50; i++){
    let response = await request(options);
    options.qs._page = i;
    let items = JSON.parse(response).result.items;
    if (items.length == 0) {
      break;
    }
    for (item of items) {
      pricePaid = item.pricePaid;
      propertyAddress = item.propertyAddress;
      transactionDate = item.transactionDate;
      newItem = { pricePaid, propertyAddress, transactionDate };
      props.push(newItem);
    }
  }

  console.log(`Received price data for ${postcode}`);

  return props;
};


module.exports.getAverageAtPostcode = async postcode => {
  console.log(`Getting prices at ${postcode}`)
  var data = await module.exports.getAllFromPostcode(postcode);
  var allPrices = []
  for(datum of data) {
    allPrices.push(datum.pricePaid);
  }
  var sum = 0;
  for (price of allPrices) {
    sum += price;
  }
  var avg = sum/allPrices.length;
  return(parseFloat(Math.round(avg * 100) / 100).toFixed(2));
}
