package ntua.multimedia.libraryproject.models.tableRows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ntua.multimedia.libraryproject.models.BaseModel;

public class BookRow extends BaseModel {

  private final StringProperty title;
  private final StringProperty author;
  private final StringProperty isbn;
  private final StringProperty avgRatingScore;
  private final StringProperty totalRatings;

  public BookRow(
      String id,
      String title,
      String author,
      String isbn,
      String avgRatingScore,
      String totalRatings) {
    super(id);
    this.title = new SimpleStringProperty(title);
    this.author = new SimpleStringProperty(author);
    this.isbn = new SimpleStringProperty(isbn);
    this.avgRatingScore = new SimpleStringProperty(avgRatingScore);
    this.totalRatings = new SimpleStringProperty(totalRatings);
  }

  public StringProperty titleProperty() {
    return title;
  }

  public StringProperty authorProperty() {
    return author;
  }

  public StringProperty isbnProperty() {
    return isbn;
  }

  public StringProperty avgRatingScoreProperty() {
    return avgRatingScore;
  }

  public StringProperty totalRatingsProperty() {
    return totalRatings;
  }
}
