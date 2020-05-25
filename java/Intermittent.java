import java.util.Vector;
public interface Intermittent {
   public int next();
   public Vector<Integer> create(int duration_l);
}
