//
//  MockJson.swift
//  ASE_iOS
//
//  Created by Work on 12/11/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

class MockJson {
    private init () {}
    
    static let json = """
        [
            {
                "price": "239380.09",
                "postcode": "BN2 3QB",
                "latitude": "50.83692",
                "longitude": "-0.124377"
            },
            {
                "price": "114292.09",
                "postcode": "BN2 3QA",
                "latitude": "50.837635",
                "longitude": "-0.124675"
            },
            {
                "price": "439380.09",
                "postcode": "BN2 3DL",
                "latitude": "50.837197",
                "longitude": "-0.124267"
            },
            {
                "price": "539380.09",
                "postcode": "BN2 3LU",
                "latitude": "50.836412",
                "longitude": "-0.124866"
            }
        ]
    """.data(using: .utf8)!
}
