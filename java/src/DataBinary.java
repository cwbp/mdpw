package com.cwbp.mdpw;
/**
 * Data class for Raw binary data.
 * Music Description and Process Words
 * http://www.cwbp.com/mdpw
 * Copyright (c) 1999-2002, Sho Hosoda
 */
public class DataBinary extends DataNote {
  byte[] binary ;  
  public DataBinary(byte[] b){
    setBinary(b);
  }
  public DataBinary(int[] b){
    setBinary(b);
  }
  public DataBinary(int c){
    setBinary(new byte[]{(byte)c});
  }
  public DataBinary(int c, int d1){
    setBinary(new byte[]{(byte)c,(byte)d1});
  }
  public DataBinary(int c, int d1, int d2){
    setBinary(new byte[]{(byte)c,(byte)d1,(byte)d2});
  }
  void setBinary(byte[] b){
    pitch = DataNote.BINARY;
    binary = b;
  }
  void setBinary(int[] i){
    binary = new byte[i.length];
    for(int j = 0; j < i.length; j++){
      binary[j] = (byte)i[j];
    }
    pitch = DataNote.BINARY;
  }
  byte[] getBinary(){
    return binary;
  }
  public String getString(){
    String r = "{b";
    for(int i = 0; i < binary.length; i ++){
      int bi = binary[i];
        if (bi < 0) bi += 0x100;
        r += " 0x"+Integer.toHexString(bi);
    }
    return r +"}";
  }
}
