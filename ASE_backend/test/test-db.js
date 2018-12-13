const chai = require("chai");
const expect = chai.expect;
const sinon = require("sinon");
const dbService = require("../services/dbService.js");
var connection = require("../services/mongooseRemote.js").dbRemote;

var { postcodeData_model } = require("../models/postcodeData.js");
var postcodeDataModel = connection.model(
    "postcodeData",
    postcodeData_model.schema
);
var { area } = require("../models/area");
area = connection.model("area", area.schema);
var { district } = require("../models/district");
district = connection.model("district", district.schema);
var { sector } = require("../models/sector");
sector = connection.model("sector", sector.schema);

describe("Postcode model tests", function(done){
  it("should return postcodes", function(done){
    var PostcodeMock = sinon.mock(postcodeDataModel);
    var expectedResult = {postcodes:[{},{}]};
    PostcodeMock.expects('find').yields(null, expectedResult);
    postcodeDataModel.find(function (err, result){
      PostcodeMock.verify();
      PostcodeMock.restore();
      expect(result).to.equal(expectedResult);
      done();
    });
  });
});

describe("Area model tests", function(done){
  it("should return postcodes within an area", function(done){
  var AreaMock = sinon.mock(area);
  var expectedResult = {postcodes:[{
    Latitude:"3442",
    Longitude:"343432",
    _id:"BN1 7OJ",
    Average_price:"4000"
  },{
    Latitude:"3442",
    Longitude:"343432",
     _id:"BN1 7BJ",
     Average_price:"489844000"
   },{
     Latitude:"3442",
     Longitude:"341432",
      _id:"BN1 7AJ",
      Average_price:"13244000"
    }]};
  AreaMock.expects('find').yields(null, expectedResult);
  area.find(function (err, result){
    AreaMock.verify();
    AreaMock.restore();
    expect(result).to.equal(expectedResult);
    done();
   });
  });
});

describe("District model tests", function(done){
  it("should return postcodes within a district", function(done){
  var DistrictMock = sinon.mock(district);
  var expectedResult = {postcodes:[{
    Latitude:"3442",
    Longitude:"343432",
    _id:"BN1 7OJ",
    Average_price:"4000"
  },{
    Latitude:"3442",
    Longitude:"343432",
     _id:"BN1 7BJ",
     Average_price:"489844000"
   },{
     Latitude:"3442",
     Longitude:"341432",
      _id:"BN1 7AJ",
      Average_price:"13244000"
    }]};
  DistrictMock.expects('find').yields(null, expectedResult);
  district.find(function (err, result){
    DistrictMock.verify();
    DistrictMock.restore();
    expect(result).to.equal(expectedResult);
    done();
   });
  });
});

describe("Sector model tests", function(done){
  it("should return postcodes within a sector", function(done){
  var SectorMock = sinon.mock(sector);
  var expectedResult = {postcodes:[{
    Latitude:"3442",
    Longitude:"343432",
    _id:"BN1 7OJ",
    Average_price:"4000"
  },{
    Latitude:"3442",
    Longitude:"343432",
     _id:"BN1 7BJ",
     Average_price:"489844000"
   },{
     Latitude:"3442",
     Longitude:"341432",
      _id:"BN1 7AJ",
      Average_price:"13244000"
    }]};
  SectorMock.expects('find').yields(null, expectedResult);
  sector.find(function (err, result){
    SectorMock.verify();
    SectorMock.restore();
    expect(result).to.equal(expectedResult);
    done();
   });
  });
})
