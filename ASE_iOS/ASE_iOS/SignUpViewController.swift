//
//  SignUpViewController.swift
//  ASE_iOS
//
//  Created by Work on 29/10/2018.
//  Copyright © 2018 Emre HAVAN. All rights reserved.
//

import UIKit
import FirebaseAuth

class SignUpViewController: UIViewController {

    
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var username: UITextField!
    @IBOutlet weak var password: UITextField!
    
    @IBAction func backButton(_ sender: UIButton) {
        
        performSegue(withIdentifier: "signUpMain", sender: self)
        
    }
    
    @IBAction func signUpButton(_ sender: UIButton) {
        
        Auth.auth().createUser(withEmail: email.text!, password: password.text!) { (authResult, error) in
            if error == nil {
                self.performSegue(withIdentifier: "", sender: self)
            }
            else{
                let alertController = UIAlertController(title: "Error", message: error?.localizedDescription, preferredStyle: .alert)
                let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
                
                alertController.addAction(defaultAction)
                self.present(alertController, animated: true, completion: nil)
            }
            guard let user = authResult?.user else { return }
        }
        
        performSegue(withIdentifier: "SignUp", sender: self)
        
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
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
