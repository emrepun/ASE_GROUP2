//
//  PropertyTableViewCell.swift
//  ASE_iOS
//
//  Created by Emre HAVAN on 25.11.2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit

class PropertyTableViewCell: UITableViewCell {
    @IBOutlet var addressLabel: UILabel!
    @IBOutlet var priceLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
