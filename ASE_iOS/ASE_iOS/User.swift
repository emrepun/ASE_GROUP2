//
//  User.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 12.10.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

class User {
    var username: String
    var password: String
    var latitude: Double
    var longitude: Double
    
    init(username: String, password: String,latitude: Double, longitude: Double) {
        self.latitude = latitude
        self.longitude = longitude
        self.username = username
        self.password = password
    }
}
