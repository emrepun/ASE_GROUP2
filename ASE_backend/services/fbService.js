var admin = require("firebase-admin");

var serviceAccount = require("../../auth/ase-group2-task3-firebase-adminsdk-i8puh-3a78fe19d5.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://ase-group2-task3.firebaseio.com/"
});


module.exports = admin;
