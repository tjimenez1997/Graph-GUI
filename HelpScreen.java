import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class HelpScreen {
    public HelpScreen(){
        System.out.println("Help Window");
        Stage stage = new Stage();
        start(stage);
    }

    /**
     * Creates a Alert Window with Help Information
     * @param mainStage Stage for the Alert Window
     */
    public void start(Stage mainStage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spring 2018 CS 313 Project");
        alert.setHeaderText("Help Center");
        String helpText = "Add Vertex: If this button is selected you can click on the graph section and add multiple vertex's in unique locations \n\nAdd Edge: When this button is selected you can click on two existing vertex's in the graph area and connect them to each other\n\nMove Vertex: This can be used to move any vertex in the Graph Area to a different area\n\nShortest Path: Used to calculate the shortest path between connected Vertex's. Shortest path can only be calculated if selected Vertex's have a edges between them with weight values\n\nChange a weight to: Allows you to adjust the weight of a specific edge you select\n\nAdd All Edges: Adds all possible edges to the Vertex's on your graph area\n\nRandom Weights: Adds random weights (Value 1-25) to all edges on your Graph Area\n\nMinimal Spanning Tree: Not implemented. See Shortest Path.\n\nHelp Button: How did you get here?";
        alert.setContentText(helpText);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }


}
