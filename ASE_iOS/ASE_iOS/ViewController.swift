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
    
    @IBAction func logInButton(_ sender: UIButton) {
        
        performSegue(withIdentifier: "logIn", sender: self)
    }
    
    
    @IBAction func signUpButton(_ sender: UIButton) {
        
        performSegue(withIdentifier: "signUp", sender: self)
        
    }
    
    let locationManager = CLLocationManager()
    var authorizationStatus = CLLocationManager.authorizationStatus()
    
    var user = User(latitude: 0.0, longitude: 0.0)

    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
    }
    
   
    
}

// MARK: Location Delegate Methods
extension ViewController: CLLocationManagerDelegate, MKMapViewDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
    }
}

