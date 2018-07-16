import javafx.scene.shape.Circle;

public class Vertex {

    private double xpos, ypos;
    private Circle circle;

    public Vertex(double x, double y) {
        xpos = x;
        ypos = y;
    }

    /**
     * Returns X Position of a Vertex
     * @return
     */
    public double getXPos() {
        return xpos;
    }

    /**
     * Return Y Position of a Vertex
     * @return
     */
    public double getYpos() {
        return ypos;
    }


    public void setXPos(double x){
        xpos = x;
    }

    public void setYpos(double y){
        ypos = y;
    }

    /**
     * Associates a Circle (GUI View Object) with the theoretical Vertex Object
     * @param value
     */
    public void setCircle(Circle value) {
        circle = value;
    }

    public Circle getCircle() {
        return circle;
    }

    /**
     * Returns true if a Vertex is being clicked. Useful for the Graph Model because it will add it to a LinkedList with selected Vertex's
     * @param clickX  X Location of User Click on the Graph Pane GUI
     * @param clickY Y Location of User Click on the Graph Pane GUI
     * @return
     */
    public boolean isSelected(double clickX, double clickY) {
        double XPosDelta = Math.abs(clickX - xpos);
        double YPosDelta = Math.abs(clickY - ypos);
        if (XPosDelta < 15 & YPosDelta < 15) { //Gives a 15 Pixel freebie if user doesn't click exactly on a circle object (GUI object representing a Vertex)
            return true;
        } else {
            return false;
        }
    }
}
