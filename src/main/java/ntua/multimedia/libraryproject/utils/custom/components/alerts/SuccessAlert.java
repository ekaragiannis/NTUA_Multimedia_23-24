package ntua.multimedia.libraryproject.utils.custom.components.alerts;

import javafx.scene.control.Alert;

public class SuccessAlert extends CustomAlert {
  public SuccessAlert(String message) {
    super(Alert.AlertType.CONFIRMATION, "Success", message);
  }
}
