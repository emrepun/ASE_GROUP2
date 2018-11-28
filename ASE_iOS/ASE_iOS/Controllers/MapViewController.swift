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
    
    private let networking = Networking()
    
    private var postCodes = [PostCode]()
    
    var isDataRequestSent = false
    
    private var heatmapLayer: GMUHeatmapTileLayer!
    private var gradientColors = [UIColor.green, UIColor.red]
    private var gradientStartPoints = [0.6, 1.0] as [NSNumber]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mapView.delegate = self
        locationSettings()
        updateMapStyle()
        
        heatmapLayer = GMUHeatmapTileLayer()
        heatmapLayer.radius = 80
        heatmapLayer.opacity = 0.8
//        heatmapLayer.gradient = GMUGradient(colors: gradientColors,
//                                            startPoints: gradientStartPoints,
//                                            colorMapSize: 256)
        
        //checkForAuthorization()
        //postTimer = Timer.scheduledTimer(timeInterval: 10, target: self, selector: #selector(writeLocationData), userInfo: nil, repeats: true)     
    }
    
    
    fileprivate func locationSettings() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.requestWhenInUseAuthorization()
    }
    
    fileprivate func updateMapStyle() {
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
    }
    
    private func getPropertyData(lat: String, long: String, radius: String, completion: (() -> Void)?) {
        networking.performNetworkTask(endpoint: PropertyAPI.postCodes(lat: lat, long: long, radius: radius), type: [PostCode].self) { [weak self] (response) in
            self?.postCodes = response
            completion?()
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(true, animated: false)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.navigationController?.setNavigationBarHidden(false, animated: true)
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
    
    func mapView(mapView: MKMapView, viewForAnnotation annotation: MKAnnotation) -> MKAnnotationView?
    {
        var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: "AnnotationIdentifier")
        
        if annotationView == nil {
            annotationView = MKAnnotationView(annotation: annotation, reuseIdentifier: "AnnotationIdentifier")
        }
        
        annotationView?.image = UIImage(named: "homemarker")
        annotationView?.canShowCallout = true
        
        return annotationView
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

            
            if !isDataRequestSent {
                isDataRequestSent = true
                getPropertyData(lat: String(location.coordinate.latitude), long: String(location.coordinate.longitude), radius: "0.5") {
                    DispatchQueue.main.async {
                        var list = [GMUWeightedLatLng]()
                        
                        for postCode in self.postCodes {
                            //let marker = PlaceMarker(postCode: postCode)
                            //marker.map = self.mapView
                            
                            var intPrice = 0
                            
                            if let latitude = postCode.latitude,
                                let lat = Double(latitude),
                                let longitude = postCode.longitude,
                                let long = Double(longitude),
                                let priceString = postCode.price,
                                let price = Float(priceString) {
                                let coords = GMUWeightedLatLng(coordinate: CLLocationCoordinate2DMake(lat , long ), intensity: price.truncatingRemainder(dividingBy: 10))
                                //price.truncatingRemainder(dividingBy: 10)
                                intPrice = Int(price)
                                print(intPrice)
//                                if intPrice < 100000 {
//                                    for _ in 0...30 {
//                                        list.append(coords)
//                                    }
//                                } else if intPrice < 300000 {
//                                    for _ in 0...90 {
//                                        list.append(coords)
//                                    }
//                                } else if intPrice < 999999 {
//                                    for _ in 0...150 {
//                                        list.append(coords)
//                                    }
//                                } else {
//                                    for _ in 0...3000 {
//                                        list.append(coords)
//                                    }
//                                }
                                
                                list.append(coords)
                            }
                        }
                        // Add the lat lngs to the heatmap layer.
                        self.heatmapLayer.weightedData = list
                        self.addHeatMap()
                    }
                }
            }
        }
        
        
        
        guard let camLocation = locations.first else { return }
        
        mapView.camera = GMSCameraPosition(target: camLocation.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
        
        locationManager.stopUpdatingLocation()
    }
    
    func addHeatMap() {
        heatmapLayer.map = mapView
    }
    
    // MARK: Segue settings
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showListPrices" {
            let markerSender = sender as! PlaceMarker
            let destinationVC = segue.destination as! PropertyTableViewController
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

