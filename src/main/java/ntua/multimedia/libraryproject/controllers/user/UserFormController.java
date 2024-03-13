package ntua.multimedia.libraryproject.controllers.user;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
import ntua.multimedia.libraryproject.models.entities.User;
import ntua.multimedia.libraryproject.services.UserService;
import ntua.multimedia.libraryproject.utils.EncryptionUtil;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.SuccessAlert;

public class UserFormController {
  private final UserService userService;
  private final User currentUser;
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private PasswordField confirmPasswordField;
  @FXML private TextField emailField;
  @FXML private TextField firstNameField;
  @FXML private TextField lastNameField;
  @FXML private TextField idCardNumberField;
  @FXML private Button saveButton;

  public UserFormController(UserService userService, User currentUser) {
    this.userService = userService;
    this.currentUser = currentUser;
  }

  @FXML
  public void initialize() {
    String decryptedPassword = EncryptionUtil.decrypt(currentUser.getPassword());
    usernameField.setText(currentUser.getUsername());
    passwordField.setText(decryptedPassword);
    emailField.setText(currentUser.getEmail());
    firstNameField.setText(currentUser.getFirstName());
    lastNameField.setText(currentUser.getLastName());
    idCardNumberField.setText(currentUser.getIdCardNumber());

    saveButton.setDisable(true);
    BooleanBinding confirmPasswordEmpty =
        Bindings.createBooleanBinding(
            () -> confirmPasswordField.getText().isEmpty(), confirmPasswordField.textProperty());
    saveButton.disableProperty().bind(confirmPasswordEmpty);
  }

  @FXML
  private void handleSave() {
    String message = validateFields();
    if (!message.isEmpty()) {
      FailureAlert alert = new FailureAlert(message);
      alert.showAndWait();
      return;
    }
    try {
      userService.createOrUpdate(
          currentUser,
          usernameField.getText(),
          passwordField.getText(),
          firstNameField.getText(),
          lastNameField.getText(),
          idCardNumberField.getText(),
          emailField.getText());
      SuccessAlert alert = new SuccessAlert("User info updated successfully!");
      alert.showAndWait();
      usernameField.getScene().getWindow().hide();
    } catch (DuplicateFieldException e) {
      FailureAlert alert = new FailureAlert(e.getMessage());
      alert.showAndWait();
    }
  }

  @FXML
  private void handleCancel() {
    usernameField.getScene().getWindow().hide();
  }

  private String validateFields() {
    String message = "";
    if (passwordField.getText().length() < 8) {
      message += "Password must be at least 8 characters long\n";
    }
    if (!passwordField.getText().equals(confirmPasswordField.getText())) {
      message += "Passwords do not match\n";
    }
    if (!emailField.getText().contains("@")) {
      message += "Email invalid\n";
    }
    return message;
  }
}
