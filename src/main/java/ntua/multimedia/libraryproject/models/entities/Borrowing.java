package ntua.multimedia.libraryproject.models.entities;

import java.time.LocalDate;
import ntua.multimedia.libraryproject.models.BaseModel;

public class Borrowing extends BaseModel {
  private final String bookId;
  private final String userId;
  private final LocalDate approvedAt;

  public Borrowing(String id, String userId, String bookId, LocalDate approvedAt) {
    super(id);
    this.bookId = bookId;
    this.userId = userId;
    this.approvedAt = approvedAt;
  }

  public String getBookId() {
    return bookId;
  }

  public String getUserId() {
    return userId;
  }

  public LocalDate getApprovedAt() {
    return approvedAt;
  }

  public LocalDate getReturnBefore() {
    return approvedAt == null ? null : approvedAt.plusDays(5);
  }

  @Override
  public String toString() {
    return "Borrowing{"
        + "id='"
        + id
        + '\''
        + ", bookId='"
        + bookId
        + '\''
        + ", userId='"
        + userId
        + '\''
        + ", approvedAt='"
        + approvedAt
        + '}';
  }
}
