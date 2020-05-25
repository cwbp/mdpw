import com.cwbp.mdpw.*;
public class tSample {
  public static void main(String[] a){
    Player p = new MIDIFilePlayer();
    DataChord c = new DataChord();           //Chord:C
    c.addElement(new DataNote(60, 2, 16, 0));
    c.addElement(new DataNote(64, 2));
    c.addElement(new DataNote(67, 2));
    DataSequence b = new DataSequence();     //Bugging
    b.addElement(c);
    b.addElement(c);
    DataSequence s = new DataSequence();     //Melody
    s.addElement(new DataNote(60, 1, 32, 0));
    s.addElement(new DataNote(62, 1));
    s.addElement(new DataNote(64, 1));
    s.addElement(new DataNote(64, 1, DataNote.VREST));
    DataChord m = new DataChord();           //Phrase
    m.addElement(s);
    m.addElement(b);
    p.init("tSample.mid");
    p.setTempo(120);
    p.play(m);                              //repeat 2 times 
    p.play(m);
    p.end();
  }
}
