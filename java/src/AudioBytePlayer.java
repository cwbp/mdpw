package com.cwbp.mdpw;
/**
 * Player by au files for Java Application
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
 import java.util.*;
import java.io.*;
import java.net.*;
import sun.audio.*;
public class AudioBytePlayer extends RealtimePlayer {
  Hashtable audios = new Hashtable();
  int currentInstrument = 0;
  byte[][] currentAudios = null ;
  static final int AUDIOMAX = 128;
  public void playNote(DataNote n){
    int pitch = n.pitch;
    int inst = n.instrument;
//    System.err.println("play:"+n.getString());
    if(pitch == DataNote.UNUSED) return;
    if(pitch == DataNote.BINARY) return;
    if(inst != currentInstrument){
      currentInstrument = inst;
      currentAudios = (byte[][])audios.get(new Integer(inst));
    }
    if(currentAudios == null){
      currentAudios =
	(byte[][])audios.elements().nextElement();
    }
    try {
      if(currentAudios[pitch] != null)
	AudioPlayer.player.
	  start(new ByteArrayInputStream(currentAudios[pitch]));
    }catch(Exception e){}
  }
  public void init(String s){// "inst-no,start-no,audio1|..|audion[,dir]:"
    init(s, new File("."), null);
  }
  void init(String s, URL u){// "inst-no,start-no,audio1|..|audion[,dir]:"
    init(s, null, u);
  }
  void init(String s,File f,URL n){
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
	    byte[][] acs = (byte[][])audios.get(new Integer(i));
	    if(acs == null)
	      acs = new byte[AUDIOMAX][];
	    while(str.hasMoreTokens() && o < AUDIOMAX){
	      try {
		InputStream in = null;
		if(f != null){
		  String fn = str.nextToken();
//System.err.println("load as:"+o+" :" +(new File(new File(f, dir), fn)).toString());
		  in = new FileInputStream(new File(new File(f, dir), fn));
		} else 
		  in = (new URL(new URL(n, dir), str.nextToken()))
		    .openStream();
		acs[o] = new byte[in.available()];
		in.read(acs[o]);
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
