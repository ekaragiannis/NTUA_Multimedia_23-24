package ntua.multimedia.libraryproject.models.tableRows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ntua.multimedia.libraryproject.models.BaseModel;

public class BorrowingRow extends BaseModel {
  private final StringProperty isbn;
  private final StringProperty bookTitle;
  private final StringProperty username;
  private final StringProperty email;
  private final StringProperty approvedAt;
  private final StringProperty returnBefore;

  public BorrowingRow(
      String id,
      String isbn,
      String bookTitle,
      String username,
      String email,
      String approvedAt,
      String returnBefore) {
    super(id);
    this.isbn = new SimpleStringProperty(isbn);
    this.bookTitle = new SimpleStringProperty(bookTitle);
    this.username = new SimpleStringProperty(username);
    this.email = new SimpleStringProperty(email);
    this.approvedAt = new SimpleStringProperty(approvedAt);
    this.returnBefore = new SimpleStringProperty(returnBefore);
  }

  public StringProperty titleProperty() {
    return bookTitle;
  }

  public StringProperty usernameProperty() {
    return username;
  }

  public StringProperty approvedAtProperty() {
    return approvedAt;
  }

  public StringProperty returnBeforeProperty() {
    return returnBefore;
  }

  public StringProperty isbnProperty() {
    return isbn;
  }

  public StringProperty emailProperty() {
    return email;
  }
}
