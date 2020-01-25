package com.cwbp.mdpw;
import java.util.*;
import javax.sound.midi.*;
/*
 * MIDIPlayer
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
public abstract class MIDIPlayer implements Player {
  static int CHANNELS = 9;
  static int PRGCHNG = 255;
  static final int PERCUSSION = 129;
  static final int PERCUSSIONCHANNEL = 9;
  protected static final int TICKS = 96;
  Vector queue = new Vector();
  int tempo = 120;
  Sequencer sequencer;
  public void setTempo(int t){
    tempo = t;
  }
   private static DataBinary noteoff(DataNote n){
     byte[] b = {(byte)(ShortMessage.NOTE_OFF|n.getInstrument()),
		 (byte)n.pitch,1};
     DataBinary r =new DataBinary(b);
     r.setStart(n.getStart()+n.getDuration());
     return r;
   }
  /**
   * Create Program Change
   * @param i instrument no.
   * @param j channel
   * @param s time stamp
   * @return Program Change DataBinary
   */     
   private static DataBinary prgchng(int i,int j, double s){
     byte[] b = {(byte)(ShortMessage.PROGRAM_CHANGE|j), (byte)i};
     DataBinary r = new DataBinary(b);
     r.setStart(s);
     return r;
   }
  double now = 0;
  public void resetNow(){
    now = 0;
  }
  public synchronized void play(Data r){
    queue.addElement(convert(r, now));
    now += r.getDuration(); 
  }
  protected static Vector convert(Data r, double nowStamp){
    Vector ns = DataNote.sort(r.toNotes(nowStamp ,new DataNote(0,0,0,0)));
    /*    System.err.println("Duration:"+ r.getDuration()); 
    for(Enumeration e = ns.elements(); e.hasMoreElements();){
      System.err.println(((DataNote)e.nextElement()).getString());;
      }*/
    //音色でチャンネルに分割しDataNote(noteonのみ)を分配
    Vector vs = new Vector();
    Hashtable cs = new Hashtable(); // 音色からチャンネル
    Hashtable noffs = new Hashtable();// チャンネルとピッチからノートオフ
    Vector q = new Vector();
    for(Enumeration e = ns.elements(); e.hasMoreElements();){
      DataNote n = (DataNote)e.nextElement();
      ///System.err.println(n.getString());
	int ci = n.getInstrument(); //音色
	if(n.pitch != DataNote.BINARY){
	  if(ci == PERCUSSION){
	    n.setInstrument(PERCUSSIONCHANNEL);
	  } else {
	    Integer ni = (Integer)cs.get(new Integer(ci)); //channel
	    if(ni == null){ //音色に対応するチャンネルがない
	      if(cs.size() > CHANNELS -1){
		ni = (Integer)q.firstElement();
		cs.remove(ni);
	      } else {
		ni = new Integer(cs.size());
	      }
	      cs.put(new Integer(ci), ni);
	      /*
               * if you want more than one channels have same insturument
               * Use instument no + 256*n (n :int)
               */
	      vs.addElement(prgchng(ci,(ni.intValue())%256,n.getStart()));
	    } 
	    n.setInstrument(ni.intValue());
	    q.removeElement(ni); //使った音色は後ろに下げる
	    q.addElement(ni);
	  }
	  DataBinary no = noteoff(n);
	  Integer nok = new Integer(no.getBinary()[0]*256+no.getBinary()[1]);
	  Double nov = (Double)noffs.get(nok);
	  if(nov != null && nov.doubleValue() <= n.getStart()){
	    nov = null;
	  }
	  if(nov == null){
	    vs.addElement(no);
	    noffs.put(nok, new Double(no.getStart()));
	  }
	  if(nov != null && nov.doubleValue() < no.getStart()){
	    for(Enumeration vse = vs.elements(); vse.hasMoreElements();){
	      DataNote vsen = (DataNote)vse.nextElement();
	      if(vsen.getStart() == nov.doubleValue() && 
		 vsen.pitch == DataNote.BINARY){
		DataBinary vseb = (DataBinary)vsen;
		if(no.getBinary()[0] == vseb.getBinary()[0] && 
		   no.getBinary()[1] == vseb.getBinary()[1]){
                  vs.remove(vseb);
		}
	      }
	    }
	    vs.addElement(no);
	    noffs.put(nok, new Double(no.getStart()));
	  }
	}
	vs.addElement(n);
    }
    return DataNote.sort(vs);
  }
  public static void addMessageToTrack(Data r, Track track, double stamp){
    addMessageToTrack(convert(r, stamp), track);
  }
  protected static void addMessageToTrack(Vector v, Track track){
    for(Enumeration en = v.elements(); en.hasMoreElements();){
      DataNote n = (DataNote)en.nextElement();
      try {
	if(n.pitch == DataNote.BINARY){
	  ShortMessage mm = new ShortMessage();
	  byte[] bd = ((DataBinary)n).getBinary();
	  if(bd.length == 1){
	    mm.setMessage(bd[0]);
	  } else if(bd.length > 0) {
	    int b0 = bd[0];
	    int b1 = bd[1];
	    int b2 = 0;
	    if(bd.length > 2) b2 = bd[2];
	    mm.setMessage(b0, b1, b2);
	  }
	  track.add(new MidiEvent(mm, (int)(n.getStart()*TICKS)));
	} else {
	  ShortMessage sm = new ShortMessage();
	  sm.setMessage(ShortMessage.NOTE_ON, n.instrument,
			n.pitch, n.velocity);
	  track.add(new MidiEvent(sm,  (int)(n.getStart()*TICKS)));
	}
      } catch (Exception ex){
	System.err.println(ex.toString());
	System.err.println("MIDIFilePlayer:"+n.getString()); 
      }
    }
  }
}
