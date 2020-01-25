package com.cwbp.mdpw;
/**
 * Player interface
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
import java.util.*;
public interface Player {
   void init(String s);
   void setTempo(int t);
   /**
    * Block thread until end of play.
    */
   int end();
   void play(Data r);
}
