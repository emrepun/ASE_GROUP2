//
//  SignUpViewController.swift
//  ASE_iOS
//
//  Created by Work on 29/10/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit
//import FirebaseAuth

class SignUpViewController: UIViewController {

    let borderColor = BorderColor.borderColor
    
    @IBOutlet var signUpButton: UIButton!
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        signUpButton.layer.borderWidth = 1
        signUpButton.layer.borderColor = borderColor.cgColor
        signUpButton.layer.cornerRadius = 14
    }
    
    @IBAction func backButton(_ sender: UIButton) {
        performSegue(withIdentifier: "signUpMain", sender: self)
    }
    
    @IBAction func signUpButton(_ sender: UIButton) {
        
//        Auth.auth().createUser(withEmail: email.text!, password: password.text!) { (authResult, error) in
//            if error == nil {
//                self.performSegue(withIdentifier: "LogIn", sender: self)
//            }
//            else{
//                let alertController = UIAlertController(title: "Error", message: error?.localizedDescription, preferredStyle: .alert)
//                let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
//                
//                alertController.addAction(defaultAction)
//                self.present(alertController, animated: true, completion: nil)
//            }
//        }
    }
}
