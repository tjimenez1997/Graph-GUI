import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class ToggleGroupHandler {
    /**
     * ToggleGroup Handler used to reset GUI values/display whenever user selects a new radio button. Prevents potential problems when mixing up different functions of the GUI.
     * @param toggleGroup toggleGroup belonging to the Radio Buttons on the GUI
     * @param view GUI View
     */
    public ToggleGroupHandler(ToggleGroup toggleGroup, GUI view){
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                view.resetGUI();
            }
        });
    }
}
