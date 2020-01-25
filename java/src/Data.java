package com.cwbp.mdpw;
/**
 * Define Interfaces of MDPW data classes
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2008, Sho Hosoda All Rights Reserved
 */
public interface Data extends Cloneable{
  public static final int UNUSED = -1;
  public static final int DEPTHINF = -2;
 /**
  * Get duration in note length.
  */
 double getDuration();
 /**
  * Set Volume value
  */
// void setVolume(int v);
 /**
  * Do visitor
  */
	void visit(Visitor v);
	void visit(double time, Visitor v);
 	void visit(Visitor v, int depth);
	void visit(double time, Visitor v, int depth);
 /**
  * Get Volume value 
  */
// int getVolume();
 /**
  * Deep copy
  */
 Data copy();
 /**
  * Get String in tcl format 
  */
 String getString();
 /**
  * trasform data to just set of DataNote.
  * @param i absolute timestamp
  * @param n note date for keeping former note values.(will change)
  * @return set of notes.
  */
 java.util.Vector toNotes(double i, DataNote n);
}
