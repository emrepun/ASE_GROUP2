const express = require('express');
const bodyParser = require('body-parser');

const PORT = 3000

var app = express();
app.use(bodyParser.json());

var apiRouter = require('./routes/apiRoutes');

app.use('/api', firebaseRouter);

app.listen(PORT, function() {
  console.log(`listening on ${3000}`);
})
