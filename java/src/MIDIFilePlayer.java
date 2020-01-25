package com.cwbp.mdpw;
import java.util.*;
import javax.sound.midi.*;
/*
 * MIDIPlayer
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
public class MIDIFilePlayer  extends MIDIPlayer {
  String file;
  public void init(String s){
    file = s;
  }
  public synchronized int end(){
    try {
      Sequence sequence = new Sequence(Sequence.PPQ, TICKS);
      Track track = sequence.createTrack();
    //set tempo
      MetaMessage mmessage = new MetaMessage();
      int l = 60*1000000/tempo;
      mmessage.setMessage(0x51, 
			  new byte[]{(byte)(l/65536), (byte)(l%65536/256), 
				       (byte)(l%256)}, 
			  3);
      track.add(new MidiEvent(mmessage, 0));
      for(Enumeration e = queue.elements(); e.hasMoreElements();){
	addMessageToTrack((Vector)e.nextElement(), track);
      }
      try {
        String wd = System.getProperty("mdpw.workingdir");
//	System.err.println("test property:"+wd);
        if(wd == null)	MidiSystem.write(sequence, 0,new java.io.File(file));
        else MidiSystem.write(sequence, 0,new java.io.File(wd, file));
      } catch(Exception ex){
	System.err.println("MIDIFilePlayer Can not open file:"+file);
	System.err.println(ex.toString());
      }
    } catch (Exception exc){
      System.err.println("MIDIFilePlayer: Fatal");
      exc.printStackTrace();
    }
    return 0;
  }
}
