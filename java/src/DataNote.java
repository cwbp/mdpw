package com.cwbp.mdpw;
/**
 * Data class for single note.
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
import java.util.*;
public class DataNote implements Data {
  int pitch;
  double duration;
  int velocity;
  int instrument;
  double start = 0;
//  public static final int VOFFSET = 64;
  public static final int BINARY = -2;
//  public static final int VUNUSED = -VOFFSET+UNUSED;
  public static final int VREST = 0;
  protected DataNote(){}
  public DataNote(int p){
    pitch = p;     duration = UNUSED;
    velocity = UNUSED;  instrument = UNUSED;
  }
  public DataNote(int p, double d){
    pitch = p;     duration = d;
    velocity = UNUSED;  instrument = UNUSED;
  }
  public DataNote(int p, double d, int v){
    pitch = p;             duration = d;
    velocity = v;  instrument = UNUSED;
  }
  public DataNote(int p, double d, int v, int i){
    pitch = p;             duration = d;
    velocity = v;  instrument = i;
  }
  public DataNote(String p, double d){
    pitch = parsePitch(p);     duration = d;
    velocity = UNUSED;  instrument = UNUSED;
  }
  public DataNote(String p, double d, int v){
    pitch = parsePitch(p);     duration = d;
    velocity = v;  instrument = UNUSED;
  }
  public DataNote(String p, double d, int v, int i){
    pitch = parsePitch(p);     duration = d;
    velocity = v;  instrument = i;
  }
  void setStart(double s){
    start = s;
  }
  double getStart(){
    return start;
  }
  public void setInstrument(int i){
    instrument = i;
  }
  public int getInstrument(){
    return instrument;
  }
  public Data copy(){
    try {
      return (Data)clone();
    } catch(CloneNotSupportedException e){
      e.printStackTrace();// Never thrown
    }
    return null;
  }
  public Vector toNotes(double i, DataNote n){
    Vector r = new Vector();
    try {
      DataNote nn = (DataNote)clone();
      nn.start = i;
      if(n != null){
	if(nn.pitch     == UNUSED) nn.pitch  = n.pitch;        else n.pitch     = nn.pitch;   
	if(nn.duration  == UNUSED) nn.duration  = n.duration;  else n.duration  = nn.duration;  
	if(nn.velocity  == UNUSED) nn.velocity  = n.velocity;  
        else if(nn.velocity > 0)    n.velocity  = nn.velocity;  
	if(nn.instrument== UNUSED) nn.instrument= n.instrument;else n.instrument= nn.instrument;
      }
      if(nn.velocity > 0){
	if(nn.velocity < 1)   nn.velocity = 1;
	if(nn.velocity > 127) nn.velocity = 127;
      }
///    	System.err.println(nn.getString() +" <= "+n.getString()+" at "+i);
      if(nn.pitch == BINARY || nn.duration != 0 || nn.velocity != 0)
	r.addElement(nn);
    } catch(CloneNotSupportedException e){}
    return r;
  }
  public double getDuration(){
    return duration;
  }
  public String getString(){
    String r = "{n ";
    if(pitch != UNUSED) r += ""+pitch; else r+="_";
    r += " ";
    if(duration != UNUSED) r += ""+duration; else r+="_";
    r += " ";
    if(velocity != UNUSED) r += ""+velocity; else r+="_";
    r += " ";
    if(instrument != UNUSED) r += ""+instrument;  else r+="_";
    return r +"}";	
  }
  
  /**
   * Map a String to a pitch value.
   * @param ss  note(pitch) name in string
   * @return pitch value
   * <pre>[A-G]+[\+\-][0-9]
   * letter: note name 
   * +: sharp -:flat
   * number: octave
   * "C0" -> "24"</pre>
   */
  public static int parsePitch(String ss){
    int j = 0;
    int p = UNUSED;
    if(ss != null && ss.length() > 0){
      if(ss.charAt(j) == 'C' ||ss.charAt(j) == 'c'){
       p = 24; j ++;
     } else if(ss.charAt(j) == 'D' ||ss.charAt(j) == 'd'){
       p = 26; j ++;
     } else if(ss.charAt(j) == 'E' ||ss.charAt(j) == 'e'){
       p = 28; j ++;
     } else if(ss.charAt(j) == 'F' ||ss.charAt(j) == 'f'){
       p = 29; j ++;
     } else if(ss.charAt(j) == 'G' ||ss.charAt(j) == 'g'){
       p = 31; j ++;
     } else if(ss.charAt(j) == 'A' ||ss.charAt(j) == 'a'){
       p = 33; j ++;
     } else if(ss.charAt(j) == 'B' ||ss.charAt(j) == 'b'){
       p = 35; j ++;
     } else {
       p = Integer.valueOf(ss).intValue();
     }
   }
   if(j > 0 && ss.length() >= j){
     if(ss.charAt(j) == '-'){
       p--; j++;
     } else if(ss.charAt(j) == '+'){
       p++; j++;
     }
     if(ss.length() >= j){
       p += 12* Integer.valueOf(ss.substring(j)).intValue();
     }
   }
   return p;
 }
 public static Vector sort(Vector v){
   Vector r = new Vector();
   for(Enumeration e = v.elements(); e.hasMoreElements();){
     addByStart((DataNote) e.nextElement(), r);
   }
   return r;
 }
 public static void addByStart(DataNote n, Vector v){
   for(int i = v.size() -1; i >= 0; i--){
     DataNote nn = (DataNote)v.elementAt(i);
     if(nn.getStart() < n.getStart()||
	(nn.getStart() == n.getStart() && nn.pitch < n.pitch)||
	(nn.getStart() == n.getStart() && nn.pitch == n.pitch && nn.velocity < n.velocity)){
       v.insertElementAt(n, i+1);
       return;
     }
   }
   v.insertElementAt(n, 0);
//   v.addElement(n);
 }
public void setVelocity(int v){
  velocity = v;
}
public int getVelocity(){
  return velocity;
}
public void setPitch(int p){
  pitch = p;
}
public int getPitch(){
  return pitch;
}
	public void visit(double t, Visitor v, int i){
		System.out.println("DataNote.visit:"+t+", "+i);
		if(i > 0 || i <= DEPTHINF){
			v.process(this, t);
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
