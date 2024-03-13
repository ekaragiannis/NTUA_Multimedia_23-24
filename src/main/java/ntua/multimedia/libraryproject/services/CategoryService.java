package ntua.multimedia.libraryproject.services;

import java.util.ArrayList;
import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
import ntua.multimedia.libraryproject.exceptions.InvalidIdException;
import ntua.multimedia.libraryproject.exceptions.InvalidReferenceException;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.models.entities.Category;
import ntua.multimedia.libraryproject.repositories.BookRepository;
import ntua.multimedia.libraryproject.repositories.CategoryRepository;
import ntua.multimedia.libraryproject.utils.GenerateIdUtil;

public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final BookRepository bookRepository;

  public CategoryService(CategoryRepository categoryRepository, BookRepository bookRepository) {
    this.categoryRepository = categoryRepository;
    this.bookRepository = bookRepository;
  }

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

  public ArrayList<Category> findAll() {
    return categoryRepository.findAll();
  }

  public Category getBookCategory(Book book) {
    return categoryRepository
        .findById(book.getCategoryId())
        .orElseThrow(() -> new InvalidReferenceException("Category not found"));
  }

  public Category findById(String id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new InvalidIdException("Category not found"));
  }

  public void delete(String id) {
    bookRepository.findByCategoryId(id).forEach(book -> bookRepository.delete(book.getId()));
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
