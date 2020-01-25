package com.cwbp.mdpw;
import java.util.*;
public class DataGenerator {
public static DataSequence sequence(int[] ps){
  return sequence(ps, DataNote.UNUSED, DataNote.UNUSED, DataNote.UNUSED);
}
public static DataSequence sequence(int[] ps, double d){
  return sequence(ps, d, DataNote.UNUSED, DataNote.UNUSED);
}
public static DataSequence sequence(int[] ps, double d, int v){
  return sequence(ps, d, v, DataNote.UNUSED);
}
public static DataSequence sequence(int[] ps, double d, int v, int i){
  double[][] w = new double[ps.length][];
  for(int j =0; j < ps.length; j++){
    w[j] = new double[]{ps[j], d, v, i};
  } 
  return sequence(w);
}
public static DataSequence sequence(double[][] vs){
  DataSequence r = new DataSequence();
  for(int j =0; j < vs.length; j++){
    double[] v = vs[j];
    switch(v.length){
      case 1:
        r.addElement(new DataNote((int)v[0], DataNote.UNUSED));
      break;
      case 2:
        r.addElement(new DataNote((int)v[0], v[1]));
      break;
      case 3:
        r.addElement(new DataNote((int)v[0], v[1], (int)v[2]));
      break;
      case 4:
        r.addElement(new DataNote((int)v[0], v[1], (int)v[2], (int)v[3]));
    } 
  }
 return r;
}
public static DataChord chord(int[] ps){
  return chord(ps, DataNote.UNUSED, DataNote.UNUSED, DataNote.UNUSED);
}
public static DataChord chord(int[] ps, double d){
  return chord(ps, d, DataNote.UNUSED, DataNote.UNUSED);
}
public static DataChord chord(int[] ps, double d, int v){
  return chord(ps, d, v, DataNote.UNUSED);
}
public static DataChord chord(int[] ps, double d, int v, int i){
  double[][] w = new double[ps.length][];
  for(int j =0; j < ps.length; j++){
    w[j] = new double[]{ps[j], d, v, i};
  } 
  return chord(w);
}
public static DataChord chord(double[][] vs){
  DataChord r = new DataChord();
  for(int j =0; j < vs.length; j++){
    double[] v = vs[j];
    switch(v.length){
      case 1:
        r.addElement(new DataNote((int)v[0], DataNote.UNUSED));
      break;
      case 2:
        r.addElement(new DataNote((int)v[0], v[1]));
      break;
      case 3:
        r.addElement(new DataNote((int)v[0], v[1], (int)v[2]));
      break;
      case 4:
        r.addElement(new DataNote((int)v[0], v[1], (int)v[2], (int)v[3]));
    } 
  }
 return r;
}
  public static DataChord c(){
    return new DataChord();
  }
  public static DataChord chord(){
    return new DataChord();
  }
  public static DataSequence s(){
    return new DataSequence();
  }
  public static DataSequence sequence(){
    return new DataSequence();
  }
  public static DataNote n(int p){
    return new DataNote(p);
  }
  public static DataNote n(int p, double d){
    return new DataNote(p,d);
  }
  public static DataNote n(int p, double d, int v){
    return new DataNote(p, d, v);
  }
  public static DataNote n(int p, double d, int v, int i){
    return new DataNote(p, d, v, i);
  }
  public static DataNote note(int p){
    return new DataNote(p);
  }
  public static DataNote note(int p, double d){
    return new DataNote(p,d);
  }
  public static DataNote note(int p, double d, int v){
    return new DataNote(p, d, v);
  }
  public static DataNote note(int p, double d, int v, int i){
    return new DataNote(p, d, v, i);
  }
  public static DataNote r(double d){
    return new DataNote(0, d, DataNote.VREST);
  }
  public static DataNote rest(double d){
    return new DataNote(0, d, DataNote.VREST);
  }

}
