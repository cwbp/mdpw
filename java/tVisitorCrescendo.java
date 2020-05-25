import com.cwbp.mdpw.*;
public class tVisitorCrescendo implements Visitor {
	double duration;
	int startVelocity;
	int endVelocity;
	public tVisitorCrescendo(double d, int s, int e){
	  duration = d;
	  startVelocity = s;
	  endVelocity = e;
	}
   public void process(DataNote n, double t){
   	  n.setVelocity((int)((endVelocity-startVelocity)*t/duration+startVelocity));
   	  System.out.println("tVisitorCrescendo:"+n.getVelocity());
   }

  public static void main(String[] a){
    Player p = new MIDIFilePlayer();
    DataSequence s = new DataSequence();
    s.addElement(new DataNote(60, 2, 32, 0));
    s.addElement(new DataNote(62));
    s.addElement(new DataNote(64));
    s.addElement(new DataNote(65));
  	tVisitorCrescendo v = new tVisitorCrescendo(s.getDuration(), 16, 89);
  	s.visit(v);
  	
    p.init("tVisitor.mid");
    p.setTempo(120);
    p.play(s);
    p.end();
  }
}
