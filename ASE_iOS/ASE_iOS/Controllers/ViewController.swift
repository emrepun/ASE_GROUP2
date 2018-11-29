//
//  ViewController.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 11.10.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit

class ViewController: UIViewController {
    
    @IBOutlet var loginButton: UIButton!
    @IBOutlet var signupButton: UIButton!
    
    let borderColor = BorderColor.borderColor
    
    @IBAction func logInButton(_ sender: UIButton) {
        
        performSegue(withIdentifier: "logIn", sender: self)
    }
    
    
    @IBAction func signUpButton(_ sender: UIButton) {
        
        performSegue(withIdentifier: "signUp", sender: self)
        
    }
    
    let locationManager = CLLocationManager()
    var authorizationStatus = CLLocationManager.authorizationStatus()

    override func viewDidLoad() {
        super.viewDidLoad()
        loginButton.layer.cornerRadius = 14
        loginButton.layer.borderWidth = 1
        loginButton.layer.borderColor = borderColor.cgColor
        signupButton.layer.cornerRadius = 14
        signupButton.layer.borderWidth = 1
        signupButton.layer.borderColor = borderColor.cgColor
    }
}

// MARK: Location Delegate Methods
extension ViewController: CLLocationManagerDelegate, MKMapViewDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
    }
}

