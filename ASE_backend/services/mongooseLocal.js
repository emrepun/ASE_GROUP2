var mongoose = require("mongoose");

const DB_IP = "localhost";
const DATABASE = "postcodes";
const AUTH = "";
const MONGO_URI = `mongodb://${AUTH}${DB_IP}:27017/${DATABASE}`;

const OPTIONS = {
    useNewUrlParser: true
};

mongoose.Promise = global.Promise;
mongoose
    .connect(
        MONGO_URI,
        OPTIONS
    )
    .then(a => {
        console.log(
            "Successfully connected to local database:",
            `${a.connections[0].name} at ${a.connections[0].host} as ${
                a.connections[0].user
            }`
        );
    })
    .catch(err => {
        console.log(err);
    });

module.exports = { mongoose };
