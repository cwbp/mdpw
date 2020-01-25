package com.cwbp.mdpw;
/**
 * Player by au files for Java Applet
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
import java.util.*;
import java.io.*;
import java.net.*;
import java.applet.*;
public class AudioClipPlayer extends RealtimePlayer {
  Hashtable audios = new Hashtable();
  int currentInstrument = 0;
  AudioClip[] currentAudios = null ;
  static final int AUDIOMAX = 128;
  public void playNote(DataNote n){
    int pitch = n.pitch;
    int inst = n.instrument;
//    System.err.println("play:"+n.getString());
    if(pitch == DataNote.UNUSED) return;
    if(pitch == DataNote.BINARY) return;
    if(inst != currentInstrument){
      currentInstrument = inst;
      currentAudios = (AudioClip[])audios.get(new Integer(inst));
    }
    if(currentAudios == null){
      currentAudios =
	(AudioClip[])audios.elements().nextElement();
    }
    try {
      if(currentAudios[pitch] != null)
	currentAudios[pitch].play();
    }catch(Exception e){}
  }
  public void init(String s){// "inst-no,start-no,audio1|..|audion[,dir]:"
    System.err.println("You can not initilize by this method");
  }
  void init(String s, Applet a){// "inst-no,start-no,audio1|..|audion[,dir]:"
    StringTokenizer ss = new StringTokenizer(s, ":");
    while(ss.hasMoreElements()){
      StringTokenizer st = new StringTokenizer(ss.nextToken(), ",");
      if(st.hasMoreElements()){
	int i = Integer.valueOf(st.nextToken()).intValue();
	if(st.hasMoreElements()){
          int o = Integer.valueOf(st.nextToken()).intValue();
	  if(st.hasMoreElements()){
	    StringTokenizer str = new StringTokenizer(st.nextToken(), "|");
	    String dir = ".";
	    if(st.hasMoreTokens()){
	      dir = st.nextToken();
	    }
	    AudioClip[] acs = (AudioClip[])audios.get(new Integer(i));
	    if(acs == null)
	      acs = new AudioClip[AUDIOMAX];
	    while(str.hasMoreTokens() && o < AUDIOMAX){
	      try {
		acs[o] = a.getAudioClip(new URL(new URL(a.getCodeBase(),
							dir),
						 str.nextToken()));
	      } catch(Exception e){
		System.err.println("Exception in loading the audio note no:"
				   +o);
		System.err.println(e.toString());
	      }
	      o++;
	    }
	    audios.put(new Integer(i), acs);
	  }
	}
      }
    }
    start();
  }
}
