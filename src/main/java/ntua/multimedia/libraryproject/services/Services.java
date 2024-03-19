package ntua.multimedia.libraryproject.services;

import ntua.multimedia.libraryproject.repositories.Repositories;

public class Services {

  private final UserService userService;
  private final BookService bookService;
  private final CategoryService categoryService;
  private final BorrowingService borrowingService;
  private final RatingService ratingService;

  public Services(Repositories repositories) {
    this.userService =
        new UserService(repositories.getUserRepository(), repositories.getBorrowingRepository());
    this.bookService =
        new BookService(
            repositories.getBookRepository(),
            repositories.getCategoryRepository(),
            repositories.getBorrowingRepository(),
            repositories.getRatingRepository());
    this.categoryService =
        new CategoryService(
            repositories.getCategoryRepository(),
            repositories.getBookRepository(),
            repositories.getBorrowingRepository(),
            repositories.getRatingRepository());
    this.borrowingService = new BorrowingService(repositories.getBorrowingRepository());
    this.ratingService =
        new RatingService(
            repositories.getRatingRepository(), repositories.getBorrowingRepository());
  }

  public UserService getUserService() {
    return userService;
  }

  public BookService getBookService() {
    return bookService;
  }

  public CategoryService getCategoryService() {
    return categoryService;
  }

  public BorrowingService getBorrowingService() {
    return borrowingService;
  }

  public RatingService getRatingService() {
    return ratingService;
  }
}
