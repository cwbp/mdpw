package com.cwbp.mdpw.util;
import java.io.*;
import java.util.*;
import java.text.*;
public class JShell {
  protected static Hashtable commandTable = new Hashtable();
  private static final int COMCLASS = 0;
  private static final int COMMETHOD = 1;
  protected boolean loopFlag = true;
  static String PROMPT = ">";
  static private final String JAVAEXT = ".java";
  static private final String CLASSEXT = ".class";
  private Hashtable commandHistory = new Hashtable();
  private int historyNumber = 0;
  protected static String DefaultShellClass;
  private static final String resourceName = "com.cwbp.mdpw.resources.messages";
  private Bundle bundle = new Bundle(resourceName, Locale.getDefault(), Locale.US);
  /**
   *コマンドとして登録するためには
   引数なしのコンストラクタをもつくらすに
   引数Stringのmethodをつくってテーブルに登録
   * 0,0 クラス名(nullはこのクラス)
   * 0,1 メソッド名
   * 1,n へルプ
   */
  static {
    commandTable.put("source", 
		     new String[]{null, "sourceCommand"});
    commandTable.put("exec", 
		     new String[]{null, "execCommand"});
    commandTable.put("!", 
		     new String[]{null, "doHistoryCommand"});
    commandTable.put("help", 
		     new String[]{null, "showHelp"});
    commandTable.put("exit", 
		     new String[]{null, "exitCommand"});
    commandTable.put("history", 
		     new String[]{null, "showHistory"});
    commandTable.put("h", 
		     new String[]{null, "showHistory"});
    commandTable.put("compile", 
		     new String[]{null, "compileCommand"});
    commandTable.put("javac", 
		     new String[]{null, "compileCommand"});
    commandTable.put("c", 
		     new String[]{null, "compileCommand"});
    commandTable.put("run", 
		     new String[]{null, "runCommand"});
    commandTable.put("java", 
		     new String[]{null, "runCommand"});
    commandTable.put("r", 
		     new String[]{null, "runCommand"});
    commandTable.put("cr", 
		     new String[]{null, "crCommand"});
    commandTable.put("search", 
		     new String[]{null, "searchCommand"});
    commandTable.put("s", 
		     new String[]{null, "searchCommand"});
    commandTable.put("ls", 
		     new String[]{null, "lsCommand"});
    commandTable.put("pwd", 
		     new String[]{null, "pwdCommand"});
    commandTable.put("cd", 
		     new String[]{null, "cdCommand"});
    DefaultShellClass  = "com.cwbp.mdpw.util.JShell";;
  };
  ShellOut shellOut = null;
  public static void main(String[] args){
    try {
      JShell shell = (JShell)Class.forName(DefaultShellClass).newInstance();
      shell.mainLoop(null);
    }  catch(Exception e){
      e.printStackTrace();
    }
  }
  protected void outprintln(String s){
    System.out.println(s);
  }
  protected void errprintln(String s){
    System.err.println(s);
  }
  protected void mainLoop(ShellOut so) throws IOException{
    BufferedReader input 
	= new BufferedReader(new InputStreamReader(System.in));
    setOption(so);
    String l =  " ";
    while(loopFlag){
	for(StringTokenizer st = new StringTokenizer(l, ";");
	    st.hasMoreTokens();){
	  String sl = st.nextToken();
	  oneLine(sl);
	  if(!loopFlag) return ;
	}
        System.out.print(PROMPT);
        System.out.flush();
	if((l = input.readLine()) == null) return;
	putHistory(l);
      }
  }
  protected String MyClassPath = "";
  protected void setOption(ShellOut so){
    shellOut = so;
    try {
      StringTokenizer cs = new StringTokenizer(this.getClass().getName(), ".");
      StringBuffer cn = new StringBuffer();
      while(cs.hasMoreTokens()){
        cn.append(cs.nextToken());
        if(cs.hasMoreTokens()) cn.append("/");
      }
      cn.append(CLASSEXT);
//      outprintln(cn.toString());
      MyClassPath = this.getClass().getClassLoader().getSystemResource(cn.toString()).toString();
      MyClassPath = MyClassPath.substring(MyClassPath.indexOf("file:")+5,
                                          MyClassPath.indexOf(cn.toString()));
      int mi = MyClassPath.indexOf("!");
      if(mi > -1) MyClassPath = MyClassPath.substring(0, mi);
    } catch(Exception e){
      e.printStackTrace();
    }
    outprintln("MyClassPath:"+MyClassPath);
  }
  protected boolean oneLine(String l) {
    if(l.startsWith("!")) l = "! "+l.substring(1);
    StringTokenizer lf = new StringTokenizer(l, " \t");
    String cb = null;
    StringBuffer ca = new StringBuffer("");
    if(lf.hasMoreTokens()){
      cb = lf.nextToken();
      while(lf.hasMoreTokens()){
	ca.append(lf.nextToken());
	if(lf.hasMoreTokens()){
	  ca.append(" ");
	}
      }
    }
    if(cb != null){
      String[] cba= (String[]) commandTable.get(cb);
      if(cba != null){
	Object o = this;
	java.lang.reflect.Method m = null;                   
	try {
	  if(cba[COMCLASS] != null){
	    o = Class.forName(cba[COMCLASS])
	      .newInstance();
	  }
	  Class c = o.getClass();
	  do {
	    //outprintln("Cl:"+c.getName());
	    try {
	      m = c.getDeclaredMethod(cba[COMMETHOD],
				      new Class[]{cb.getClass()});
	    } catch(Exception e){ }
	    c = c.getSuperclass();
	  } while(m == null && !c.getName().equals("java.lang.Object"));
	} catch(Exception e){ e.printStackTrace();}
	if(m != null) {
	  try {
	    m.invoke(o, new Object[]{ca.toString()});
	  } catch(Exception e){ e.printStackTrace();}
	  return true;
	} else {
	  errprintln(cb+": Installation Error.");
	}
      } else {
	errprintln(cb+": Command not Found.");
	return false;
      }
    }
    return true;
  }
  protected void exitCommand(String s){
    loopFlag = false;
  }
  private void showHistory(String s){
    int l = 20;
    try {
      if(s.length() > 0) l = Integer.valueOf(s).intValue();
    } catch(Exception e){
      errprintln("history invalid arg:"+s+". Use default");
    }
    for(int i = historyNumber -l; i <= historyNumber; i ++){
      if(i > 0){
        String st = getHistory(new Integer(i));
        if(st != null)  outprintln("!"+i+": "+st);
      } 
    } 
  }
  private void showHelp(String s){
    try {
      StringTokenizer st = new StringTokenizer(s, " \t");
      if(st.hasMoreTokens()){
	while(st.hasMoreTokens()){
	  String com = st.nextToken();
	  if(commandTable.get(com) == null){
	    outprintln(com +": "+bundle.searchString("help.nocommand"));
	    return;
	  }
	  String rb = "help."+com+".";
	  try {
	    outprintln(com +": "+bundle.searchString(rb+"0"));
	    try {
	      for(int i = 1;true; i++){
		outprintln(bundle.searchString(rb+i));
	      }
	    } catch(Exception e){
	    }
	  } catch(Exception e){
	    outprintln(com +": " + bundle.searchString("help.notfound"));
	  } 
	}
      } else {
	errprintln("Command List");
	int i = 1;
	StringBuffer l = new StringBuffer();
	for(Iterator k = (new TreeSet(commandTable.keySet())).iterator() ;k.hasNext(); i++){
	  String com = (String)k.next();
	  l.append(com);
	  l.append("\t");
	  if(i%4 == 0){
	    outprintln(l.toString());
	    l = new StringBuffer();
	  }
	}
	if(i%4 != 1) outprintln(l.toString());
      }
    } catch(Exception e){}
  }
  private void doHistoryCommand(String s){
    String st = null;
    try {
      if(s.equals("!")) s ="-1";
      if(s.equals("!!")) s ="-2";
      st = getHistory(Integer.valueOf(s));
      if(st != null){
	outprintln(st);
	for(StringTokenizer str = new StringTokenizer(st, ";");
	    str.hasMoreTokens();){
	  String sl = str.nextToken();
	  oneLine(sl);
	  if(!loopFlag) return ;
	}
	replaceHistory(st);
	return;
      }
    } catch(Exception e){
      	    e.printStackTrace();
    }
    errprintln("!"+s+":No such history");
    return;
  }
  private int execCommand(String s){
    try {
      //	    errprintln("c:"+s);
      Process p = Runtime.getRuntime().exec(s,new String[]{},workingDir);
      int i;
      InputStream in = p.getErrorStream();
      //	    errprintln("fork start stderr"+s);
      while((i = in.read()) > -1) System.err.write(i);
      in = p.getInputStream();
      //	    errprintln("fork start stdout:"+s);
      while((i = in.read()) > -1) System.out.write(i);
      //	    errprintln("fork end");
      p.waitFor();
      return p.exitValue();
    } catch(Exception e){
      errprintln("IOException");
      e.printStackTrace();
    }
    return -1;
  }
  File workingDir = new File(".");
  public void cdCommand(String s){
    s = expandSharp(s, "/");
    File d = new File(workingDir, s);
    if(s.startsWith("/")) d = new File(s);
    if(d.exists() && d.isDirectory()){
      searchedNames = null;
      workingDir = d;
      System.setProperty("mdpw.workingdir",d.getPath());
//      errprintln("test property:"+System.getProperty("mdpw.workingdir"));
    } else {
      errprintln("No such directory:"+s);
    }
  }
  /*
   * wildcard for ls 
   */
  private boolean wildMatch(String o, String p){
    StringTokenizer ps = new StringTokenizer(p, "*");
    int oi = 0;
    while(ps.hasMoreElements()){
      String psn = ps.nextToken();
      int t = o.indexOf(psn);
//      System.err.println("wm:"+o+" "+p+" "+psn+":"+t+" "+( t+psn.length()));
      if(oi == 0 && t > 0 && !p.startsWith("*")) return false;
      if(t < oi) return false;
      oi = t+psn.length();
    }
//    System.err.println("wm:"+o+" "+p+" :"+oi+" "+(o.length()));
    return (oi == o.length() || p.endsWith("*"));
  }
  private void lsOne(String s, boolean lf){
    File d = workingDir;
    s = expandSharp(s, "/");
    if(s == null)  return;
    boolean wf = false;
    if(s.indexOf("*") > -1){  
      wf = true; //use wildcard for workingDir
//      System.err.println("use wildcard:"+s);	
    } else if(s.length() > 0){
      d = new File(d, s);
    }
    if(d.isDirectory()){
      String[] l = d.list();
      for(int i = 0; i < l.length; i ++){
        if(!wf || wildMatch(l[i], s)) {
	  if(lf){
	    File f = new File(d, l[i]);
	    if(f.isDirectory()){
	      outprintln(lsTimeString(f.lastModified())+ "\t" + l[i]+"/");
	    } else {
	      outprintln(lsTimeString(f.lastModified())+ "\t" + l[i]);
	    }
	  } else {
	    outprintln(l[i]);
  	  }
	}
      }
    } else if(d.exists()){
      outprintln(d.getName());
    } else {
      errprintln("No such file or directory:"+s);
    }
  } 
  private SimpleDateFormat dateFomatterY =new SimpleDateFormat("MMM dd  yyyy");
  private SimpleDateFormat dateFomatterH =new SimpleDateFormat("MMM dd HH:mm");
  private Date workDate = new Date();
  private long now = workDate.getTime();
  private static final long oneYear = 365L*24L*60L*60L*1000L;
  private String lsTimeString(long t){
    workDate.setTime(t);
    if((now - t) > oneYear) {
      return dateFomatterY.format(workDate);
    } else {
      return dateFomatterH.format(workDate);
    }
  }
  public void lsCommand(String s){
    boolean longF = false;
    Vector ds = new Vector();
    for(StringTokenizer st = new StringTokenizer(s, " ");
	st.hasMoreTokens();){
      String str = st.nextToken();
      if(str.startsWith("-l")){
	longF = true;
      } else {
	ds.addElement(str);
      }
    }
    if(ds.size() == 0){ ds.addElement(".");}
    for(Enumeration e =ds.elements(); e.hasMoreElements();){
      lsOne((String)e.nextElement(), longF);
    }
  }
  public void pwdCommand(String s){
    try {
      outprintln(workingDir.getCanonicalPath());
    } catch(Exception e){}
  }
  protected String expandSharp(String s, String sep) {
    StringBuffer r = new StringBuffer(); 
    StringTokenizer st = new StringTokenizer(s, " ");
    while(st.hasMoreTokens()){
      r.append(getName(st.nextToken(), sep));
      if(st.hasMoreTokens()) r.append(" ");
    }
    return r.toString();
  }
  Vector searchedNames = null;
  private String getName(String s, String sep) {
    if(sep == null ) sep = ".";
    if(s.startsWith("#")){ 
      s = s.substring(1);
      StringBuffer t = new StringBuffer();
      StringTokenizer st = new StringTokenizer(s, ".");
      if(st.hasMoreTokens()){
	s = st.nextToken();
	while(st.hasMoreTokens()){
	  t.append(".");
	  t.append(st.nextToken());
	}
      }
      StringBuffer bs = new StringBuffer();
      try {
	StringTokenizer bst = 
	  new StringTokenizer((String)searchedNames.
			      elementAt(Integer.valueOf(s).intValue()),
			      ".");
	while(bst.hasMoreTokens()){
	  bs.append(bst.nextToken());
	  if(bst.hasMoreTokens()){ bs.append(sep);}
	}
	
      } catch(Exception e){
	errprintln("Fail to expand#"+s);
      }
      outprintln("expand: #"+s+" -> "+bs.toString()+t.toString());
      return bs.toString()+t.toString();
    } 
    return s;
  }
  private void searchedNamesAdd(String s){
    if(searchedNames == null) searchedNames = new Vector();
    if(searchedNames.indexOf(s) < 0)
      searchedNames.addElement(s);
  }
  private void searchCommand(String s){
    String pn = expandSharp(s, ".");
    if(pn.indexOf("..") > -1){
      errprintln("Search path contains '..'. Ignore.");
    } else if(pn.length() > 0){
      String w = "";
      for(StringTokenizer st = new StringTokenizer(pn, ".");
	  st.hasMoreElements();){
	w += st.nextToken();
	if(st.hasMoreElements()){
	  w += "/";
	}
      }
      File d = new File(workingDir, w);
      if(d.isDirectory()){
	String[] fn = d.list(); 
	for(int i = 0; i < fn.length; i++){
	  String r = fn[i];
	  if(!pn.equals(".")){
	    r = pn+"."+fn[i];
	  }
	  if(fn[i].endsWith(JAVAEXT)){
	    searchedNamesAdd(r.substring(0,r.length()-JAVAEXT.length()));
	    
	  } else if(fn[i].endsWith(CLASSEXT)){
	    searchedNamesAdd(r.substring(0,r.length()- CLASSEXT.length()));
	  } else if((new File(d, fn[i])).isDirectory()){
	    searchedNamesAdd(r);
	  }
	}
      } else {
	errprintln("No directory for package:"+pn);
      }
    }
    if(searchedNames == null ||  searchedNames.size() == 0){
      errprintln("Nothing is searched");
    } else {
      for(int i = 0; i < searchedNames.size(); i++){
	outprintln("#"+i+": "+searchedNames.elementAt(i));
      }
    }
  }
  public void runCommand(String s){
    s = expandSharp(s, ".");
    execCommand("java -classpath ."+ System.getProperty("path.separator")+MyClassPath +" "+s);
  }
  private boolean compileCommand(String s){
    s = expandSharp(s, "/");
    errprintln("javac -classpath ."+ System.getProperty("path.separator")+MyClassPath +" "+s);
    if(execCommand("javac  -classpath ."+ System.getProperty("path.separator")+MyClassPath +" "+s) == 0) return true;
    return false;
  }
  private void crCommand(String s){
    //-d
    //.をデイレクトリに 
    String ws =  expandSharp(s, "/");
    if(compileCommand(ws+".java")){
      errprintln("End of compilation.");
      ws =  expandSharp(s, ".");
      runCommand(s);
    } else {
      errprintln("Error occured in compilation.");
    }
  }

  protected void sourceCommand(String s){
    try {
      BufferedReader input 
	= new BufferedReader(new FileReader(new File(workingDir, s)));
      String l;  
      while((l = input.readLine()) != null){
        outprintln("*"+PROMPT+l);
	for(StringTokenizer st = new StringTokenizer(l, ";");
	    st.hasMoreTokens();){
	  String sl = st.nextToken();
	  oneLine(sl);
	}
      }
      input.close();
    } catch(Exception e){
      errprintln("Error occured while reading:"+s);
      errprintln(e.toString());
    }
    
  }
  protected String getHistory(Integer i) {
    if(i.intValue() < 0){
      i = new Integer(i.intValue() + historyNumber); 
   } 
    return (String)commandHistory.get(i);
  }
  private void replaceHistory(String s) {
    commandHistory.put(new Integer(historyNumber),s);
  }
  protected int putHistory(String s) {
    historyNumber ++;
    commandHistory.put(new Integer(historyNumber),s);
    return historyNumber;
  }
}
/*
  実行	外部
  コンパイル 内部/外部
  あったら便利な機能
  ヒストリ
  コンプリーション代わりにサーチ
 */
