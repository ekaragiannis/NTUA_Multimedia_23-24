package ntua.multimedia.libraryproject.services;

import java.util.ArrayList;
import java.util.List;

import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
import ntua.multimedia.libraryproject.exceptions.InvalidIdException;
import ntua.multimedia.libraryproject.exceptions.InvalidReferenceException;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.models.entities.Category;
import ntua.multimedia.libraryproject.repositories.BookRepository;
import ntua.multimedia.libraryproject.repositories.BorrowingRepository;
import ntua.multimedia.libraryproject.repositories.CategoryRepository;
import ntua.multimedia.libraryproject.repositories.RatingRepository;
import ntua.multimedia.libraryproject.utils.GenerateIdUtil;

public class CategoryService {

  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  private final BorrowingRepository borrowingRepository;
  private final RatingRepository ratingRepository;

  public CategoryService(
      CategoryRepository categoryRepository,
      BookRepository bookRepository,
      BorrowingRepository borrowingRepository,
      RatingRepository ratingRepository) {
    this.categoryRepository = categoryRepository;
    this.ratingRepository = ratingRepository;
    this.borrowingRepository = borrowingRepository;
    this.bookRepository = bookRepository;
  }

  /**
   * Crates or updates a category with the provided information.
   *
   * @param category The category to create or update. If null, a new category will be created.
   * @param title The title of the category.
   */
  public void createOrUpdate(Category category, String title) {
    String categoryId =
        category == null ? GenerateIdUtil.generateUniqueId(categoryRepository) : category.getId();
    Category newCategory = new Category(categoryId, title);
    String msg = checkUniqueConstraints(newCategory);
    if (!msg.isEmpty()) {
      throw new DuplicateFieldException(msg);
    }
    categoryRepository.save(newCategory);
  }

  /**
   * Retrieves all categories.
   *
   * @return An ArrayList containing all categories.
   */
  public ArrayList<Category> findAll() {
    return categoryRepository.findAll();
  }

  /**
   * Retrieves the category for a given book.
   *
   * @param book The book to retrieve the category for.
   * @return The category associated with the specified book.
   * @throws InvalidReferenceException If the category of the book is not found.
   */
  public Category getBookCategory(Book book) throws InvalidReferenceException {
    return categoryRepository
        .findById(book.getCategoryId())
        .orElseThrow(() -> new InvalidReferenceException("Category not found"));
  }

  /**
   * Retrieves a category by its ID.
   *
   * @param id The ID of the category to retrieve.
   * @return The category with the specified ID.
   * @throws InvalidIdException If the category with the specified ID is not found.
   */
  public Category findById(String id) throws InvalidIdException {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new InvalidIdException("Category not found"));
  }

  /**
   * Deletes a category and all associated books.
   *
   * @param id The ID of the category to delete.
   */
  public void delete(String id) {
    List<Book> books = bookRepository.findByCategoryId(id);
    for (Book book : books) {
      String bookId = book.getId();
      ratingRepository.findByBookId(bookId).forEach(r -> ratingRepository.delete(r.getId()));
      borrowingRepository.findByBookId(bookId).forEach(b -> borrowingRepository.delete(b.getId()));
      bookRepository.delete(bookId);
    }
    categoryRepository.delete(id);
  }

  private String checkUniqueConstraints(Category newCategory) {
    ArrayList<Category> categories = categoryRepository.findAll();
    for (Category category : categories) {
      // Skip the category we are updating
      if (category.getId().equals(newCategory.getId())) {
        continue;
      }
      if (category.getTitle().equals(newCategory.getTitle())) {
        return "Category already exists";
      }
    }
    return "";
  }
}
