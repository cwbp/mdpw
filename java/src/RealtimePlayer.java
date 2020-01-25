package com.cwbp.mdpw;
/**
 * Player in Real time (with Thread)
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999, Sho Hosoda
 */
import java.util.*;
abstract class RealtimePlayer extends Thread 
  implements Player {
  abstract public void playNote(DataNote n);
  public void init(String s){
    start();
  }
  int tempo = 120;
  double noteLength = 60000 / tempo;
  double duration;
  Vector queue = new Vector();
  public void setTempo(int t){
    tempo = t;
    noteLength = 60000 / tempo;
  }
  public int getTempo(){
    return tempo;
  }
  public int getDuration(double d){
    return (int)(noteLength*d);
  }
  public synchronized int end(){
    return getSizeSyn();
  }
  public synchronized int getSizeSyn(){
    return queue.size();
  }
  public synchronized void play(Data r){
    //    System.out.println("play1:"+queue.size());
    queue.addElement(DataNote.sort(r.toNotes(0,new DataNote(0,0,0,0))));
    notify();
  }
  public void run(){
    boolean loop = true;
    while(loop){
      try {
//	System.out.println("run:"+queue.size());
	Enumeration e = null;
	synchronized (this){
	  if(queue.size() == 0){
	    //	  System.out.println("wait");
	    wait();
	  } else {
	    //	  System.out.println("Player.start play");
	    e = ((Vector)queue.firstElement()).elements();
	    queue.removeElementAt(0);
	  }
	}
	if(e != null){
	  double t = 0;
	  DataNote fn = (new DataNote(DataNote.UNUSED,
				      DataNote.UNUSED,
				      DataNote.UNUSED,
				      DataNote.UNUSED));
	  while(e.hasMoreElements()){
	    DataNote n = (DataNote)e.nextElement();
	    double d;
	    if((d = n.getStart() - fn.getStart()) > 0){
	      //	      System.out.println("Player.sleep:"+d);
	      sleep(getDuration(d));
	    }
	    playNote(n);
	    fn = n;
	  }
	  //	  System.out.println("Player.sleep:"+fn.getDuration());
	  sleep(getDuration(fn.getDuration()));
	}
	//	System.out.println("end");
      } catch(Exception ex){
	System.out.println("except");
	ex.printStackTrace();
      }
    }
  }
}
