import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Edge {
    private Vertex start, end;
    private Text weightText;
    private Line line = null;
    private int weight = 0;

    public Edge(Vertex startPoint,Vertex endPoint){
        start = startPoint;
        end = endPoint;
    }

    /**
     * Returns weight value for a particular edge
     * @return
     */
    public int getWeight(){
        return weight;
    }

    /**
     * Sets weight value for a particular edge
     * @param value Weight Value that Edge will be set to
     */
    public void setWeight(int value) {
        weight = value;
    }

    public Text getWeightText(){
        return weightText;
    }

    public void setWeightText(Text text){
        weightText = text;
    }

    /**
     * Returns start point (Vertex) for a particular edge
     * @return
     */
    public Vertex getStart(){
        return start;
    }

    /**
     * Returns end point (Vertex) for a particular edge
     * @return
     */
    public Vertex getEnd(){
        return end;
    }

    /**
     * Associates Edge to a Line (GUI's View of the theoretical Edge Object)
     * @param value
     */
    public void setLine(Line value){
        line = value;
    }

    public Line getLine(){
        if(line.equals(null)) {
            return null;
        }
        return line;
    }
}
