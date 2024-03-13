package ntua.multimedia.libraryproject.models.entities;

import java.time.LocalDate;
import ntua.multimedia.libraryproject.models.BaseModel;

public class Rating extends BaseModel {
  private final String userId;
  private final String bookId;
  private final int score;
  private final String description;
  private final LocalDate ratedAt;

  public Rating(String id, String userId, String bookId, int score, String description) {
    super(id);
    this.userId = userId;
    this.bookId = bookId;
    this.score = score;
    this.description = description;
    this.ratedAt = LocalDate.now();
  }

  public String getUserId() {
    return userId;
  }

  public String getBookId() {
    return bookId;
  }

  public int getScore() {
    return score;
  }

  public String getDescription() {
    return description;
  }

  public LocalDate getRatedAt() {
    return ratedAt;
  }
}
