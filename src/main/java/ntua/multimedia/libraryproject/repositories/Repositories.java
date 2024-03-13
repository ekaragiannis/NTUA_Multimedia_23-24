package ntua.multimedia.libraryproject.repositories;

import ntua.multimedia.libraryproject.repositories.impl.*;

public class Repositories {
  private final UserRepository userRepository = new UserRepositoryImpl();
  private final BookRepository bookRepository = new BookRepositoryImpl();
  private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();
  private final BorrowingRepository borrowingRepository = new BorrowingRepositoryImpl();
  private final RatingRepository ratingRepository = new RatingRepositoryImpl();

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public BookRepository getBookRepository() {
    return bookRepository;
  }

  public CategoryRepository getCategoryRepository() {
    return categoryRepository;
  }

  public BorrowingRepository getBorrowingRepository() {
    return borrowingRepository;
  }

  public RatingRepository getRatingRepository() {
    return ratingRepository;
  }
}
