//
//  Networking.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 22.11.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

struct Networking {
    func performNetworkTask<T: Codable>(endpoint: EndpointType,
                                        type: T.Type,
                                        completion: ((_ response: T) -> Void)?) {
        var url: URL!
        
        if endpoint is CrimeAPI {
            url = endpoint.baseURL
        } else {
            url = endpoint.baseURL.appendingPathComponent(endpoint.path)
        }
        
        let urlRequest = URLRequest(url: url)
        
        print(url)
        
        let urlSession = URLSession.shared.dataTask(with: urlRequest) { (data, urlResponse, error) in
            
            if let _ = error {
                print("error")
                return
            }
            
            guard let data = data else {
                print("data is not data")
                return
            }
            
            let response = Response(data: data)
            guard let decoded = response.decode(type) else {
                return
            }
            
            completion?(decoded)
        }
        
        urlSession.resume()
    }
}
