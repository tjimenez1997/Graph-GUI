import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextFieldHandler {

    /**
     * Text Field Event Handler
     * @param textField TextField from the GUI. Used to see what is in the User Input field from the GUI
     */
    public TextFieldHandler (TextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //If Empty and User Input doesn't match the Regex (1-9 only), make the textField empty
                if(textField.getText().length() == 1 && !textField.getText().substring(0,1).matches("[1-9][0-9]*")){
                    textField.clear();
                } else { //Removes any character that doesn't match the Regex 1-9 for the first number and 0-9 after the first number
                    if(textField.getText().length() >= 1 && !textField.getText().matches("[1-9][0-9]*")) {
                        textField.setText(newValue.substring(0, textField.getText().length() - 1));
                    }
                }
            }
        });
    }
}
