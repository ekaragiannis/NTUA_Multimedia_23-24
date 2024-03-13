package ntua.multimedia.libraryproject.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
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

  public ArrayList<Book> findAll() {
    return bookRepository.findAll();
  }

  public List<Book> findAll(boolean ascending, int limit) {
    ArrayList<Book> allBooks = bookRepository.findAll();
    Comparator<Book> comparatorBase = Comparator.comparingDouble(this::getAverageRating);
    Comparator<Book> comparator = ascending ? comparatorBase : comparatorBase.reversed();
    return allBooks.stream().sorted(comparator).limit(limit).collect(Collectors.toList());
  }

  public Book findById(String id) {
    return bookRepository
        .findById(id)
        .orElseThrow(() -> new InvalidReferenceException("Book not found"));
  }

  public void delete(String id) {
    borrowingRepository
        .findByBookId(id)
        .forEach(borrowing -> borrowingRepository.delete(borrowing.getId()));
    ratingRepository
        .findByBookId(id)
        .forEach(borrowing -> borrowingRepository.delete(borrowing.getId()));
    bookRepository.delete(id);
  }

  // titlePart and authorPart are given in lowercase
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

  public int getTotalRatings(Book book) {
    return ratingRepository.findByBookId(book.getId()).size();
  }

  public double getAverageRating(Book book) {
    return ratingRepository.findByBookId(book.getId()).stream()
        .mapToInt(Rating::getScore)
        .average()
        .orElse(0.0);
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
