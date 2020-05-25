package com.cwbp.mdpw.tcl;
import tcl.lang.*;

/**
 * Tcl extension package for MDPW play
 * Player interface
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */

public class PlayExtension extends Extension {
    /*
     * Create all the commands in the stopwatch package.
     */
    public void init(Interp interp) {
	interp.createCommand("play", new PlayCmd());
    }
}

