package ntua.multimedia.libraryproject.models.tableRows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ntua.multimedia.libraryproject.models.BaseModel;

public class UserRow extends BaseModel {
  private final StringProperty username;
  private final StringProperty firstName;
  private final StringProperty lastName;
  private final StringProperty idCardNumber;
  private final StringProperty email;

  public UserRow(
      String id,
      String username,
      String firstName,
      String lastName,
      String idCardNumber,
      String email) {
    super(id);
    this.username = new SimpleStringProperty(username);
    this.firstName = new SimpleStringProperty(firstName);
    this.lastName = new SimpleStringProperty(lastName);
    this.idCardNumber = new SimpleStringProperty(idCardNumber);
    this.email = new SimpleStringProperty(email);
  }

  public StringProperty usernameProperty() {
    return username;
  }

  public StringProperty firstNameProperty() {
    return firstName;
  }

  public StringProperty lastNameProperty() {
    return lastName;
  }

  public StringProperty idCardNumberProperty() {
    return idCardNumber;
  }

  public StringProperty emailProperty() {
    return email;
  }
}
