//
//  Extensions.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 12.10.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit

extension UIViewController {
    func showAlert(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Okay", comment: ""), style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}

extension Double {
    func round(digit: Int) -> Double {
        let multiplier = pow(10.0, Double(digit))
        return (self*multiplier).rounded()/multiplier
    }
}

extension Date {
    var ticks: UInt64 {
        return UInt64((self.timeIntervalSince1970 + 62_135_596_800) * 10_000_000)
    }
}


