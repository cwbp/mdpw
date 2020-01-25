package com.cwbp.mdpw;
import java.util.*;
public class ChordGenerator
 implements ChordConstants {
public static int truncate(int op, int p1, int p2){
  int r = op;
  while(r > p2){ op -= 12;}
  while(r < p1){ op += 12;}
  return r;
}
public static void truncate(Data d, int p1, int p2){
  try {
    for(Enumeration e = ((Vector)d).elements(); e.hasMoreElements();){
      try {
	truncate((Data)e.nextElement(), p1, p2);
      } catch(ClassCastException ex) {}
    }
  } catch(ClassCastException exp) {
    try {
      DataNote dn = (DataNote)d;
      if(dn.pitch > 0) dn.pitch = truncate(dn.pitch, p1, p2);
    } catch(ClassCastException ex) {}
  }
}
 public static DataChord generate(int t, int p){ 
   return generate(t, p, DataNote.UNUSED, DataNote.UNUSED, DataNote.UNUSED);
 }
 public static DataChord generate(int t, int p, double d){ 
   return generate(t, p, d, DataNote.UNUSED, DataNote.UNUSED);
 }
 public static DataChord generate(int t, int p, double d, int v){ 
   return generate(t, p, d, v, DataNote.UNUSED);
 }
 public static DataChord generate(int t, int p, double d, int v, int in){ 
   DataChord r = new DataChord();
   if(t < 0 ||  CHORDS.length <= t) {
     throw(new InvalidChordTypeException());
   } else {
     DataNote n = new DataNote(p, d, v, in);
     r.addElement(n);
     for(int i = 1; i < CHORDS[t].length; i++){
       r.addElement(new DataNote(p+CHORDS[t][i], DataNote.UNUSED,  
                                 DataNote.UNUSED, DataNote.UNUSED));
     }
   }
   return r;
 } 
public static DataSequence alpegio(DataChord c, int[] p){
  DataSequence r = new DataSequence();
  for(int i = 0; i < p.length; i++){
    r.addElement(c.elementAt(p[i]));
  }
  return r;
}
}
/* public static DataChord generateT(int t, int p, int p2){ 
   return generateT(t, p, p2, DataNote.UNUSED, DataNote.UNUSED, DataNote.UNUSED);
 }
 public static DataChord generateT(int t, int p, int p2, double d){ 
   return generateT(t, p, p2, d, DataNote.UNUSED, DataNote.UNUSED);
 }
 public static DataChord generateT(int t, int p, int p2, double d, int v){ 
   return generateT(t, p, p2, d, v, DataNote.UNUSED);
 }
 public static DataChord generateT(int t, int p, int p2, double d, int v, int in){ 
   DataChord r = new DataChord();
   if(t < 0 ||  CHORDS.length <= t) {
   } else {
     int pb = p;
     while (pb > p2){ pb -= 12;}
     int j = 0;
     while(pb+CHORDS[t][j] < p2){j++;}
     DataNote n = new DataNote(pb+CHORDS[t][j], d, v, in);
     r.addElement(n);
     for(int i = j+1; i < CHORDS[t].length; i++){
       r.addElement(new DataNote(pb+CHORDS[t][i], DataNote.UNUSED,  
                                 DataNote.UNUSED, DataNote.UNUSED));
     }
     for(int i = 0; i < j &&  i < CHORDS[t].length; i++){
       r.addElement(new DataNote(pb+12+CHORDS[t][i], DataNote.UNUSED,  
                                 DataNote.UNUSED, DataNote.UNUSED));
     }
   }
   return r;
 } 
*/
