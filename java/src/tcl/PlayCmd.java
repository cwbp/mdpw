package com.cwbp.mdpw.tcl;
/**
 * Tcl command for playing
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999, Sho Hosoda
 */
import tcl.lang.*;
import java.util.*;
import com.cwbp.mdpw.*;
class PlayCmd implements Command {
  Interp interp;
  Player player;
  public void cmdProc(Interp in, TclObject argv[])
    throws TclException {
    interp = in;
    if(argv.length == 2) {
      if(argv[1].toString().equals("end")){
	while(player.end() > 0){
	}
	return;
      }
      try {
	player.play(toData(argv[1]));
	return;
      } catch(Exception e){
	throw new TclException(interp, e.toString());
      }
    } else if(argv.length == 3) {
      if(argv[1].toString().equals("tempo")){
	if(player != null){
	  int i = TclInteger.get(interp, argv[2]); 
	  player.setTempo(i);
	}
	return;
      }
      try {
	player =(Player)Class.forName(argv[1].toString()).newInstance();
      } catch(Exception ex){
	try {
	  player 
	    =(Player)Class.forName("com.cwbp.mdpw."
				   +argv[1].toString()).newInstance();
	} catch(Exception e){
	  throw new TclException(interp, "Can not find player:"+ argv[1]);
	}
      }
      try {
	player.init(argv[2].toString());
      } catch(ClassCastException e){
	throw new TclException(interp, "Can not init player:"+ argv[1]
			       +" by "+argv[2]);
      }
      return;
    }
    throw new TclNumArgsException(interp, 1, argv, "{list}");
  }
  public Data toData(TclObject o) 
    throws DataInvalidFormatException {
    String s;
    int l;
    try {
      s = TclList.index(interp, o, 0).toString();
      l = TclList.getLength(interp, o);
      if(s.equals("n")){
	int i, v, p; double d;
	if(l > 1) p = TclInteger.get(interp, TclList.index(interp, o, 1)); 
	else p = DataNote.UNUSED;
	if(l > 2){
//	  d = TclDouble.get(interp, TclList.index(interp, o, 2));
	  String s2 = TclList.index(interp, o, 2).toString();
	  StringTokenizer st = new StringTokenizer(s2, "/");
	  d = Double.valueOf(st.nextToken()).doubleValue();
	  if(st.hasMoreTokens()){
	    d /= Double.valueOf(st.nextToken()).doubleValue();
	  }
	} else d = DataNote.UNUSED;
	if(l > 3) v = TclInteger.get(interp, TclList.index(interp, o, 3)); 
	else v = DataNote.UNUSED;
	if(l > 4) i = TclInteger.get(interp, TclList.index(interp, o, 4)); 
	else i = DataNote.UNUSED;
	return new DataNote(p, d, v, i); 
      } else if(s.equals("b")){ 
	int[] is = new int[l-1];
	for(int i = 1; i < l; i ++){
	  is[i-1] = TclInteger.get(interp, TclList.index(interp, o, i));
	  //System.err.println("is["+(i-1)+"]="+is[i-1]);
	}
	return new DataBinary(is);
      } else if(s.equals("c")){ 
	DataChord r = new DataChord();
	for(int i = 1; i < l; i++){
	  r.addElement(toData(TclList.index(interp, o, i)));
	}
	return r;
      } else if(s.equals("s")){
	DataSequence r = new DataSequence();
	for(int i = 1; i < l; i++){
	  r.addElement(toData(TclList.index(interp, o, i)));
	}
	///System.err.println("length:"+r.size());
	return r;
      } else {
	throw(new DataInvalidFormatException(o.toString()));
      }
    } catch(Exception e){
      throw(new DataInvalidFormatException(o.toString()));
    }
  }
}
