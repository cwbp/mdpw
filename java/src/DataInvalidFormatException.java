package com.cwbp.mdpw;
/**
 * Exception for invalid format of data  
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
class DataInvalidFormatException extends Exception {
  public DataInvalidFormatException(String s){
    super(s);
  }
}
