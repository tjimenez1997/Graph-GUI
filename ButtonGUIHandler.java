import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

public class ButtonGUIHandler {

    /**
     * Button Event Handler
     * @param model Model representing the graph
     * @param buttonList Button Linked List used to see which one is being clicked
     */
    public ButtonGUIHandler(GraphModel model, LinkedList<Button> buttonList){
        Iterator buttonIterator = buttonList.iterator();
        //Goes through Linked List of Buttons to see which one was clicked and does appropriate action
        while(buttonIterator.hasNext()){
            Button next = (Button) buttonIterator.next();
            next.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    //If Add All Edges is clicked...
                    if(next.getUserData().toString().equals("6")){
                        model.addAllEdges();
                        System.out.println("Created All Possible Edges");
                    }
                    //If Random Weights is clicked...
                    else if(next.getUserData().toString().equals("7")){
                        model.addRandomWeights();
                        System.out.println("Added All Possible Random Weights");
                    }
                    //If Minimal Spanning Tree is clicked...
                    else if(next.getUserData().toString().equals("8")){
                        System.out.println("Minimal Spanning Tree (Not Yet Implemented). See Shortest Path.");
                    }
                    //If Help is clicked...
                    else{
                        HelpScreen newHelpWindow = new HelpScreen();
                    }
                }
            });
        }
    }
}
