//
//  MapViewController.swift
//  ASE_iOS
//
//  Created by Work on 29/10/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit
import FirebaseAuth
import FirebaseDatabase

class MapViewController: UIViewController {
    
    @IBOutlet var mapView: MKMapView!
    @IBOutlet var latitudeLabel: UILabel!
    @IBOutlet var longitudeLabel: UILabel!
    
    
    
    let locationManager = CLLocationManager()
    var authorizationStatus = CLLocationManager.authorizationStatus()
    var user = User(latitude: 0.0, longitude: 0.0)
    var postTimer: Timer!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        locationSettings()
        checkForAuthorization()
        postTimer = Timer.scheduledTimer(timeInterval: 10, target: self, selector: #selector(writeLocationData), userInfo: nil, repeats: true)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        postTimer.invalidate()
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
    
    @objc
    fileprivate func writeLocationData() {
        locationManager.startMonitoringSignificantLocationChanges()
        
        let innerUser = Auth.auth().currentUser
        
        let currentDate = getCurrentMillis()
        
        let timeStamp = String(currentDate)
                
        let ref = Database.database().reference().child("gpsdata").child(innerUser!.uid).child(timeStamp)
                
        let locationObject = [
            "latitude": user.latitude,
            "longitude": user.longitude
        ] as [String: Any]
        
        latitudeLabel.text = "Latitude: \(user.latitude)"
        longitudeLabel.text = "Longitude: \(user.longitude)"
        
        ref.setValue(locationObject) { (error, ref) in
            if error == nil {
                print("chill")
            } else {
                print("error")
            }
        }
        
        locationManager.stopMonitoringSignificantLocationChanges()
    }
    
    
    @IBAction func locateMeButtonTapped(_ sender: Any) {
        let authStat = CLLocationManager.authorizationStatus()
        
        if authStat == .denied || authStat == .restricted || authStat == .notDetermined {
            showAlert(title: "Location Services Disabled", message: "Please enable Location Services in Settings to locate yourself!")
        } else {
            let distance: CLLocationDistance = 500
            let region = MKCoordinateRegion(center: mapView.userLocation.coordinate, latitudinalMeters: distance, longitudinalMeters: distance)
            mapView.setRegion(region, animated: true)
        }
    }
    
    func getCurrentMillis()->Int64 {
        return Int64(Date().timeIntervalSince1970 * 1000)
    }
}
    // MARK: Location Delegate Methods
extension MapViewController: CLLocationManagerDelegate, MKMapViewDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            user.latitude = (location.coordinate.latitude).round(digit: 6)
            user.longitude = location.coordinate.longitude.round(digit: 6)
            latitudeLabel.text = "Latitude: \(user.latitude)"
            longitudeLabel.text = "Longitude: \(user.longitude)"
        }
    }
}


