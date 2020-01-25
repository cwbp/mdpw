package com.cwbp.mdpw;
import java.util.*;
/*
 * Visitor
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
public interface Visitor {
	public void process(DataNote n, double t);
}