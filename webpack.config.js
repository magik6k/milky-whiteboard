'use strict'

module.exports = require('./scalajs.webpack.config');

module.exports.resolve = {
    alias: {
        zlib: 'browserify-zlib-next'
    }
};

module.exports.module.preLoaders.push({ test: /\.json$/, loader: 'json-loader' });
