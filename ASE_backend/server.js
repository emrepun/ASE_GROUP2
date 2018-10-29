const express = require('express');
const bodyParser = require('body-parser');

const PORT = 3000

var app = express();
app.use(bodyParser.json());

var firebaseRouter = require('./routes/fbRoutes');

app.use('/fb', firebaseRouter);

app.listen(PORT, function() {
  console.log(`listening on ${3000}`);
})
