//
//  ApiController.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 22.11.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import Foundation

//class Apicontroller {
//
//    private init() {}
//
//    private let baseUrl = "https://6746a94d.ngrok.io/api"
//    private let postCodeDecoder = JSONDecoder()
//
//    static let shared = Apicontroller()
//
//    func fetchPostCodeByAverage(parameters: Parameters) -> [PostCode]? {
//        var postCodes: [PostCode]?
//
//        Alamofire.request(baseUrl, method: .get, parameters: parameters, encoding: URLEncoding.default).validate().responseJSON { response in
//            switch response.result {
//            case .success(let value):
//
//                let chill = try! self.postCodeDecoder.decode([PostCode].self, from: response.data!)
//                postCodes = chill
//            case .failure( _):
//                postCodes = nil
//            }
//        }
//        return postCodes
//    }
//}
