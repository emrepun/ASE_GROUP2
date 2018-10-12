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
    
    @IBOutlet var mapView: MKMapView!
    @IBOutlet var latitudeLabel: UILabel!
    @IBOutlet var longitudeLabel: UILabel!
    
    let locationManager = CLLocationManager()
    var authorizationStatus = CLLocationManager.authorizationStatus()
    
    var user = User(latitude: 0.0, longitude: 0.0)

    override func viewDidLoad() {
        super.viewDidLoad()
        locationSettings()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        checkForAuthorization()
    }
    
    fileprivate func locationSettings() {
        mapView.delegate = self
        mapView.showsUserLocation = true
        mapView.userTrackingMode = .follow
        
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }
    
    fileprivate func checkForAuthorization() {
        if authorizationStatus == .denied || authorizationStatus == .restricted {
            print("big")
            showAlert(title: "Location Services Disabled", message: "Please enable Location Services in Settings to view your location on map")
        }
    }
}

// MARK: Location Delegate Methods
extension ViewController: CLLocationManagerDelegate, MKMapViewDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            user.latitude = (location.coordinate.latitude).round(digit: 6)
            user.longitude = location.coordinate.longitude.round(digit: 6)
            
            updateCoordinateLabels()
        }
    }
    
    fileprivate func updateCoordinateLabels() {
        latitudeLabel.text = String(user.latitude)
        longitudeLabel.text = String(user.longitude)
    }
}

