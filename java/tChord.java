import com.cwbp.mdpw.*;
public class tChord {
  public static void main(String[] a){
    Player p = new MIDIFilePlayer();
    DataChord c = new DataChord();
    c.addElement(new DataNote(60, 1, 64, 0));
    c.addElement(new DataNote(64, 1));
    c.addElement(new DataNote(67, 1));
    p.init("tChord.mid");
    p.setTempo(120);
    p.play(c);
    p.end();
  }
}
