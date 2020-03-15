const mdpw = require("./mdpw");
const mdpw_nodejs = require("./mdpw_nodejs");
const fs = require('fs')
var a = ['s',['n',64,1,64,6],['n',66,1,64,6],['n',68,1,64,6],['n',64,1,64,6]];
var b = ['n', 52,4,64,0]
var d = ['s',['n', 37, 0.5, 0, 129], ['n', 37, 1, 64, 129], ['n', 38, 1, 64, 129], ['n', 41, 1, 64, 129]]
a = ['s',['n',64,1,64,6], ['n', 37, 1, 64, 129]];
d = ['s',['n', 37, 0.5, 0, 129], ['n', 37, 1, 64, 129], ['n', 38, 1, 64, 129], ['n', 41, 1, 64, 129]]
var c = ['c', a,  d]
var p = mdpw.compile(a, 120);
mdpw_nodejs.save(p, "test.mid");
//fs.writeFileSync("test1.mid", mdpw.toUint8Array(mdpw.compile(d, 120)))

