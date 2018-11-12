//
//  PlaceMarker.swift
//  ASE_iOS
//
//  Created by Work on 12/11/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation
import GoogleMaps
import UIKit

class PlaceMarker: GMSMarker {
    
    init(postCode: PostCode) {
        super.init()
        
        if let lat: CLLocationDegrees = CLLocationDegrees(postCode.latitude!), let long: CLLocationDegrees = CLLocationDegrees(postCode.longitude!) {
            let coordinate = CLLocationCoordinate2D(latitude: lat, longitude: long)
            position = coordinate
        }
        
        let view = Bundle.main.loadNibNamed("MarkerInfoView", owner: nil, options: nil)?.first as! MarkerInfoView
        view.priceLabel.text = postCode.price!
        
        iconView = view
        appearAnimation = .pop
    }
    
}
