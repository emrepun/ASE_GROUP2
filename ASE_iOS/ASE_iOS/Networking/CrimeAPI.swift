//
//  CrimeAPI.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 7.12.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

enum CrimeAPI {
    case crimes(lat: String, long: String)
}

extension CrimeAPI: EndpointType {
    var baseURL: URL {
        switch self {
        case .crimes(let lat, let long):
            if let url = URLComponents(scheme: "https",
                                       host: "data.police.uk",
                                       path: "\(path)", queryItems: [
                                        URLQueryItem(name: "lat", value: "\(lat)"),
                                        URLQueryItem(name: "lng", value: "\(long)")]).url {
                return url
            } else {
                return URL(string: "https://data.police.uk/api/crimes-street/all-crime")!
            }
        }
    }
    
    var path: String {
        switch self {
        case .crimes(_, _):
            return "/api/crimes-street/all-crime"
        }
    }
}
