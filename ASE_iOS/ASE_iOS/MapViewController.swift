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
    
    let locationManager = CLLocationManager()
    var authorizationStatus = CLLocationManager.authorizationStatus()
    var ref: DatabaseReference!
    var user = User(latitude: 0.0, longitude: 0.0)
    var postTimer: Timer!
    var home: CLLocation!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        checkForAuthorization()
        postTimer = Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(writeLocationData), userInfo: nil, repeats: true)
        let gestureRecognizer = UITapGestureRecognizer(target: self, action:"handleTap:")
        gestureRecognizer.delegate = (self as! UIGestureRecognizerDelegate)
        mapView.addGestureRecognizer(gestureRecognizer)
        // Do any additional setup after loading the view.
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
    
    //Added to conform to design documentation but given Martin's reply I feel writeLocationData could just be mapped to a button or function 'contact home' that then provides feedback to user that location data has been sent
    @objc
    fileprivate func handleTap(gestureReconizer: UILongPressGestureRecognizer) {
        
        let location = gestureReconizer.location(in:mapView)
        let coordinate = mapView.convert(location,toCoordinateFrom: mapView)
        
        let annotation = MKPointAnnotation()
        annotation.coordinate = coordinate
        mapView.addAnnotation(annotation)
    }
    
    @objc
    fileprivate func writeLocationData() {
        
        locationManager.startMonitoringSignificantLocationChanges()
        
        let innerUser = Auth.auth().currentUser
                
        let timeStamp = String(Date().ticks)
                
        let ref = Database.database().reference().child("gpsdata").child(innerUser!.uid).child(timeStamp)
                
        let dogObject = [
            "Latitude": user.latitude,
            "Longitude": user.longitude
        ] as [String: Any]
                
                
        ref.setValue(dogObject) { (error, ref) in
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
}
    // MARK: Location Delegate Methods
    extension MapViewController: CLLocationManagerDelegate, MKMapViewDelegate {
        func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
            if let location = locations.last {
                user.latitude = (location.coordinate.latitude).round(digit: 6)
                user.longitude = location.coordinate.longitude.round(digit: 6)
            
            }
        }
    }

extension Date {
    var ticks: UInt64 {
        return UInt64((self.timeIntervalSince1970 + 62_135_596_800) * 10_000_000)
    }
}
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
