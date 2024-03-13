package ntua.multimedia.libraryproject.models.entities;

import ntua.multimedia.libraryproject.models.BaseModel;

public class Book extends BaseModel {
  private final String author;
  private final String publisher;
  private final String isbn;
  private final String publishedYear;
  private final String categoryId;
  private final int totalCopies;
  private String title;

  public Book(
      String id,
      String isbn,
      String title,
      String author,
      String publisher,
      String publishedYear,
      String categoryId,
      int totalCopies) {
    super(id);
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.isbn = isbn;
    this.publishedYear = publishedYear;
    this.categoryId = categoryId;
    this.totalCopies = totalCopies;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public String getPublisher() {
    return publisher;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getPublishedYear() {
    return publishedYear;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public int getTotalCopies() {
    return totalCopies;
  }

  @Override
  public String toString() {
    return "Book{"
        + "id='"
        + id
        + '\''
        + ", title='"
        + title
        + '\''
        + ", author='"
        + author
        + '\''
        + ", isbn='"
        + isbn
        + '\''
        + '}';
  }
}
