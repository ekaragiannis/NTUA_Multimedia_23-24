package ntua.multimedia.libraryproject.utils.custom.components.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert extends Alert {

  public CustomAlert(AlertType alertType, String title, String contentText) {
    super(alertType);
    setTitle(title);
    setHeaderText(null);
    setContentText(contentText);
    getDialogPane().getButtonTypes().clear();
    getDialogPane().getButtonTypes().add(ButtonType.OK);
  }
}
