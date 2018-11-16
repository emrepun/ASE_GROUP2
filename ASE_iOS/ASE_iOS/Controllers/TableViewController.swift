//
//  TableViewController.swift
//  ASE_iOS
//
//  Created by Work on 15/11/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit

class TableViewController: UITableViewController {
    
    var postCode = ""
    
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
