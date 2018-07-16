import javafx.application.Application;
import javafx.stage.Stage;

public class VertexEdgeGUI  extends Application {
    public static void main(String args[]) {
        launch(args);
    }

    /**
     * Starts entire application by initializing the GUI (View)
     * @param mainStage The stage the main application window is formed on.
     */

    public void start(Stage mainStage) {
        GUI main = new GUI(mainStage);
    }
}
