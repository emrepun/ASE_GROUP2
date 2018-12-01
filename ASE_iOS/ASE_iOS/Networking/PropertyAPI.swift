//
//  PropertyAPI.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 22.11.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

enum PropertyAPI {
    case postCodes(lat: String, long: String, radius: String)
    case postCodeSpecific(postCode: String)
}

extension PropertyAPI: EndpointType {
    var baseURL: URL {
        return URL(string: "https://1550b5a9.ngrok.io/api")!
    }
    
    var path: String {
        switch self {
        case .postCodes(let lat, let long, let radius):
            return "/pcprices/\(lat)/\(long)/\(radius)"
        case .postCodeSpecific(let postCode):
            return "/addresses/\(postCode.lowercased())"
        }
    }
}
