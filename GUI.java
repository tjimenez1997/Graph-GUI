import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.LinkedList;

public class GUI {
    //Class Variables for all components involving the GUI (View)
    private BorderPane layout = new BorderPane();
    private VBox vBox = new VBox();
    private HBox hBox = new HBox();
    private StackPane stackPane = new StackPane();
    private Pane graphArea = new Pane();
    private LinkedList<Circle> circleList = new LinkedList<Circle>();
    private GraphModel graphModel = new GraphModel(this);
    private LinkedList<Button> buttonList = new LinkedList<Button>();
    private LinkedList<Line> lineList = new LinkedList<Line>();
    private LinkedList<Edge> switchedLineList = new LinkedList<Edge>();
    private Circle movingVertex;
    private Location movingVertexOriginalLocation = new Location();
    private LinkedList<Location> originalStartsofLine = new LinkedList<Location>();
    private TextField weightValue = new TextField();
    final ToggleGroup radioGroup = new ToggleGroup();

    /**
     * Constructor used to initialize GUI view, with essential buttons and graph area.
     * @param mainStage The stage the main application window is formed on.
     */
    public GUI(Stage mainStage) {
        createWindow(mainStage);
    }

    private void createWindow(Stage mainStage){
        mainStage.setTitle("Spring 2018 CS 313 Project");
        RadioButton[] radioButtons = new RadioButton[5];
        RadioButton rButton1 = new RadioButton("Add Vertex");
        RadioButton rButton2 = new RadioButton("Add Edge");
        RadioButton rButton3 = new RadioButton("Move Vertex");
        RadioButton rButton4 = new RadioButton("Shortest Path");
        RadioButton rButton5 = new RadioButton("Change a weight to:  ");
        rButton1.setToggleGroup(radioGroup);
        rButton2.setToggleGroup(radioGroup);
        rButton3.setToggleGroup(radioGroup);
        rButton4.setToggleGroup(radioGroup);
        rButton5.setToggleGroup(radioGroup);
        rButton1.setUserData("1");
        rButton2.setUserData("2");
        rButton3.setUserData("3");
        rButton4.setUserData("4");
        rButton5.setUserData("5");
        Button button1 = new Button("Add All Edges");
        Button button2 = new Button("Random Weights");
        Button button3 = new Button("Minimal Spanning Tree");
        Button button4 = new Button("Help");
        button1.setUserData("6");
        button2.setUserData("7");
        button3.setUserData("8");
        button4.setUserData("9");
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);

        //Button + Weight Field Controller Classes being Initialized
        ButtonGUIHandler buttonHandler = new ButtonGUIHandler(graphModel,buttonList);
        TextFieldHandler textFieldHandler = new TextFieldHandler(weightValue);
        ToggleGroupHandler toggleGroupHandler = new ToggleGroupHandler(radioGroup,this);

        //Adds all buttons + spacers to keep GUI responsive to layout managers vBox
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(createSpacer(), rButton1, createSpacer(), rButton2, createSpacer(), rButton3, createSpacer(), rButton4, createSpacer(), hBox, createSpacer(), button1, createSpacer(), button2, createSpacer(), button3, createSpacer(), button4, createSpacer());
        hBox.getChildren().addAll(rButton5, weightValue);
        layout.setLeft(vBox);

        // Creates Graph Pane Area +  Adds Handler
        graphArea.setStyle("-fx-background-color: #E0E0E0");
        stackPane.getChildren().add(graphArea);
        layout.setCenter(stackPane);
        GraphAreaGUIHandler graphHandler = new GraphAreaGUIHandler(graphModel, this);
        Scene scene = new Scene(layout, 800, 600);
        mainStage.setScene(scene);
        mainStage.show();
        System.out.println("Debug:");
    }

    /**
     * Creates Spacer Node to put in between each Node on the Left Side of the GUI. This keeps spacing equal between each Radio Button/Button when entire window is resized.
     * @return
     */
    private Node createSpacer(){
        Region spacer = new Region();
        vBox.setVgrow(spacer,Priority.ALWAYS);
        return spacer;
    }

    /**
     * Draws a Vertex on the graph area. Adds Circle object representing the Vertex to a Linked List for future modification. Additionally adds circle handler for when user selects the circle to add a edge or weight.
     * @param clickX X Position of Vertex (To be drawn)
     * @param clickY Y Position of Vertex (To be drawn)
     */
    public void drawVertex(double clickX, double clickY){
        Circle circle = new Circle();
        graphModel.linkCircleToVertex(circle);
        circleList.add(circle);
        CircleGUIHandler circleHandler = new CircleGUIHandler(graphModel,circle,this);
        circle.setManaged(false);
        circle.setCenterX(clickX);
        circle.setCenterY(clickY);
        circle.setRadius(6);
        circle.setFill(Color.RED);
        circle.setVisible(true);
        stackPane.getChildren().add(circle);
    }

    /**
     * Redraws a Vertex on the graph area. Used when drawing a edge (To bring the Vertex circle to the front).
     * @param startV X Position of Vertex (To be drawn)
     * @param endV Y Position of Vertex (To be drawn)
     */
    public void redrawVertex(Vertex startV, Vertex endV){
        Circle start = findVertex(startV);
        Circle end = findVertex(endV);
        start.toFront();
        end.toFront();
    }

    /**
     * Redraws a Vertex on the graph area. Used when user moved a Vertex with the "Move Edge" button
     * @param clickX X Position of Vertex (To be drawn)
     * @param clickY Y Position of Vertex (To be drawn)
     */
    public void drawMovingVertex(double clickX, double clickY){
        movingVertex.setCenterX(clickX);
        movingVertex.setCenterY(clickY);
    }

    /**
     * Finds a Vertex (Circle on the graph area) using the coordinates of the Model Objects Vertex
     * @param vertexToFind Vertex Object from Model used to find correct Circle object
     * @return
     */
    private Circle findVertex(Vertex vertexToFind){
        Iterator circleIterator = circleList.iterator();
        while(circleIterator.hasNext()){
            Circle current = (Circle) circleIterator.next();
            if(current.getCenterX() == vertexToFind.getXPos() && current.getCenterY() == vertexToFind.getYpos()){
                return current;
            }
        }
        return null;
    }

    /**
     * Finds the Vertex (Circle Object) that is currently moving and places it in class variable used for other methods
     * @param vertexToFind Vertex Object with Coordinates used to find corresponding Circle Object
     */
    public void findMovingVertex(Vertex vertexToFind) {
        Circle circle = findVertex(vertexToFind);
        if (circle != null) {
            movingVertex = circle;
            movingVertexOriginalLocation.setX(circle.getCenterX());
            movingVertexOriginalLocation.setY(circle.getCenterY());
        }
    }

    /**
     * Resets the GUI (Removes highlighted Vertex's and Edges
     * Used when user toggles between GUI Radio Buttons
     */
    public void resetGUI() {
        Iterator lineIterator = lineList.iterator();
        while (lineIterator.hasNext()) {
            Line current = (Line) lineIterator.next();
            unhighlightLine(current);
        }

        Iterator circleIterator = circleList.iterator();
        while (circleIterator.hasNext()) {
            Circle current = (Circle) circleIterator.next();
            unhighlightVertex(current);
        }
        if (!getRadioGroup().getSelectedToggle().getUserData().toString().equals("4")){
            graphModel.clearConnectedEdge();
            graphModel.clearSelectedEdge();
        }
        weightValue.setText("");
    }

    /**
     * Highlights a Edge to the color green
     * @param line Line to highlight (GUI object representing the Model's Edge)
     */
    public void highlightLine(Line line) {
        line.setStroke(Color.GREEN);
    }

    /**
     * Unhighlights a Edge to the default color (red)
     * @param line Line to unhighlight (GUI object representing the Model's Edge)
     */
    public void unhighlightLine(Line line) {
        line.setStroke(Color.BLUE);
    }

    /**
     * Highlights a Vertex to the color green
     * @param circle Vertex to Highlight (GUI object representing the Model's Vertex)
     */
    public void highlightVertex(Circle circle){
        circle.setFill(Color.GREEN);
    }

    /**
     * Unhighlights a Vertex to the default color (red)
     * @param circle Vertex to unhighlight (GUI object representing the Model's Vertex)
     */
    public void unhighlightVertex(Circle circle){
        circle.setFill(Color.RED);
    }

    /**
     * Draws a Edge between two Selected Vertex's on the Graph Area GUI
     * @param startPoint Vertex used to start the Edge
     * @param endPoint Vertex used to end the Edge
     */
    public void drawEdge(Vertex startPoint, Vertex endPoint){
        Line edge = new Line(startPoint.getXPos(), startPoint.getYpos(), endPoint.getXPos(), endPoint.getYpos());
        graphModel.linkLineToEdge(edge);
        lineList.add(edge);
        edge.setStroke(Color.BLUE);
        edge.setManaged(false);
        stackPane.getChildren().add(edge);
    }

    /**
     * Draws Edges that are moving when user picks up a Vertex to move on the Graph Area
     * @param mouseX Mouse X Coordinate
     * @param mouseY Mouse Y Coordinate
     */
    public void drawMovingEdges(double mouseX, double mouseY){
        LinkedList<Edge> movingEdges = graphModel.getConnectedEdgeList();
        //System.out.println("Edge Count: "+movingEdges.size());
        Iterator edgeIterator = movingEdges.iterator();
        while(edgeIterator.hasNext()){
            Edge current = (Edge) edgeIterator.next();
            Line currentLine = current.getLine();
            currentLine.setStartX(mouseX);
            currentLine.setStartY(mouseY);
            if(current.getWeight() != 0){
                drawMovingWeight(current);
            }
        }

    }

    /**
     * Draws a weight value on a Edge
     * @param selectedEdge Edge that is going to get a newly drawn weight value
     */
    public void drawWeight(Edge selectedEdge){
        double weightX, weightY;
        weightX = (selectedEdge.getStart().getXPos() + selectedEdge.getEnd().getXPos()) / 2;
        weightY = (selectedEdge.getStart().getYpos() + selectedEdge.getEnd().getYpos()) / 2;
        //System.out.println("Weight: "+selectedEdge.getWeight());
        Text weightText = new Text();
        selectedEdge.setWeightText(weightText);
        String text = Integer.toString(selectedEdge.getWeight());
        weightText.setText(text);
        weightText.setManaged(false);
        weightText.setX(weightX);
        weightText.setY(weightY);
        weightText.setVisible(true);
        stackPane.getChildren().add(weightText);
    }

    /**
     * Draws Moving Edge Weights when user chooses to move a Vertex with attached edges on it (That contain a weight)
     * @param selectedEdge Edge with weight that needs to be redrawn
     */
    public void drawMovingWeight(Edge selectedEdge){
        double weightX, weightY;
        weightX = (selectedEdge.getStart().getXPos() + selectedEdge.getEnd().getXPos()) / 2;
        weightY = (selectedEdge.getStart().getYpos() + selectedEdge.getEnd().getYpos()) / 2;
        Text weightText = selectedEdge.getWeightText();
        weightText.setX(weightX);
        weightText.setY(weightY);
        //System.out.println("Weight X: "+weightX+" Y: "+weightY);
    }

    /**
     * Hides a Edge's Weight Text on the GUI
     * @param selectedEdge Edge with weight text that is going to be hidden
     */
    public void eraseWeight(Edge selectedEdge){
        selectedEdge.getWeightText().setVisible(false);
    }

    /**
     * Flips the Edges (Line) Start point and End Point if Vertex that user is dragging is the endpoint of any of the moving edges. This makes drawing moving edges easier because only the start point needs to be modified then
     * @param edges Edge (Line associated with it) that is going to get its Startpoint and Endpoint flipped;
     */
    public void processEdges(LinkedList<Edge> edges) {
        Iterator edgeIterator = edges.iterator();
        while(edgeIterator.hasNext()){
            Edge current = (Edge)edgeIterator.next();
            //System.out.println("Vertex X: "+movingVertexOriginalLocation.getX()+"Vertex Y: "+movingVertexOriginalLocation.getY());
            //System.out.println("Edge Start X: "+current.getStart().getYpos()+" Y: "+current.getStart().getYpos());
            //System.out.println("Edge End X: "+current.getEnd().getXPos()+" Y: "+current.getEnd().getYpos());
            if(movingVertexOriginalLocation.getX() == current.getEnd().getXPos() && movingVertexOriginalLocation.getY() == current.getEnd().getYpos()){
                switchedLineList.add(current);
                Location originalStartofLine = new Location();
                originalStartofLine.setX(current.getStart().getXPos());
                originalStartofLine.setY(current.getStart().getYpos());
                originalStartsofLine.add(originalStartofLine);
                current.getLine().setEndX(originalStartofLine.getX());
                current.getLine().setEndY(originalStartofLine.getY());
                //System.out.println("Switched a edge");
            }
        }
    }

    /**
     * Restores the original state of any Lines who's startpoint and endpoint where modified. This is used to properly display lines when user releases a Vertex after moving it.
     */
    public void postProcessEdges() {
        Iterator edgeIterator = switchedLineList.iterator();
        Iterator originalLocation = originalStartsofLine.iterator();
        //System.out.println("Switched Edge Count: "+switchedLineList.size());
        while (edgeIterator.hasNext() && originalLocation.hasNext()) {
            Edge current = (Edge) edgeIterator.next();
            Location originalLocations = (Location) originalLocation.next();
            current.getLine().setStartX(originalLocations.getX());
            current.getLine().setStartY(originalLocations.getY());
            current.getLine().setEndX(movingVertex.getCenterX());
            current.getLine().setEndY(movingVertex.getCenterY());
        }
        originalStartsofLine.clear();
        switchedLineList.clear();
    }

    /**
     * Method used by Model to check the current Value of the Weight Value textfield. Returns 1 if nothing is in the box.
     * @return
     */
    public int getWeightValue() {
        //Returns 1 to Controller if there is no User Input
        if(weightValue.getText().isEmpty()){
            System.out.println("No Weight in Textfield");
            return 1;
        } else {
            return Integer.parseInt(weightValue.getText());
        }
    }

    /**
     * Returns the original position of a Vertex the user moved
     * @return
     */
    public Location getOriginalVertexLocation() {
        return movingVertexOriginalLocation;
    }

    /**
     * Method used to give information to the controller about which radio toggle is selected.
     * @return
     */
    public ToggleGroup getRadioGroup(){
        return radioGroup;
    }

    /**
     * Method used to give scope to the Graph Area controller
     * @return
     */
    public Pane getGraphArea(){
        return graphArea;
    }
}
