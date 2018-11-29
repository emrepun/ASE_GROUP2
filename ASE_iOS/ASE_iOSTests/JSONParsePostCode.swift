//
//  JSONParse.swift
//  ASE_iOSTests
//
//  Created by Emre HAVAN on 28.11.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import XCTest
@testable import ASE_iOS

class JSONParsePostCode: XCTestCase {
    
    var mockJsonPostcode = Data()
    let jsonDecoder = JSONDecoder()
    var postCode: PostCode!
    let latitude = "50.83692"
    let longitude = "-0.124377"
    
    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        mockJsonPostcode = """
        
            {
                "price": "239380.09",
                "postcode": "BN2 3QB",
                "latitude": "50.83692",
                "longitude": "-0.124377"
            }
        
    """.data(using: .utf8)!
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
    }
    
    func testPostCodeLatitudeJSONParser() {
        do {
            postCode = try jsonDecoder.decode(PostCode.self, from: mockJsonPostcode)
            XCTAssert(postCode.latitude! == self.latitude)
        } catch _ {
            assertionFailure()
        }
    }
    
    func testPostCodeLongitudeJSONParser() {
        do {
            postCode = try jsonDecoder.decode(PostCode.self, from: mockJsonPostcode)
            XCTAssert(postCode.longitude! == self.longitude)
        } catch _ {
            assertionFailure()
        }
    }

    func testPerformanceExample() {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }

}
