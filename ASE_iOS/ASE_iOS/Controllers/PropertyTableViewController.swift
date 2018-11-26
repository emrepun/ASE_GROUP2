//
//  TableViewController.swift
//  ASE_iOS
//
//  Created by Work on 15/11/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit

class PropertyTableViewController: UITableViewController {
    
    private let networking = Networking()
    
    var postCode = ""
    
    var properties = [Property]()

    override func viewDidLoad() {
        super.viewDidLoad()
        print(postCode)
        
        if postCode.count > 0 {
            getPropertyData(postCode: postCode) {
                //guard self.properties.count > 0 else { return } //show alert maybe.
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
                
            }
        }
    }
    
    func getPropertyData(postCode: String, completion: (() -> Void)?) {
        networking.performNetworkTask(endpoint: PropertyAPI.postCodeSpecific(postCode: postCode), type: [Property].self) { [weak self] (response) in
            self?.properties = response
            completion?()
        }
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return properties.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "propertyCell", for: indexPath) as! PropertyTableViewCell
        let address = properties[indexPath.row].propertyAddress
        let price = properties[indexPath.row].pricePaid
        let numberFormatter = NumberFormatter()
        numberFormatter.numberStyle = .decimal
        numberFormatter.groupingSeparator = "."
        let formattedPrice = numberFormatter.string(from: NSNumber(value: price ?? -1))
        cell.addressLabel.text = "\(address?.paon ?? "") - \(address?.street ?? "")"
        cell.priceLabel.text = "\(formattedPrice ?? "") £"
        return cell
    }
}
