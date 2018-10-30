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
    var user = User(uid:"test",latitude: 0.0, longitude: 0.0)
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        checkForAuthorization()
        // Do any additional setup after loading the view.
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
    
    fileprivate func writeInitialLocationData() {
        
        /**let userID = Auth.auth().currentUser?.uid
        
        ref = Database.database().reference()
        
        self.ref.child("users").child(userID).setValue([])*/
        
        //Auth.auth().signIn(withEmail: email.text!, password: password.text!) { (user, error) in
          //  if error == nil {
                
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
            }
            
        //}
        

    
    
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
    
//}

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

}
