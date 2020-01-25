package com.cwbp.mdpw;
/**
 * Data class for a Chord.
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
import java.util.*;
public class DataChord extends Vector<Data>
implements Data {
	public DataChord(){}
	public DataChord(Data ... data){
		for(Data d : data){
			addElement(d);
		}
	}
  public Vector toNotes(double i, DataNote n){ 
    Vector r = new Vector();
    for(Enumeration e = elements(); e.hasMoreElements();){
      try {
		Data d = (Data)e.nextElement();
		for(Enumeration en = d.toNotes(i, n).elements();
	      en.hasMoreElements();){
	  		r.addElement(en.nextElement());
		}
      } catch(ClassCastException ex){}
    }
    return r;
  }
  public Data copy(){
    DataChord r = new DataChord();
    for(Enumeration e = elements(); e.hasMoreElements();){
      try {
  	Data d = (Data)e.nextElement();
	r.addElement(d.copy());
      } catch(ClassCastException ex){}
    }
    return r;
  }
  public String getString() {
    String r = "{c ";
    for(Enumeration e = elements(); e.hasMoreElements();){
      r += ((Data)e.nextElement()).getString() +" ";
    }
    return r +"}";
  }
  public double getDuration(){
    double r = 0;
    if(size() > 0){
      return ((Data)firstElement()).getDuration();
    } else 
      return 0;
  }
	public void visit(double t, Visitor v, int i){
		if(i > 0 || i <= DEPTHINF){
    		for(Enumeration e = elements(); e.hasMoreElements();){
        		 ((Data)e.nextElement()).visit(t, v, i-1);
	    	}    
		}
	}
	public void visit(double t, Visitor v){
		visit(t, v, DEPTHINF);
	}
	public void visit(Visitor v){
		visit(0, v, DEPTHINF);
	}
	public void visit(Visitor v, int i){
		visit(0, v, i);
	}
}
