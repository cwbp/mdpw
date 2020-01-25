package com.cwbp.mdpw.util;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.sound.midi.*;
public class PlayCommand {
   
   public static void main(String[] args){
    Sequencer sequencer = null;
    if(sequencer ==  null){
       try {
         sequencer = MidiSystem.getSequencer();
         sequencer.open();
         sequencer.addMetaEventListener(new Listener(sequencer));
       } catch(Exception e){
         System.err.println("Fail in MIDISystem initilization");
         return;
       }
    }
    String s = args[0];
      try {
        sequencer.setSequence(MidiSystem.getSequence(new File(s)));
      } catch(Exception e){
        System.err.println("Fail in opening MIDI file:"+s);
      }
      sequencer.start();
  }
  public static final int END_OF_TRACK_MESSAGE = 47;
  static class Listener implements MetaEventListener {
   Sequencer sequencer;
   public Listener( Sequencer sequencer){
     this.sequencer = sequencer;
   }
   public void meta(MetaMessage event) {
    if (event.getType() == END_OF_TRACK_MESSAGE) {
      if (sequencer != null ) {
        sequencer.close();
      }
    }
  }
  }
}
