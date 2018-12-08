/**
 * Main server file, this is what is launched by 'npm start'
 */
const express = require("express");
const bodyParser = require("body-parser");

const PORT = process.env.PORT | 3000;
const HOST = "0.0.0.0";

var app = express();
app.use(bodyParser.json());

var apiRouter = require("./routes/apiRoutes");

app.use("/api", apiRouter);

app.listen(PORT, function() {
    // eslint-disable-next-line no-console
    console.log(`listening on ${HOST}:${3000}`);
});
