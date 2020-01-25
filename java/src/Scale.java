package com.cwbp.mdpw;
public class Scale 
implements ScaleConstants {
  int base = 0;
  int type = 1;
  public static int getPitch(int i, int t){
    int[] s = SCALES[t];
    int p = 0;
    while(i < 0){
      i += s.length;
      p -= 12;
    }
    while(i >= s.length){
      i -= s.length;
      p += 12;
    }
    return p+s[i];
  }
  public int getPitch(int i){
    return getPitch(i, type)+base;
  }
  public int[] getPitch(int[] i){
    int [] r = new int[i.length];
    for(int j = 0; j < i.length; j ++){
      r[j] = getPitch(i[j], type)+base;
    }
    return r;
  }
  public Scale(int b, int t){
    base = b; type = t;   
  }

public DataChord chordByName(int t, int b){
  return chordByName(t, b, DataNote.UNUSED, DataNote.UNUSED, DataNote.UNUSED);
}
public DataChord chordByName(int t, int b, double d){
  return chordByName(t, b, d, DataNote.UNUSED, DataNote.UNUSED);
}
public DataChord chordByName(int t, int b, double d, int v){
  return chordByName(t, b, d, v, DataNote.UNUSED);
}
public DataChord chordByName(int t, int b, double d, int v, int i){
  return ChordGenerator.generate(t, getPitch(b), d, v, i);
}

public DataChord chordByNumber(int t, int b){
  return chordByNumber(t, b, DataNote.UNUSED, DataNote.UNUSED, DataNote.UNUSED);
}
public DataChord chordByNumber(int t, int b, double d){
  return chordByNumber(t, b, d, DataNote.UNUSED, DataNote.UNUSED);
}
public DataChord chordByNumber(int t, int b, double d, int v){
  return chordByNumber(t, b, d, v, DataNote.UNUSED);
}
public DataChord chordByNumber(int t, int b, double d, int v, int i){
  int[] ps = new int[t];
  for(int j = 0; j < t; j++){
    ps[j] = getPitch(j*2);
  }
  return DataGenerator.chord(ps, d, v, i);
}
}
