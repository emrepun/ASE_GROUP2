//
//  Property.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 24.11.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

struct Property: Codable {
    let pricePaid: Int?
    let propertyAddress: Address?
    let transactionDate: String?
}

struct Address: Codable {
    let street: String?
    let paon: String?
}
