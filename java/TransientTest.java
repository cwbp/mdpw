import java.util.Random;
import com.cwbp.mdpw.*;
public class TransientTest {

 static Random rand =new Random();
  static int[] make(int[] start, int[] end, int before, int length, int after){
     int[] result =new int[before+length+after];
     int index = 0;
     
     int oi = -1;
     int ni = oi;
     for(int i = 0; i <before; i ++){
        while(oi ==  ni){ ni = rand.nextInt(start.length); }
        result[index] = start[ni];
	index ++;
        oi = ni;
     }
     for(int i = 0; i <length; i ++){
//       if(rand.nextDouble()*length < i){
       if(rand.nextDouble() < .5){
        while(oi ==  ni){ ni = rand.nextInt(end.length); }
        result[index] = end[ni];
       } else {
        while(oi ==  ni){ ni = rand.nextInt(start.length); }
        result[index] = start[ni];
       }
	index ++;
        oi = ni;
     }
     for(int i = 0; i <after; i ++){
        while(oi ==  ni){ ni = rand.nextInt(end.length); }
        result[index] = end[ni];
	index ++;
        oi = ni;
     }
     return result;
  }
  public static void main(String[] args){
    int[] start = work2.chord[work2._C];
    int[] end = work2.chord[work2._G];
    int l = 32;
    Player p = new MIDIFilePlayer();
    p.init("ttest.mid");
    p.setTempo(120);
    DataSequence s = new DataSequence();
    int[] ps = Transient.make(start, end, l, l, l);
    s.add(new DataNote(64, 1, 0, 1));
    for(int j = 0; j < ps.length; j ++){
       int v =64;
       if(j%l == 0) v = 127;
       s.add(new DataNote(ps[j], 0.25, v, 1));
    }
    p.play(s);
    p.end();

  }
}
