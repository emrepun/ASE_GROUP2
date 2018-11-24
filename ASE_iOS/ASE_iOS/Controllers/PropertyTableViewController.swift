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
    
//    var dogCode: [String: AnyObject]!
//
//    var addresses: [AnyObject] {
//        get {
//            if (dogCode["Addresses"] as? [AnyObject]) != nil {
//                return self.addresses
//            } else {
//                return [AnyObject]()
//            }
//        }
//    }

    override func viewDidLoad() {
        super.viewDidLoad()
        print(postCode)
        
        if postCode.count > 0 {
            getPropertyData(postCode: postCode) {
                guard self.properties.count > 0 else { return } //show alert maybe.
                self.tableView.reloadData()
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
        return 0
    }
}
