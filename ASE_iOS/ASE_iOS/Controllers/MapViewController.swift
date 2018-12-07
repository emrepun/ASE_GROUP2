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
import GoogleMaps
import GooglePlaces
import GooglePlacePicker



class MapViewController: UIViewController, GMSPlacePickerViewControllerDelegate {
    
    @IBOutlet var mapView: GMSMapView!
    
    @IBOutlet var toggleButton: UIButton!
    
    let activityIndicator = UIActivityIndicatorView()
    
    let locationManager = CLLocationManager()
    //var authorizationStatus = CLLocationManager.authorizationStatus()
    var user = User(latitude: 0.0, longitude: 0.0)
    //var postTimer: Timer!
    
    private let networking = Networking()
    
    private var postCodes = [PostCode]()
    var list = [GMUWeightedLatLng]()
    
    var isDataRequestSent = false
    var currentRadius = 0.0
    
    private var heatmapLayer: GMUHeatmapTileLayer!
    private var gradientColors = [UIColor.green, UIColor.red]
    private var gradientStartPoints = [0.6, 1.0] as [NSNumber]
    
    var isHeatmap = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        activityIndicator.style = .gray
        activityIndicator.hidesWhenStopped = true
        activityIndicator.center = view.center
        view.addSubview(activityIndicator)
        activityIndicator.startAnimating()
        mapView.delegate = self
        locationSettings()
        updateMapStyle()
        
        heatmapLayer = GMUHeatmapTileLayer()
        heatmapLayer.radius = 80
        heatmapLayer.opacity = 0.8
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
    
    @IBAction func toggleViewTapped(_ sender: Any) {
        activityIndicator.startAnimating()
        if isHeatmap {
            isHeatmap = !isHeatmap
            toggleButton.setTitle("Heatmap", for: .normal)
            heatmapLayer.map = nil
            guard postCodes.count > 0 else { return }
            
            DispatchQueue.main.async {
                for postCode in self.postCodes {
                    let marker = PlaceMarker(postCode: postCode)
                    marker.map = self.mapView
                }
            }
            
        } else {
            isHeatmap = !isHeatmap
            toggleButton.setTitle("Markers", for: .normal)
            guard list.count > 0 else { return }
            mapView.clear()

            let heatLayer = GMUHeatmapTileLayer()
            heatLayer.radius = 80
            heatLayer.opacity = 0.8
            heatLayer.gradient = GMUGradient(colors: gradientColors,
                                             startPoints: gradientStartPoints,
                                             colorMapSize: 256)
            heatLayer.weightedData = list
            heatLayer.map = mapView
            heatmapLayer = heatLayer
            heatmapLayer.map = mapView
        }
        activityIndicator.stopAnimating()
    }
    
    @IBAction func refreshTapped(_ sender: Any) {
        activityIndicator.startAnimating()
        let centerCoordinate = mapView.getCenterCoordinate()
        let radius = mapView.getRadius() / 1000
        currentRadius = radius
        
        let strRadius = String(radius)
        let latitude = String(centerCoordinate.latitude)
        let longitude = String(centerCoordinate.longitude)
        
        isHeatmap = false
        toggleButton.setTitle("Heatmap", for: .normal)
        heatmapLayer.map = nil
        
        emptyArrays { (success) in
            if success {
                self.getPropertyData(lat: latitude, long: longitude, radius: strRadius) {
                    DispatchQueue.main.async {
                        self.getMarkerAndHeatmapAfterCompletion()
                    }
                }
            }
        }
    }
    
    func emptyArrays(completion: @escaping (_ success: Bool) -> Void) {
        DispatchQueue.main.async {
            self.postCodes.removeAll()
            self.list.removeAll()
            self.mapView.clear()
            
            completion(true)
        }
    }
    
    @IBAction func searchPlaceTapped(_ sender: Any) {
        let config = GMSPlacePickerConfig(viewport: nil)
        let placePicker = GMSPlacePickerViewController(config: config)
        placePicker.delegate = self
        present(placePicker, animated: true, completion: nil)
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
    
    fileprivate func getMarkerAndHeatmapAfterCompletion() {
        DispatchQueue.main.async {
            for postCode in self.postCodes {
                let marker = PlaceMarker(postCode: postCode)
                marker.map = self.mapView
                
                if let latitude = postCode.latitude,
                    let lat = Double(latitude),
                    let longitude = postCode.longitude,
                    let long = Double(longitude),
                    let priceString = postCode.price {
                    let price = Float(priceString) ?? 0.0
                    let coords = GMUWeightedLatLng(coordinate: CLLocationCoordinate2DMake(lat,long), intensity: price.truncatingRemainder(dividingBy: 10))
                    
                    self.list.append(coords)
                }
            }
            
            self.activityIndicator.stopAnimating()
            // Add the lat lngs to the heatmap layer.
            self.heatmapLayer.weightedData = self.list
        }
        
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            user.latitude = (location.coordinate.latitude).round(digit: 6)
            user.longitude = location.coordinate.longitude.round(digit: 6)
            
            if !isDataRequestSent {
                isDataRequestSent = true
                print(location.coordinate.latitude)
                print(location.coordinate.longitude)
                getPropertyData(lat: String(location.coordinate.latitude), long: String(location.coordinate.longitude), radius: "0.1") {
                    DispatchQueue.main.async {
                        self.getMarkerAndHeatmapAfterCompletion()
                    }
                }
            }
        }
        
        guard let camLocation = locations.first else { return }
        
        mapView.camera = GMSCameraPosition(target: camLocation.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
        
        locationManager.stopUpdatingLocation()
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
        if currentRadius < 1 {
            performSegue(withIdentifier: "showListPrices", sender: marker)
        }
        return true
    }
}


// MARK: Places Search
extension MapViewController {
    // The code snippet below shows how to create and display a GMSPlacePickerViewController.
    @IBAction func pickPlace(_ sender: UIButton) {
        let config = GMSPlacePickerConfig(viewport: nil)
        let placePicker = GMSPlacePickerViewController(config: config)
        
        present(placePicker, animated: true, completion: nil)
    }
    
    // To receive the results from the place picker 'self' will need to conform to
    // GMSPlacePickerViewControllerDelegate and implement this code.
    func placePicker(_ viewController: GMSPlacePickerViewController, didPick place: GMSPlace) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        let radius = mapView.getRadius() / 1000
        currentRadius = radius
        
        let strRadius = String(radius)
        let latitude = String(place.coordinate.latitude)
        let longitude = String(place.coordinate.longitude)
        
        isHeatmap = false
        toggleButton.setTitle("Heatmap", for: .normal)
        heatmapLayer.map = nil
        
        emptyArrays { (success) in
            if success {
                let location = GMSCameraPosition.camera(withLatitude: place.coordinate.latitude, longitude: place.coordinate.longitude, zoom: 17.0)
                self.mapView.animate(to: location)
                self.getPropertyData(lat: latitude, long: longitude, radius: strRadius) {
                    DispatchQueue.main.async {
                        self.getMarkerAndHeatmapAfterCompletion()
                    }
                }
            }
        }
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
    }
}

