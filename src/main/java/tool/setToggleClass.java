package tool;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class setToggleClass implements setToggle {
    //set radiobutton and namebutton as Toggle group
    public void setToggle(RadioButton radioButton, String string, ToggleGroup toggleGroup) {
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setUserData(string);
    }
}
