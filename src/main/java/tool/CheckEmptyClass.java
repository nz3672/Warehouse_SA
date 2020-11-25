package tool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class CheckEmptyClass implements checkEmpty {

    public boolean checkTextfieldEmpty(String text){
        if (text.equals("") && text != null){
            return false;
        }
        return true;
    }

    public boolean checkCheckboxEmpty(CheckBox checkBox){
        if (checkBox.isSelected()){
            return true;
        }
        return false;
    }
    public String checkSelectedRiobtn(final ToggleGroup group){
        if (group.getSelectedToggle() != null) {
            return group.getSelectedToggle().getUserData().toString();
        } else {
            return null;
        }
    }
}
