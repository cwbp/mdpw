package com.cwbp.mdpw;
/**
 * Data class for MDPW sequence.
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2008, Sho Hosoda
 */
import java.util.*;
public class DataSequence extends Vector<Data> 
implements Data {
	public DataSequence(){}
	public DataSequence(Data ... data){
		for(Data d : data){
			addElement(d);
		}
	}
  public Vector toNotes(double i, DataNote n){ 
    Vector r = new Vector();
  	double odu = n.getDuration();
    for(Enumeration e = elements(); e.hasMoreElements();){
      try {
	   Data d = (Data)e.nextElement();
	   for(Enumeration en =  d.toNotes(i, n).elements();
	    en.hasMoreElements();)
	    r.addElement(en.nextElement());
      	double du = d.getDuration();
      	if(du == UNUSED) du = odu; else  odu = du;  
      	i += du;
      } catch(ClassCastException ex){}
    }
    return r;
  }
  public Data copy(){
    DataSequence r = new DataSequence();
    for(Enumeration e = elements(); e.hasMoreElements();){
      try {
        Data d = (Data)e.nextElement();
	    r.addElement(d.copy());
      } catch(ClassCastException ex){}
    }
    return r;
  }
  public String getString() {
    String r = "{s ";
    for(Enumeration e = elements(); e.hasMoreElements();){
      r += ((Data)e.nextElement()).getString() +" ";
    }
    return r +"}";
  }
  int formerSize = -1;
  double formerDuration;
  public void resetDuration(){
    formerSize = -1;
  }
  public double getDuration(){
    if(formerSize == size()){
      return formerDuration;
    }
    double r = 0;
    double f = 0;
    for(Enumeration e = elements(); e.hasMoreElements();){
      double d = ((Data)e.nextElement()).getDuration();
      if(d == DataNote.UNUSED) d = f; else f = d;
      r += d;
    }    
    formerSize = size();
    formerDuration = r;
    return r;
  }
/*  int volume = 1;
  public void setVolume(int v){
   volume = v;
 }
 public int getVolume(){
   return volume;
 } */
  /**
   * Override Vector methods which reduce the size
   * for duration cache.
   */
public void clear(){
  resetDuration();
  super.clear();
}
public Data remove(int index){
  resetDuration();
  return super.remove(index);
}
public boolean remove(Object element){
  resetDuration();
  return super.remove(element);
}
public boolean removeAll(Collection c){
  resetDuration();
  return super.removeAll(c);
}
public void removeAllElements(){
  resetDuration();
  super.removeAllElements();
}
public boolean removeElement(Object element){
  resetDuration();
  return super.removeElement(element);
}
public void removeElementAt(int index){
  resetDuration();
  super.removeElementAt(index);
}
public boolean retainAll(Collection c){
  resetDuration();
  return super.retainAll(c);
}
public void setSize(int size){
  resetDuration();
  super.setSize(size);
}
	public void visit(double t, Visitor v, int i){
		double time = t;
		if(i > 0 || i <= DEPTHINF){
			double od = 1;
    		for(Enumeration e = elements(); e.hasMoreElements();){
    			Data da = (Data)e.nextElement();
//    			System.out.println("seq.visit:"+time+", "+i);
    			double nd = da.getDuration();
    			if(nd == UNUSED) nd = od;  else  od = nd;
    			double nt = time + nd;
        		da.visit(time,  v, i-1);
    			time = nt;
	    	}    
		}
	  resetDuration();
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
