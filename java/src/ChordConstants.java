package com.cwbp.mdpw;
public interface ChordConstants {
 static final int[][] CHORDS = {
  {0, 4, 7},    //c_MAJOR  
  {0, 3, 7},    //c_m 
  {0, 5, 7},    //c_sus4
  {0, 4, 8},     //c_aug
  {0, 4, 7, 11},//cM7
  {0, 3, 7, 10},//c_m7
  {0, 4, 7, 9}, //c_6  
  {0, 4, 7, 10},//c_7
  {0, 3, 7, 11},//c_mM7
  {0, 3, 6, 9},  //c_dim
  {0, 3, 7, 9} //c_m6
 };
 public static final int _MAJOR = 0;
 public static final int _m = 1;
 public static final int _sus4 = 2;
 public static final int _aug = 3;
 public static final int _M7 = 4;
 public static final int _m7 = 5;
 //  6 means 6th
 public static final int _6 = 6;
 //  7 means 7th
 public static final int _7 = 7;
 public static final int _mM7 = 8; 
 public static final int _dim = 9;
 public static final int _m6 = 10;
}
