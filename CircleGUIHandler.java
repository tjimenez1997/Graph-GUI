import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class CircleGUIHandler {
    /**
     *  Circle Object Event Handler
     * @param model Model representing the graph
     * @param circle Circle Object receiving the handler
     * @param currentView GUI that updates when the model does. Used to implement Event Handler
     */
    public CircleGUIHandler(GraphModel model, Circle circle, GUI currentView){

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                //ClickX and ClickY gives coordinates where your mouse clicked on the Graph Pane
                double clickX = event.getX();
                double clickY = event.getY();
                //If Add Edges is Selected...
                if(currentView.getRadioGroup().getSelectedToggle() != null && currentView.getRadioGroup().getSelectedToggle().getUserData().toString().equals("2")){
                    model.selectVertex(clickX,clickY);
                }
                //If Add a Weight is Selected...
                else if(currentView.getRadioGroup().getSelectedToggle() != null && currentView.getRadioGroup().getSelectedToggle().getUserData().toString().equals("5")){
                    model.selectVertex(clickX,clickY);
                }
                //If Shortest Path is Selected...
                else if(currentView.getRadioGroup().getSelectedToggle() != null && currentView.getRadioGroup().getSelectedToggle().getUserData().toString().equals("4")){
                    currentView.resetGUI();
                    model.selectVertex(clickX,clickY);
                }
            }
        });

        /**
         * Handles Model/GUI when user is pressing on a vertex (Circle) to move it
         */
        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double clickX = event.getX();
                double clickY = event.getY();
                if(currentView.getRadioGroup().getSelectedToggle() != null && currentView.getRadioGroup().getSelectedToggle().getUserData().toString().equals("3")){
                    model.selectVertex(clickX,clickY);
                    if(model.getSelectedVertextList().size() == 1){
                        currentView.findMovingVertex(model.getSelectedVertextList().getFirst());
                        model.getConnected(model.getSelectedVertextList().getFirst());
                        currentView.processEdges(model.getConnectedEdgeList());
                        currentView.drawMovingEdges(currentView.getOriginalVertexLocation().getX(),currentView.getOriginalVertexLocation().getY());
                    } else{
                        System.out.println("Could not find vertex");
                    }
                }
            }
        });

        /**
         * Handles processing/updating the Model/GUI when user is moving a vertex
         */
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double clickX = event.getX();
                double clickY = event.getY();
                if(currentView.getRadioGroup().getSelectedToggle() != null && currentView.getRadioGroup().getSelectedToggle().getUserData().toString().equals("3") && model.getSelectedVertextList().size() == 1){
                    Vertex toModifyLocation = model.getSelectedVertextList().getFirst();
                    currentView.drawMovingVertex(clickX, clickY);
                    currentView.drawMovingEdges(clickX,clickY);
                    toModifyLocation.setXPos(clickX);
                    toModifyLocation.setYpos(clickY);
                }
            }
        });

        /**
         * Handles processing/updating the Model/GUI when user releases a vertex after moving it
         */
        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(currentView.getRadioGroup().getSelectedToggle() != null && currentView.getRadioGroup().getSelectedToggle().getUserData().toString().equals("3")){
                    if(model.getSelectedVertextList().size() == 1){
                        Vertex toModifyLocation = model.getSelectedVertextList().getFirst();
                        currentView.unhighlightVertex(toModifyLocation.getCircle());
                        //System.out.println(toModifyLocation);
                        currentView.postProcessEdges();
                        model.clearSelectedEdge();
                        model.clearConnectedEdge();
                    }
                }
            }
        });
    }
}
