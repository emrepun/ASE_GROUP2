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
import GoogleMaps

class MapViewController: UIViewController {
    
    @IBOutlet var mapView: GMSMapView!
    
    let locationManager = CLLocationManager()
    //var authorizationStatus = CLLocationManager.authorizationStatus()
    var user = User(latitude: 0.0, longitude: 0.0)
    //var postTimer: Timer!
    
    var postCodes = [PostCode]()
    
    let postCodeDecoder = JSONDecoder()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mapView.delegate = self
        locationSettings()
        fetchData()
        
        postCodeDecoder.keyDecodingStrategy = .convertFromSnakeCase
        
        do {
            // Set the map style by passing the URL of the local file.
            if let styleURL = Bundle.main.url(forResource: "style", withExtension: "json") {
                mapView.mapStyle = try GMSMapStyle(contentsOfFileURL: styleURL)
            } else {
                NSLog("Unable to find style.json")
            }
        } catch {
            NSLog("One or more of the map styles failed to load. \(error)")
        }
        
        //checkForAuthorization()
        //postTimer = Timer.scheduledTimer(timeInterval: 10, target: self, selector: #selector(writeLocationData), userInfo: nil, repeats: true)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(true, animated: false)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: true)
    }
    
    fileprivate func locationSettings() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.requestWhenInUseAuthorization()
        //locationManager.startUpdatingLocation()
    }
    
    func fetchData() {
        postCodes = try! postCodeDecoder.decode([PostCode].self, from: MockJson.json)
        
        for postCode in postCodes {
            let marker = PlaceMarker(postCode: postCode)
            marker.map = self.mapView
        }
    }
    
//    fileprivate func checkForAuthorization() {
//        if authorizationStatus == .denied || authorizationStatus == .restricted {
//            print("big")
//            showAlert(title: "Location Services Disabled", message: "Please enable Location Services in Settings to view your location on map")
//        }
//    }
    
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
        
        ref.setValue(locationObject) { (error, ref) in
            if error == nil {
                print("chill")
            } else {
                print("error")
            }
        }
        
        locationManager.stopMonitoringSignificantLocationChanges()
    }
    
    
//    @IBAction func locateMeButtonTapped(_ sender: Any) {
//        let authStat = CLLocationManager.authorizationStatus()
//        
//        if authStat == .denied || authStat == .restricted || authStat == .notDetermined {
//            showAlert(title: "Location Services Disabled", message: "Please enable Location Services in Settings to locate yourself!")
//        } else {
//            let distance: CLLocationDistance = 500
//            let region = MKCoordinateRegion(center: mapView.userLocation.coordinate, latitudinalMeters: distance, longitudinalMeters: distance)
//            mapView.setRegion(region, animated: true)
//        }
//    }
    
    func getCurrentMillis()->Int64 {
        return Int64(Date().timeIntervalSince1970 * 1000)
    }
}
    // MARK: Location Delegate Methods
extension MapViewController: CLLocationManagerDelegate {
    
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        guard status == .authorizedWhenInUse else {
            return
        }
        
        locationManager.startUpdatingLocation()
        
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            user.latitude = (location.coordinate.latitude).round(digit: 6)
            user.longitude = location.coordinate.longitude.round(digit: 6)
        }
        
        guard let camLocation = locations.first else { return }
        
        mapView.camera = GMSCameraPosition(target: camLocation.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
        
        locationManager.stopUpdatingLocation()
    }
    
    // MARK: Segue settings
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showListPrices" {
            let markerSender = sender as! PlaceMarker
            let destinationVC = segue.destination as! TableViewController
            if let postCode = markerSender.postCodeName {
                destinationVC.postCode = postCode
            }
        }
    }
}

extension MapViewController: GMSMapViewDelegate {
    
    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        // FIXME: Fix
        // TODO: Open Post Code Specific house prices list.
        performSegue(withIdentifier: "showListPrices", sender: marker)
        return true
    }
    
}


