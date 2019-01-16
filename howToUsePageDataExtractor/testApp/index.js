// Plugins
const http = require('http');
const fs = require('fs');

require.extensions['.html'] = function (module, filename) {
    module.exports = fs.readFileSync(filename, 'utf8');
};

// Constants
const config = require('./config');
const SERVER_PORT = config.port || 3000;
const paramsList = config.paramsList || [];

const requestHandler = (request, response) => {
    let params = paramsList.map(param => {
        let str = getParamEl(param)
        param.min+=param.progression;
        param.max+=param.progression;
        return str + '\r\n';
		
    });
    response.setHeader('Content-Type', 'text/html');
    response.end(tpl(params.join('')))
}

const server = http.createServer(requestHandler)

server.listen(SERVER_PORT, (err) => {
    if (err) {
        return console.log('something bad happened', err)
    }

    console.log(`server is listening on ${SERVER_PORT}`)
})

///////
function getRndInteger(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

function getParamEl(param) {
    return `<div>${param.name}=${getRndInteger(param.min, param.max)}</div>`;
}

function tpl(stringData) {
    return `
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta http-equiv="X-UA-Compatible" content="ie=edge">
            <title>testApp</title>
        </head>
        <body>

            <h1>Test Server status page</h1>

            These are some server metrics:<br>

            <br>
            ${stringData}
        </body>
        </html>
    `;
}