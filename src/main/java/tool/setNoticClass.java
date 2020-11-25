package tool;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class setNoticClass implements setNotic {
    public void showNotic(String info, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(info);
        alert.setHeaderText(null);
        alert .setTitle(title);
        alert.showAndWait();
    }

    public boolean showComfirm(String info, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(info);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }
}
