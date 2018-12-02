//
//  ASE_iOSTests.swift
//  ASE_iOSTests
//
//  Created by Emre HAVAN on 2.12.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import XCTest
@testable import ASE_iOS

class ASE_iOSTests: XCTestCase {
    
    var apiEndPoint = PropertyAPI.postCodes(lat: "", long: "", radius: "")
    var latitude = ""
    var longitude = ""
    var radius = ""
    var apiPath = ""
    var apiBaseUrl = ""

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
        latitude = "50.831792"
        longitude = "-0.135262"
        radius = "0.5"
        apiPath.append("/pcprices/\(latitude)/\(longitude)/\(radius)")
        apiBaseUrl.append("https://ase-group2.herokuapp.com/api")
        apiEndPoint = PropertyAPI.postCodes(lat: latitude, long: longitude, radius: radius)
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
    }
    
    func testApiEndpointPath() {
        XCTAssert(apiEndPoint.path == apiPath)
    }
    
    func testApiBaseUrl() {
        XCTAssert(apiEndPoint.baseURL.absoluteString == apiBaseUrl)
    }

    func testPerformanceExample() {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }

}
