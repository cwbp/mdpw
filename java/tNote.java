import com.cwbp.mdpw.*;
public class tNote {
  public static void main(String[] a){
    Player p = new MIDIFilePlayer();
    p.init("tNote.mid");
    p.setTempo(120);
    p.play(new DataNote(60, 1, 64, 1));
    p.end();
  }
}
