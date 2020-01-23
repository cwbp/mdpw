var TICK=960;
var NOFF = "%8";
var NON = "%9";
var PCHANGE = "%C";
var PITCH_REST = 128;
var TYPE = 0;
var PITCH = 1;
var DURATION = 2;
var POSITION = 2;
var VELOCITY = 3;
var INST = 4;
function encodeValue(l,m){
 if(!m){ m = 0;}
// alert(l);
 var tl = "0"+(parseInt(l)|m).toString(16);
 return "%"+tl.substring(tl.length -2);
}
function encodeLength(l){
 if(l > 127){
   var m = 1;
   while((tl = Math.floor(l/m)) > 127){
    m *= 128;
   }
   return encodeValue(tl,0x80)+encodeLength(l%m);
 } else {
   return encodeValue(l);
 }
}
function length4bytes(l){
  var m = 256*256*256;
  var r = "";
  for(var i = 0; i <4 ; i ++){
    var tl = Math.floor(l/m);
    r += encodeValue(tl);
    m = m / 256;
  }
  return r;
}
function toHex2(i){
  var r = i.toString(16);
  if(r.length < 2){
    r = '0'+r;
  }
  return '%'+r;
}
function createSMF(a){
  var tl;
  var r = "";
  for(var i = 0; i < a.length; i ++){
     r += a[i];
     tl += a[i].length/3;
  }
  return "MThd"+length4bytes(6)+"%00%01%00"+encodeValue(a.length)+"%03%C0"+r;
  //トラック数上限255
  //tick 960 テンポはトラック内%ff%51

}
function createTrack(a, tempo){
  if(!tempo){ tempo = 120;}
  if(tempo < 1){ tempo = 120;}
//  alert("tempo:"+tempo);
  var td = '%00%FF%51%03'+length4bytes(60*1000000/tempo).substring(3);
  var ld = '%00%FF%2F%00'
  return "MTrk"+length4bytes((a.length+td.length+ld.length)/3)+td+a+ld;
}

function fillDefault(n, dn){
//  alert("fd n:"+n+" dn:"+dn);
  if(n.length < DURATION+1  || n[DURATION] < 0){
    n[DURATION] = dn[DURATION];
  }
  if(n.length < VELOCITY+1  || [VELOCITY] < 0){
    n[VELOCITY] = dn[VELOCITY];
  }
//  alert("not defined l:"+n.length);
//  alert(n[INST]);
  if(n.length < INST+1  || n[INST] < 0){
//    alert("i not defined l:"+n.length);
    n[INST] = dn[INST];
  }
  dn[0] = n[0];  dn[1] = n[1];  dn[2] = n[2];
  dn[3] = n[3];  dn[4] = n[4];
  return n;
}
/*配列をコンパイル前の内部配列にする
  副作用でdwnがかわる */
function precompileArray(dest, src,  pos, dwn){
// alert("cA");
   if(!pos){ pos = 0;}
   if(!dwn){ dwn = ['n',64,1,64,0];}
   if(src[TYPE] == 'n'){
//alert("dwn;"+dwn);
      var nn = fillDefault(src, dwn);
      if(nn[PITCH] < PITCH_REST){
        insertNote(dest, nn, pos);
      }
//alert(dwn);
      if(LAST_POS < pos+nn[DURATION]){
        LAST_POS = pos+nn[DURATION]; 
      }
      return pos+nn[DURATION];
   } else if(src[TYPE] == 's'){
      var cp = pos;
      for(var i = 1; i < src.length; i ++){
        cp = precompileArray(dest, src[i], cp, dwn);
      }
      return cp;
   } else if(src[TYPE] == 'c'){
      var cp = pos;
      for(var i = 1; i < src.length; i ++){
        var tcp = precompileArray(dest, src[i], pos, dwn);
        if(cp < tcp){ cp = tcp;} //MAX
      }
      return cp;
   }
}
//内部配列を生成する
//n値がそろっている,全体で一つの配列になる
//
function insertNote(a, n, pos){
 var pit = n[PITCH];
 var dur = n[DURATION];
 var vel= n[VELOCITY];
 var inst = n[INST];
 var i = 0;
 var non = [NON, pit, pos, vel,inst ];
 var noff = [NOFF, pit, pos+dur, vel, inst];
 for(; i < a.length && non; i ++){
    if(a[i][POSITION] > non[POSITION]){
        a.splice(i, 0, non);
        non = false;
    }
 }
//alert("i:"+i +" non:"+non);
 if(non){
   a.splice(a.length, 0, non);
 }
 for(; i < a.length && noff; i ++){
    if(a[i][POSITION] >= noff[POSITION]){
        a.splice(i, 0, noff);
        noff = false;
    }
 }
//alert("i:"+i+ " noff:"+noff);

 if(noff){
   a.splice(a.length, 0, noff);
 }
// alert(a.length);
}
//配列を楽器別に分類して、楽器別にチャンネルまたはトラックを作ってバイナリ化


function compileArray(a){
  var r = new Array();
  //楽器別に分類

  for(var i = 0; i < a.length; i ++){
//   alert(a[i]);
    var ins = a[i][INST];
    if(!r[ins]){ r[ins] = new Array();}
    r[ins].push(a[i]);
  }
  //楽器別に分類したものをチャンネルにまとめる

  var channel;
  var ctr = false;
  var tr = new Array();
  for(var i = 0; i < r.length; i ++){
   if(r[i]){
    if(!ctr){
      channel = 0;
      ctr = new Array();
      tr.push(ctr);
    }
    for(var j = 0;  j < r[i].length; j ++){
      var c = r[i][j];
      for(var l = 0; l < ctr.length && c; l ++){
         if(c[POSITION] < ctr[l][POSITION]){
           ctr.splice(l, 0, c);
           c = false;
         }
      }
      if(c){
       ctr.push(c);
      }
    }
    channel ++;
    if(channel > 15){
      channel = 0;
      ctr = false;
    }
   }
  }
//  return tr;
  //trには16音色ごとにチャンネル別にまとめられた配列の配列
  //チャンネルを一つの配列に

  var tr2 = new Array();
  for(var i = 0; i < tr.length; i ++){
    var insts = new Object();
    var instsi = 0;
    var dt = ""; var cp = 0;
   for(var j = 0; j < tr[i].length; j ++){
      var  del = Math.floor((tr[i][j][POSITION] -cp)*TICK);
      cp = tr[i][j][POSITION];
      if(!insts[tr[i][j][INST]]){
        insts[tr[i][j][INST]] = instsi.toString(16); //常に一桁のはず

        dt +=  '%00'+PCHANGE+insts[tr[i][j][INST]]+toHex2(tr[i][j][INST]&0x7f);
        instsi ++;
      }
      dt += encodeLength(del)+tr[i][j][TYPE]+insts[tr[i][j][INST]];
      dt += toHex2(tr[i][j][PITCH])+toHex2(tr[i][j][VELOCITY]);
   }
   tr2.push(dt);
  }
  //trには音色が混じったチャンネル別に配列にまとめられた配列
  //配列をバイナリ化

  return tr2;  
}
var LAST_POS = 0; //

function compileArrayString(s, tempo){
  LAST_POS = 0;
  var t = eval(s);
  var dest = new Array();
  precompileArray(dest, t);
  dest = compileArray(dest);
  var tr = new Array();
  for(var j = 0; j < dest.length; j ++){
    tr.push(createTrack(dest[j], tempo));
  }
  return 'data:audio/midi,'+ createSMF(tr);
}
//とりあえず完成　2010.11.8
//デバッグまだ
//noteoffが複数の場合削る機能がまだ
