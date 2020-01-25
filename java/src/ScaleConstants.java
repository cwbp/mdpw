package com.cwbp.mdpw;
public interface ScaleConstants {
  public final static int P1 = 0;
  public final static int m2 = 1;
  public final static int M2 = 2;
  public final static int m3 = 3;
  public final static int M3 = 4;
  public final static int P4 = 5;
  public final static int A4 = 6;
  public final static int D4 = 6;
  public final static int P5 = 7;
  public final static int m6 = 8;
  public final static int M6 = 9;
  public final static int m7 = 10;
  public final static int M7 = 11;
  public final static int P8 = 12;
  public final static int[] MAJOR_s  = {P1, M2, M3, P4, P5, M6, M7};
  public final static int[] MINOR_s = {P1, M2, m3, P4, P5, m6, m7};
  public final static int[] MELODIC_MINOR_DOWN_s = {P1, M2, m3, P4, P5, m6, m7};
  public final static int[] MELODIC_MINOR_UP_s = {P1, M2, m3, P4, P5, M6, M7};
  public final static int[] HARMONIC_MINOR_s = {P1, M2, m3, P4, P5, m6, M7};
  final static int[][] SCALES = {MAJOR_s, MELODIC_MINOR_DOWN_s, 
                          MELODIC_MINOR_UP_s, HARMONIC_MINOR_s};
  public static final int MAJOR = 0;
  public static final int MELODIC_MINOR_DOWN = 1;
  public static final int MELODIC_MINOR_UP = 2;
  public static final int HARMONIC_MINOR = 3;
}
