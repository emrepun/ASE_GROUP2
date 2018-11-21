var mongoose = require("mongoose").set("debug", true);
var dbCreds = require("../../auth/dbCreds.js");

const DATABASE = "postcodes";
const URL = dbCreds.remoteURL;
const USERNAME = dbCreds.remoteLogin;
const PASSWORD = dbCreds.remotePassword;
const MONGO_URI = `mongodb+srv://${USERNAME}:${PASSWORD}@${URL}`;

const OPTIONS = {
    useNewUrlParser: true,
    dbName: DATABASE
};

mongoose.Promise = global.Promise;
var dbRemote = mongoose.createConnection(MONGO_URI, OPTIONS);

dbRemote.on("error", err => {
    console.log(err);
});

dbRemote.on("open", a => {
    console.log(`Connected to ${DATABASE} at ${MONGO_URI} as ${USERNAME}`);
});

module.exports = { dbRemote };
