package com.cwbp.mdpw;
/**
 * Player for Stdout. For debuggin use.
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
public class StdoutPlayer extends RealtimePlayer {
  public void playNote(DataNote n){
    System.out.println((new java.util.Date()).getTime()+"::"+n.getString());
  }
}
