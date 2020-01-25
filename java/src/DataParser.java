package com.cwbp.mdpw;
/*
 * Parser for tcl format MPDW data
 * You can use this class save or load MDPW data in the tcl format
 * You can execute this class for play MDPW data in the tcl format.
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
import java.util.*;
import java.io.*;
public class DataParser {
  static char COMMENT = '#';
  /**
   *  encode to tcl
   *  The function implements in Data class.
   */
  public static String encode(Data d){
    return d.getString();
  }
  /**
   *  decode from tcl
   */
  public static Data decode(Reader din){
    StreamTokenizer st = new StreamTokenizer(din);
    st.commentChar(COMMENT);
    st.wordChars('/','/');
//    st.wordChars('-','-');
    st.wordChars('_','_');
    st.wordChars('{','}');
    Vector stack = new Vector();
    Vector currentData = new Vector(); //dummy
    try {
      int  notei = -1;
      double[] notea = new double[4];
      while(true){
        switch(st.nextToken()){
        case StreamTokenizer.TT_NUMBER:
//	  System.out.print("num:"+st.nval+" ");
	  if(-1 < notei && notei < 4){ 
	    notea[notei] = st.nval;
	    notei ++;
	  }
          break;
        case StreamTokenizer.TT_WORD:
	  String ts = st.sval;
	  while(ts.length() > 0){
//	    System.out.println("parse:"+ ts);
	    if(ts.startsWith("{")){ // Starts data definition
	      if(ts.length() == 1){
		st.nextToken();
		ts = st.sval;
	      } else 
		ts = ts.substring(1);
	      if(ts.equals("c")||ts.equals("C")||ts.equals("chord")){
//		System.out.println("chord:");
		currentData.addElement(currentData  = new DataChord());
		stack.insertElementAt(currentData, 0);
		ts = "";
	      } else if(ts.equals("s")||ts.equals("S")||ts.equals("seq")){
//		System.out.println("seq:");
		currentData.addElement(currentData  = new DataSequence());
		stack.insertElementAt(currentData, 0);
		ts = "";
	      } else if(ts.equals("n")||ts.equals("N")||ts.equals("note")){
//		System.out.println("note:");
		notei = 0;
		notea[0] = notea[1] = notea[2] = notea[3] = DataNote.UNUSED; 
		ts = "";
	      } else {
		System.err.println("Unkown tag:"+ts);
	      }
	    } else if(ts.startsWith("}")){
	      if(notei < 0){
		if(stack.size() == 1){
		  System.err.println("End::");
		  return (Data)stack.elementAt(0);
		} else {
		  stack.removeElementAt(0);
		  currentData = (Vector)stack.elementAt(0);
		}
	      } else {
		currentData
		  .addElement(new DataNote((int)notea[0], notea[1],
					   (int)notea[2], (int)notea[3]));
		notei = -1;
	      }
	      ts = ts.substring(1);
	    } else if(ts.startsWith("/")){
	      int tsp = ts.indexOf("}");
	      String tsn;
	      if(tsp < 0){
		tsn = ts.substring(1);
		ts = "";
	      } else {
		tsn = ts.substring(1, tsp);
		ts = ts.substring(tsp);
	      }
	      if(notei == 2){
		notea[1] /= Double.valueOf(tsn).doubleValue();
//		System.err.println(" "+notea[1]);
	      }
	    } else if(ts.startsWith("_")){
	      notea[notei] = DataNote.UNUSED;
	      notei ++;
	      ts = ts.substring(1);
	    } else if(notei == 0){
	      notea[notei] = DataNote.parsePitch(ts);
	      notei ++;
	      ts = "";
	    }
	  }
	  break;
        case StreamTokenizer.TT_EOF:
          System.out.println("EOF:");
          return null;
        }
      }  
    } catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }
  private static void exit1(String s){
    System.err.println(s);
    System.exit(1);
  }
  public static void main(String[] args){
    if(args.length < 4) 
      exit1("Usage:java DataParser file tempo player_class [init]");
    Data r = null;
    try {
      r =  decode(new FileReader(args[0]));
    } catch(Exception e){
      exit1("Can not find file:"+args[0]);
    }
    System.out.println("Result:"+r.getString());
    System.out.println("---------");
    int tempo = Integer.valueOf(args[1]).intValue();
    String s = args[2];
    Player player = null;
    try {
      player = (Player)Class.forName(args[2]).newInstance();
    } catch(Exception e){
      exit1("Can not find Player Class:"+args[2]);
    }
    if(args.length > 3)  player.init(args[3]);
    try {
      while(true){
	System.out.println("DataParser.play1");
	player.play(r);
	while(System.in.available() == 0){}
	System.in.read();
	System.out.println("DataParser.play2");
      }
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}
