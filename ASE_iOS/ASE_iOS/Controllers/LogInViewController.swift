//
//  LogInViewController.swift
//  ASE_iOS
//
//  Created by Work on 29/10/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit
//import FirebaseAuth

class LogInViewController: UIViewController {

    let borderColor = BorderColor.borderColor
    
    @IBOutlet var loginButton: UIButton!
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loginButton.layer.borderWidth = 1
        loginButton.layer.borderColor = borderColor.cgColor
        loginButton.layer.cornerRadius = 14
    }
    
    @IBAction func backButton(_ sender: Any) {
       
        performSegue(withIdentifier: "logInMain", sender: self)
    }
    
    @IBAction func LogInButton(_ sender: UIButton) {
        
//        Auth.auth().signIn(withEmail: email.text!, password: password.text!) { (user, error) in
//            if error == nil{
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
