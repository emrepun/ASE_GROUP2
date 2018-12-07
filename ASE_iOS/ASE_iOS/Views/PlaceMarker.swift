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
    
    var postCodeName: String?
    
    init(postCode: PostCode) {
        super.init()
        
        self.postCodeName = postCode.postcode
        
        if let latitude = postCode.latitude, let longitude = postCode.longitude {
            if let lat: CLLocationDegrees = CLLocationDegrees(latitude), let long: CLLocationDegrees = CLLocationDegrees(longitude) {
                let coordinate = CLLocationCoordinate2D(latitude: lat, longitude: long)
                position = coordinate
            }
        }
        
        let view = Bundle.main.loadNibNamed("MarkerInfoView", owner: nil, options: nil)?.first as! MarkerInfoView
        
        if let stringPrice = postCode.price {
            if let price = Double(stringPrice) {
                let numberFormatter = NumberFormatter()
                numberFormatter.numberStyle = .decimal
                numberFormatter.groupingSeparator = "."
                let formattedNumber = numberFormatter.string(from: NSNumber(value: price))
                view.priceLabel.text = "\(formattedNumber ?? "") £"
            } else {
                view.priceLabel.text = "N/A"
            }
        }
        iconView = view
        appearAnimation = .pop
    }
}


