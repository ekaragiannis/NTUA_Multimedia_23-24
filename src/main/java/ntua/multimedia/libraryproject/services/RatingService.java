package ntua.multimedia.libraryproject.services;

import java.util.ArrayList;
import ntua.multimedia.libraryproject.exceptions.PermissionDeniedException;
import ntua.multimedia.libraryproject.models.entities.*;
import ntua.multimedia.libraryproject.repositories.BorrowingRepository;
import ntua.multimedia.libraryproject.repositories.RatingRepository;
import ntua.multimedia.libraryproject.utils.GenerateIdUtil;

public class RatingService {
  private final RatingRepository ratingRepository;
  private final BorrowingRepository borrowingRepository;

  public RatingService(RatingRepository ratingRepository, BorrowingRepository borrowingRepository) {
    this.ratingRepository = ratingRepository;
    this.borrowingRepository = borrowingRepository;
  }

  public void createOrUpdate(
      Rating rating, String userId, String bookId, int score, String description) {
    String ratingId =
        rating == null ? GenerateIdUtil.generateUniqueId(ratingRepository) : rating.getId();
    if (rating == null && !hasRatingPermission(userId, bookId)) {
      throw new PermissionDeniedException(
          "Your are not allowed to rate a book if you haven't borrowed it first.");
    }
    Rating newRating = new Rating(ratingId, userId, bookId, score, description);
    ratingRepository.save(newRating);
  }

  public ArrayList<Rating> findByBook(Book book) {
    return ratingRepository.findByBookId(book.getId());
  }

  private boolean hasRatingPermission(String userId, String bookId) {
    return borrowingRepository.findAll().stream()
        .filter(borrowing -> borrowing.getUserId().equals(userId))
        .filter(borrowing -> borrowing.getBookId().equals(bookId))
        .anyMatch(borrowing -> borrowing.getApprovedAt() != null);
  }

  public void delete(String id) {
    ratingRepository.delete(id);
  }
}
