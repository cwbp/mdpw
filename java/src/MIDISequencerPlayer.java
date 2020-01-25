package com.cwbp.mdpw;
import java.util.*;
import javax.sound.midi.*;
/*
 * MIDIPlayer
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
public class MIDISequencerPlayer extends MIDIPlayer 
implements Runnable, MetaEventListener {
  Thread thread = null;
  static private Sequencer sequencer;
  static {
   try {
     sequencer = MidiSystem.getSequencer();
     sequencer.open();
   } catch(MidiUnavailableException e){
     System.err.println("MIDISequencerPlayer initialize failed");
//     e.printStackTrace();
     System.err.println(e.toString());
   }
  }
  public MIDISequencerPlayer(){  
    sequencer.addMetaEventListener(this);
  }
  public void init(String s){  }
  public synchronized int end(){
    ///    System.err.println("end1");
    synchronized(queue){} // Mutex with play
    try {
      while(queue.size() > 0 || thread != null){
	///	System.err.println("end2");
	wait();
      }
    } catch(Exception e){}
    ///System.err.println("end3");
    return 0;
  }
  public void play(Data r){
    ///System.err.println("play start");
    synchronized(queue){
      super.play(r);
      resetNow();
      if (thread == null){
        thread = new Thread(this);
        thread.start();
      }
    }
    ///System.err.println("play end");
  }
  public void run(){
   synchronized(sequencer){
    while(queue.size() > 0){
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
        addMessageToTrack((Vector)queue.firstElement(), track);
        queue.removeElementAt(0);
        if (sequencer == null){
          System.err.println("MIDISequencerPlayer initialize failed");
        } else {
///        sequencer.setTempoInBPM(tempo);
          sequencer.setSequence(sequence);
          sequencer.start();
	  /// System.err.println("started");
          if(queue.size() > 0) waitlocal();
          /// System.err.println("restarted");
          synchronized(queue){
            if(queue.size() == 0) thread = null;
	    ///System.err.println("tread == null");
          }
        }
      } catch(InvalidMidiDataException e){
         e.printStackTrace();
         System.err.println(e.toString());
      }
    }
   }
   ///System.err.println("run end");
  }
  public void meta(MetaMessage meta){
    ///System.err.println("meta");
    if(meta.getType() == 0x2f) notifylocal(); 
  }
  private synchronized void waitlocal(){
    try {
      super.wait();
    } catch(InterruptedException e){ 
      e.printStackTrace();
    }
  }
  private synchronized void notifylocal(){
    super.notify();
  }
}
