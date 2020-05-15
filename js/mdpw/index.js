const m = require('./mdpw.js')
const mn= require('./mdpw_nodejs')
exports.compile = m.compile
exports.toUint8Array = m.toUint8Array
exports.save = mn.save 

