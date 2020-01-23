const fs = require('fs')
exports.save = function(s, file){
  var fos = fs.createWriteStream(file);
  var i = s.indexOf(",")+1;
  while(i < s.length){
    var si = s.charCodeAt(i);
    if(si == "+".charCodeAt(0)){
      si = " ".charCodeAt(0);
    } else if(si == "%".charCodeAt(0)){
      si = s.charAt(i+1)+s.charAt(i+2);
      i++; i++;
      si = parseInt(si, 16);
    }
    fos.write(new Uint8Array([si]));
    i++;
  }
  fos.end();
}
