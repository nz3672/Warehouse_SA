package tool;

import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public interface checkEmpty {
    boolean checkTextfieldEmpty(String textInput);
    boolean checkCheckboxEmpty(CheckBox checkBox);
    String checkSelectedRiobtn(ToggleGroup group);
}
