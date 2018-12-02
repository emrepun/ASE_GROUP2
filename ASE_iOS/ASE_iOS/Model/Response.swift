//
//  Response.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 22.11.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation
struct Response {
    fileprivate var data: Data
    init(data: Data) {
        self.data = data
    }
}

extension Response {
    public func decode<T: Codable>(_ type: T.Type) -> T? {
        let jsonDecoder = JSONDecoder()
        do {
            let response = try jsonDecoder.decode(T.self, from: data)
            return response
        } catch _ {
            return nil
        }
    }
}
