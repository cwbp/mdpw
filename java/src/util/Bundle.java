package com.cwbp.mdpw.util;
import java.util.*;
public class Bundle  {
  protected Vector bundles = new Vector();
  String baseName = null;
  public Bundle(String s){
    baseName = s;
  }
  public Bundle(String s, Locale l1, Locale l2){
    baseName = s;    add(l1);    add(l2);
  }
  public String searchString(String s) throws Exception {
    Exception exc = null;
    for(Enumeration e = bundles.elements(); e.hasMoreElements();){
      try {
	return ((ResourceBundle)e.nextElement()).getString(s);
      } catch(Exception ex) {
	exc = ex;
      }
    }
    throw(exc);
  }
  public String getString(String s){
    Exception exc = null;
    for(Enumeration e = bundles.elements(); e.hasMoreElements();){
      try {
	return ((ResourceBundle)e.nextElement()).getString(s);
      } catch(Exception ex) {      }
    }
    return s;
  }
  public void add(Locale locale) {
    bundles.addElement(ResourceBundle.getBundle (baseName, locale));
  }
}
