//
//  User.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 12.10.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

class User {
    var uid: String
    var latitude: Double
    var longitude: Double
    
    init(uid: String, latitude: Double, longitude: Double) {
        self.uid = uid
        self.latitude = latitude
        self.longitude = longitude
    }
}
