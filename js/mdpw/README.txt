This package is a generator for MIDI file from Array of Array. 
Here is a sample code.
```
const mdpw = require("mdpw");
var a = ['s',['n',64,1,64,1],['n',68,1,64,1],['n',71,1,64,1],
             ['c',['n',64,1,64,1],['n',68,1,64,1],['n',71,1,64,1]]];
var p = mdpw.compile(a, 120);
mdpw.save(p, "sample.mid");
```
'n' means a note and followed values are pitch, duration, velocity and instrument. The instrument 129 means Drum.
's' means to play sequentially and 'c' means to play simultaneously.
