const mdpw = require("./mdpw");
const mdpw_nodejs = require("./mdpw_nodejs");
var a = ['s',['n',64,1,64,0],['n',66,1,64,0],['n',68,1,64,0],['n',64,1,64,0]];
var p = mdpw.compile(a, 120);
mdpw_nodejs.save(p, "test.mid");



