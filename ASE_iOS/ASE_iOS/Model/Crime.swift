//
//  Crime.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 7.12.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

struct Crime: Codable {
    let location: Location?
}

struct Location: Codable {
    let latitude: String?
    let longitude: String?
}
