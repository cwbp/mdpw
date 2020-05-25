import com.cwbp.mdpw.*;
public class tSeq {
  public static void main(String[] a){
    Player p = new MIDIFilePlayer();
    DataSequence s = new DataSequence();
    s.addElement(new DataNote(60, 2, 32, 0));
    s.addElement(new DataNote(62));
    s.addElement(new DataNote(64));
    p.init("tSeq.mid");
    p.setTempo(120);
    p.play(s);
    p.end();
  }
}
