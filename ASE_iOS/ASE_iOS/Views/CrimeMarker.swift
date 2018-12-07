//
//  CrimeMarker.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 7.12.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation
import GoogleMaps

class CrimeMarker: GMSMarker {
    
    init(crime: Crime) {
        super.init()
        
        if let latitude = crime.location?.latitude, let longitude = crime.location?.longitude {
            if let lat: CLLocationDegrees = CLLocationDegrees(latitude), let long: CLLocationDegrees = CLLocationDegrees(longitude) {
                let coordinate = CLLocationCoordinate2D(latitude: lat, longitude: long)
                position = coordinate
            }
        }
        
        icon = #imageLiteral(resourceName: "homemarker")
        appearAnimation = .pop
    }
}
