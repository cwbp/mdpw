package com.cwbp.mdpw.util;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.sound.midi.*;
public class MShell extends JShell {
  static ClassLoader CompilerLoader = null;
  private static final String CompilerClassName = "sun.tools.javac.Main"; 
  private static final String ToolsJar = "lib/tools.jar"; 
  static {
    commandTable.put("inrun", 
		     new String[]{null, "inRunCommand"});
    commandTable.put("ir", 
		     new String[]{null, "inRunCommand"});
    commandTable.put("incompile", 
		     new String[]{null, "inCompileCommand"});
    commandTable.put("ic", 
		     new String[]{null, "inCompileCommand"});
    commandTable.put("play", 
		     new String[]{null, "playCommand"});
    commandTable.put("p", 
		     new String[]{null, "playCommand"});
    DefaultShellClass = "com.cwbp.mdpw.util.MShell";
  }
  protected void setOption(ShellOut so){
    super.setOption(so);
    try {
      try {
        CompilerLoader = Class.forName(CompilerClassName).getClassLoader();
        errprintln("Built-in compile may work by System");
      } catch(Exception e){
        File jt = findJar();
        if(jt != null){
          errprintln("Built-in compile may work by Jar :"+jt.toString());
          CompilerLoader = new URLClassLoader(new URL[]{jt.toURL()});
          CompilerLoader.loadClass(CompilerClassName);
        } else 
          errprintln("Built-in compile may NOT work.");
      }
    } catch(Exception e){
      errprintln("Can not Find Compiler Class");
      e.printStackTrace();
    }
    try {
      CompilerLoader = new URLClassLoader(new URL[]{(new File(MyClassPath)).toURL()}, CompilerLoader );
    } catch(Exception e){
      errprintln("Can not Add Original Class Path");
    }
  }
  private Sequencer sequencer = null;
  private static File findJar(){
    File h = new File(System.getProperty("java.home"));
    File jt = new File(h, ToolsJar);
    if(jt.exists()) return jt;
    jt = new File(h, "../"+ToolsJar);
    if(jt.exists()) return jt;
    File[] rs= File.listRoots();
    Hashtable t = new Hashtable(); 
    for(int i = 0; i < rs.length; i ++){
      String dl = null;
      try {
        if(!rs[i].toString().startsWith("A")){
          File[] rs2 = rs[i].listFiles();
          for(int j = 0; (rs2 != null) && (j < rs2.length ); j ++){
            if(rs2[j].getName().startsWith("j") ||rs2[j].getName().startsWith("J")){
              File can = new File(rs2[j], ToolsJar);
              if(can.exists()) t.put(rs2[j].getName(), can);
            }
          }
        }
      } catch(Exception e){
        System.err.println("Drive:"+rs[i].toString());
//        e.printStackTrace();
      }
    }
    if(t.size() == 0) return null; 
    TreeSet tr = new TreeSet(t.keySet());
    return (File)t.get(tr.last());
  }
  private void invokeClass(ClassLoader cl, String cn, String[] as){
    try {
      try {
         cl.loadClass(cn).getMethod("main", new Class[]{as.getClass()})
         .invoke(null, new Object[]{as});
      } catch(Exception e) {
        errprintln("Error in invoking:"+cn);
        errprintln(e.toString());
        e.printStackTrace();
      }
    } catch(Exception e) {
      errprintln("Error in instatiation:"+cn);
      errprintln(e.toString());
      e.printStackTrace();
    }
  }
  private String rel2absolute(String o){
    String sep = System.getProperty("path.separator");
    StringBuffer r = new StringBuffer();
    StringTokenizer st = new StringTokenizer(o, sep);
    while(st.hasMoreTokens()){
      String s = st.nextToken();
      if(s.startsWith("/") || s.indexOf(":") == 1){
        r.append(s);
      } else {
        try {
          r.append(workingDir.getCanonicalPath()+"/"+s);
        } catch(Exception e){
          e.printStackTrace();
        }
      }
      if(st.hasMoreTokens()) r.append(sep);
    }
    return r.toString();
  }
  protected void inCompileCommand(String s){ 
    if(CompilerLoader == null){ 
      errprintln("No class for compilation. Use compile. ");
      return;
    }
    s = expandSharp(s, "/");
    StringTokenizer st = new StringTokenizer(s, " ");
    Vector a = new Vector();
    StringBuffer asb = new StringBuffer();
    boolean changeflag = false; 
    int classpathstate = 0;
    String addClasspath = "."+System.getProperty("path.separator")+MyClassPath;
    while(st.hasMoreTokens()){
      String at = st.nextToken();
      if(classpathstate == 1){
        at +=System.getProperty("path.separator")+addClasspath;
        classpathstate = 2;
      }
      if(changeflag || at.endsWith(".java")){
        at = rel2absolute(at);
      } 
      a.addElement(at);
      asb.append(at);  asb.append(" ");
      if(at.equals("-d")){
        changeflag = true;
      } else if(at.startsWith("-c")){
        changeflag = true;
        classpathstate = 1;
      } else {
        changeflag = false;
      }
    } 
    if(classpathstate < 2){
      a.addElement("-classpath");
      String ac = rel2absolute(addClasspath);
      a.addElement(ac);
      asb.append("-classpath ");  asb.append(ac);
    } 
    outprintln("Args:"+ asb);
    String[] as = new String[a.size()];
    for(int i = 0; i < as.length; i ++){
      as[i] = (String)a.elementAt(i);
    }
    try {  
      String cn = "javac";
      Class c = CompilerLoader.loadClass(CompilerClassName);
      java.lang.reflect.Constructor co = c.getConstructor(new Class[]{Class.forName("java.io.OutputStream"), cn.getClass()}) ;
      Object o = co.newInstance(new Object[]{System.out, cn});
      java.lang.reflect.Method m = o.getClass().getDeclaredMethod("compile", new Class[]{as.getClass()});
      if(((Boolean)m.invoke(o, new Object[]{as})).booleanValue()) errprintln("Compile success");
      else  errprintln("Compile FAIL");
     } catch(Exception e) {
      errprintln("Error incompile:"+s);
      e.printStackTrace();
    }
  }
  ClassLoader cl = null;
  protected void inRunCommand(String s){ 
    s = expandSharp(s, ".");
    StringTokenizer st = new StringTokenizer(s, " ");
    if(st.hasMoreTokens()){
       String cn = st.nextToken();
       Vector a = new Vector();
       while(st.hasMoreTokens()){
         a.addElement(st.nextToken());
       }
       String[] as = new String[a.size()];
       for(int i = 0; i < as.length; i ++){
         as[i] = (String)a.elementAt(i);
       } 
      try {
        invokeClass(new URLClassLoader(new URL[]{workingURL()}),cn, as);
      } catch(Exception e) {
        errprintln("Error inrun:"+s);
        e.printStackTrace();
      }
    } else {
      errprintln("Need class name");
    }
  }
  private URL workingURL() throws Exception {
    return (new File(workingDir.getCanonicalPath())).toURL(); 
  }
   protected void playCommand(String s){ 
    s = expandSharp(s, "/");
    if(sequencer ==  null){
       try {
         sequencer = MidiSystem.getSequencer();
         sequencer.open();
       } catch(Exception e){
         errprintln("Fail in MIDISystem initilization");
         errprintln(e.toString());
         return;
       } 
    }
    try {
      sequencer.setSequence(MidiSystem.getSequence(new File(workingDir, s)));
    } catch(Exception e){
      errprintln("Fail in opening MIDI file:"+s);
      errprintln(e.toString());
    } 
    outprintln("play "+s);
    sequencer.start();
  }
  protected void exitCommand(String s){
    if(sequencer != null){
      sequencer.stop();
      sequencer.close();
      sequencer = null;
    }
    super.exitCommand(s);
  }
  public static void main(String[] args){
    try {
      JShell shell = (JShell)Class.forName(DefaultShellClass).newInstance();
      shell.mainLoop(null);
    }  catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
    System.exit(0);
  }

}
