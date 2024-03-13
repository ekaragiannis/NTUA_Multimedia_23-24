package ntua.multimedia.libraryproject.models.entities;

import ntua.multimedia.libraryproject.models.BaseModel;

public class User extends BaseModel {
  private final String username;
  private final String password;
  private final String idCardNumber;
  private final String firstName;
  private final String lastName;
  private final String email;

  public User(
      String id,
      String username,
      String password,
      String firstName,
      String lastName,
      String idCardNumber,
      String email) {
    super(id);
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.idCardNumber = idCardNumber;
    this.email = email;
  }

  public String getIdCardNumber() {
    return idCardNumber;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return "User{"
        + "id='"
        + id
        + '\''
        + ", username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", idCardNumber='"
        + idCardNumber
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", email='"
        + email
        + '\''
        + '}';
  }
}
