package com.cwbp.mdpw.util;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
public class ShellWindow implements ShellOut {
  PrintStream printstream = null;
  static String InnerShellClass = "com.cwbp.mdpw.util.MShell";
  private static final String PROMPT = ">";
  private void showMessage(String s){
    if(outputArea == null){
      System.out.println(s);
    } else {
      outputArea.append(s);
      outputArea.append("\n");
    }
  }
  public java.io.PrintStream getOut(){
//    System.out.println("ShellWindow.getPrintStream");
    if(printstream == null){
       printstream = new PrintStream(System.out){
         public void println(String s){
           showMessage(s);
         }
       };
    }
    return printstream;
  }
  public java.io.PrintStream getErr(){
//    System.out.println("ShellWindow.getPrintStream");
    if(printstream == null){
       printstream = new PrintStream(System.err){
         public void println(String s){
           showMessage(s);
         }
       };
    }
    return printstream;
  }
  protected void mainLoop(ShellOut so) throws IOException{
    printstream = System.out; 
    outputArea = null;
    BufferedReader input 
	= new BufferedReader(new InputStreamReader(System.in));
    String l =  " ";
    while(shell.loopFlag){
	for(StringTokenizer st = new StringTokenizer(l, ";");
	    st.hasMoreTokens();){
	  String sl = st.nextToken();
	  shell.oneLine(sl);
	  if(!shell.loopFlag) return ;
	}
        System.out.print(PROMPT);
        System.out.flush();
	if((l = input.readLine()) == null) return;
	shell.putHistory(l);
      }
  }
  JShell shell;
  public void init(JShell s){
    shell = s;
    shell.setOption(this);
  }
  public static void main(String[] args){
    try {
      ShellWindow w = new ShellWindow();
      w.init((JShell)Class.forName(InnerShellClass).newInstance());
      if(args.length > 1 && args[0].startsWith("-c")){
        System.out.println("Console mode.");
        w.mainLoop(w);
        System.exit(0);
      } else {
        try {
          System.setOut(w.getOut());
          System.setErr(w.getErr());
          w.initWidgets();
        } catch(Exception e){
          System.err.println("Error in starting window. Use -c option for Console mode.");
          System.err.println(e.toString());
        }
      }
    } catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
  }
  TextField inputField = new TextField();
  TextArea outputArea = new TextArea();
  private int historyIndex;  
  private void initWidgets(){
    Panel p = new Panel();
    inputField.addActionListener(new ActionListener(){
       public void actionPerformed(ActionEvent e){
//         System.err.println(e.toString());
         showMessage(e.getActionCommand());
         inputField.setText("");
  	 for(StringTokenizer st = new StringTokenizer(e.getActionCommand(), ";");
	    st.hasMoreTokens();){
	   String sl = st.nextToken();
           if(!localCommand(sl))  shell.oneLine(sl);
	   if(!shell.loopFlag) System.exit(0);
	 }
  	 historyIndex = shell.putHistory(e.getActionCommand())+1;
       }  
    });
    inputField.addKeyListener(new KeyAdapter(){
       public void keyReleased(KeyEvent e){
//         System.out.println(e.toString());
         switch(e.getKeyCode()){
           default: return ;
           case KeyEvent.VK_UP:
             historyIndex --;
             break;
           case KeyEvent.VK_DOWN:
            historyIndex ++;
            break;
         }
         String s = shell.getHistory(new Integer(historyIndex));
         if(s != null) inputField.setText(s); else inputField.setText("");
       } 
    });
    p.setLayout(new BorderLayout());
    p.add(inputField, "South");
    p.add(outputArea, "Center");
    outputArea.setEditable(false);
    Frame f = new Frame();
    f.add(p);
    f.setSize(300, 300);
    f.show();
  }
  /**
   * @return true if find local command
   */
  private boolean localCommand(String s){ 
    if(s.startsWith("clear")){
      outputArea.setText("");
    } else return false;
    return true;
  }
}

