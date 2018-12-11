//
//  JSONParseProperty.swift
//  ASE_iOSTests
//
//  Created by Emre HAVAN on 2.12.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import XCTest
@testable import ASE_iOS

class JSONParseProperty: XCTestCase {
    
    var mockJsonProperty = Data()
    let jsonDecoder = JSONDecoder()
    var property: Property!
    let pricePaid = 40000
    let street = "London Rd."
    let date = "Fri, 04 Oct 1996"
    
    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
        mockJsonProperty = """
            {
                "pricePaid": 40000,
                "propertyAddress": {
                    "street": "London Rd.",
                    "paon": "24"
                },
                "transactionDate": "Fri, 04 Oct 1996"
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
    
    func testPropertyPricePaidJSONParser() {
        do {
            property = try jsonDecoder.decode(Property.self, from: mockJsonProperty)
            XCTAssert(property.pricePaid! == self.pricePaid)
        } catch _ {
            XCTAssert(false)
        }
    }
    
    func testPropertyStreetNameJSONParser() {
        do {
            property = try jsonDecoder.decode(Property.self, from: mockJsonProperty)
            XCTAssert(property.propertyAddress?.street! == self.street)
        } catch _ {
            XCTAssert(false)
        }
    }
    
    func testPropertyTransactionDateJSONParser() {
        do {
            property = try jsonDecoder.decode(Property.self, from: mockJsonProperty)
            XCTAssert(property.transactionDate == self.date)
        } catch _ {
            XCTAssert(false)
        }
    }
    
    func testPerformanceExample() {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }
    
}
