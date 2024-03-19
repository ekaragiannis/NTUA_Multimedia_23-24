package ntua.multimedia.libraryproject.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
import ntua.multimedia.libraryproject.exceptions.InvalidIdException;
import ntua.multimedia.libraryproject.exceptions.InvalidReferenceException;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.models.entities.Category;
import ntua.multimedia.libraryproject.models.entities.Rating;
import ntua.multimedia.libraryproject.repositories.BookRepository;
import ntua.multimedia.libraryproject.repositories.BorrowingRepository;
import ntua.multimedia.libraryproject.repositories.CategoryRepository;
import ntua.multimedia.libraryproject.repositories.RatingRepository;
import ntua.multimedia.libraryproject.utils.GenerateIdUtil;

public class BookService {
  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  private final BorrowingRepository borrowingRepository;
  private final RatingRepository ratingRepository;

  public BookService(
      BookRepository bookRepository,
      CategoryRepository categoryRepository,
      BorrowingRepository borrowingRepository,
      RatingRepository ratingRepository) {
    this.bookRepository = bookRepository;
    this.categoryRepository = categoryRepository;
    this.borrowingRepository = borrowingRepository;
    this.ratingRepository = ratingRepository;
  }

  /**
   * Creates or updates a book with the provided information.
   *
   * @param book The book to create or update. If null, a new book will be created.
   * @param isbn The new ISBN to assign to the book.
   * @param title The title of the book.
   * @param author The author of the book.
   * @param publisher The publisher of the book.
   * @param publishedYear The year when the book was published.
   * @param categoryTitle The title of the category to which the book belongs.
   * @param totalCopies The total number of copies available for the book.
   */
  public void createOrUpdate(
      Book book,
      String isbn,
      String title,
      String author,
      String publisher,
      String publishedYear,
      String categoryTitle,
      int totalCopies) {
    String bookId = book == null ? GenerateIdUtil.generateUniqueId(bookRepository) : book.getId();
    Category category =
        categoryRepository
            .findByTitle(categoryTitle)
            .orElseThrow(() -> new InvalidReferenceException("Category not found"));
    Book newBook =
        new Book(
            bookId, isbn, title, author, publisher, publishedYear, category.getId(), totalCopies);
    String msg = checkUniqueConstraints(newBook);
    if (!msg.isEmpty()) {
      throw new DuplicateFieldException(msg);
    }
    bookRepository.save(newBook);
  }

  /**
   * Retrieves all books from the repository
   *
   * @return An ArrayList containing all books
   */
  public ArrayList<Book> findAll() {
    return bookRepository.findAll();
  }

  /**
   * Retrieves books sorted by average rating.
   *
   * @param ascending If true, sorts in ascending order, otherwise in descending order.
   * @param limit Maximum number of books to retrieve.
   * @return A List of books sorted by average rating.
   */
  public List<Book> findAll(boolean ascending, int limit) {
    ArrayList<Book> allBooks = bookRepository.findAll();
    Comparator<Book> comparatorBase = Comparator.comparingDouble(this::getAverageRating);
    Comparator<Book> comparator = ascending ? comparatorBase : comparatorBase.reversed();
    return allBooks.stream().sorted(comparator).limit(limit).collect(Collectors.toList());
  }

  /**
   * Retrieves a book by its ID.
   *
   * @param id The ID of the book to retrieve.
   * @return The Book object corresponding to the given ID.
   * @throws InvalidIdException if the book with the given ID is not found.
   */
  public Book findById(String id) throws InvalidIdException {
    return bookRepository.findById(id).orElseThrow(() -> new InvalidIdException("Book not found"));
  }

  /**
   * Deletes a book by its ID along with associated ratings and borrowings.
   *
   * @param id The ID of the book to delete.
   */
  public void delete(String id) {
    borrowingRepository
        .findByBookId(id)
        .forEach(borrowing -> borrowingRepository.delete(borrowing.getId()));
    ratingRepository
        .findByBookId(id)
        .forEach(rating -> ratingRepository.delete(rating.getId()));
    bookRepository.delete(id);
  }

  /**
   * Filters books based on title, author and published year.
   *
   * @param titlePart The partial title to filter by.
   * @param authorPart The partial author name to filter by.
   * @param publishedYear The published year to filter by.
   * @return An ArrayList of books matching the specified criteria.
   */
  public ArrayList<Book> filteredBooks(String titlePart, String authorPart, String publishedYear) {
    ArrayList<Book> allBooks = bookRepository.findAll();
    ArrayList<Book> filteredBooks = new ArrayList<>();

    for (Book book : allBooks) {
      boolean titleMatch = titlePart.isEmpty() || book.getTitle().toLowerCase().contains(titlePart);
      boolean authorMatch =
          authorPart.isEmpty() || book.getAuthor().toLowerCase().contains(authorPart);
      boolean publishedYearMatch =
          publishedYear.isEmpty() || book.getPublishedYear().equals(publishedYear);
      if (titleMatch && authorMatch && publishedYearMatch) {
        filteredBooks.add(book);
      }
    }
    return filteredBooks;
  }

  /**
   * Retrieves the total number of ratings for a book.
   *
   * @param book The book for which to retrieve the total number of ratings.
   * @return The total number of ratings for the specified book.
   */
  public int getTotalRatings(Book book) {
    return ratingRepository.findByBookId(book.getId()).size();
  }

  /**
   * Calculated the average rating for a book.
   *
   * @param book The book for which to calculate the average rating.
   * @return The average rating for the specified book.
   */
  public double getAverageRating(Book book) {
    double avgRating =  ratingRepository.findByBookId(book.getId()).stream()
        .mapToInt(Rating::getScore)
        .average()
        .orElse(0.0);

    return Math.round(avgRating * 10.0) / 10.0;
  }

  private String checkUniqueConstraints(Book newBook) {
    ArrayList<Book> books = bookRepository.findAll();
    for (Book book : books) {
      // Skip the book we are updating
      if (book.getId().equals(newBook.getId())) {
        continue;
      }
      if (book.getIsbn().equals(newBook.getIsbn())) {
        return "ISBN already exists";
      }
    }
    return "";
  }
}
