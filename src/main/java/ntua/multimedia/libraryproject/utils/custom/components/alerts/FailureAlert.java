package ntua.multimedia.libraryproject.utils.custom.components.alerts;

import javafx.scene.control.Alert;

public class FailureAlert extends CustomAlert {
  public FailureAlert(String message) {
    super(Alert.AlertType.ERROR, "Failure", message);
  }
}
