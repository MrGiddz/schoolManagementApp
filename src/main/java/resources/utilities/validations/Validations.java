package resources.utilities.validations;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

public class Validations {

    public static boolean numeric(JFXTextField textField){
        return textField.getText().matches("[0-9]{15}");
    }

    public static boolean isEmpty(JFXTextField textField){
        return textField.getText().isEmpty();
    }

    public static boolean isEmpty(JFXPasswordField textField){
        return textField.getText().isEmpty();
    }

}
