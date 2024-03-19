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

  /**
   * Creates or updates a rating for a book with the provided information.
   *
   * @param rating The rating to create or update. If null, a new rating will be created.
   * @param userId The ID of the user giving the rating.
   * @param bookId The ID of the book being rated.
   * @param score The score given for the book.
   * @param description Optional description or review for the book.
   * @throws PermissionDeniedException If the used doesn't have permissions to rate the book.
   */
  public void createOrUpdate(
      Rating rating, String userId, String bookId, int score, String description)
      throws PermissionDeniedException {
    String ratingId =
        rating == null ? GenerateIdUtil.generateUniqueId(ratingRepository) : rating.getId();
    if (rating == null && !hasRatingPermission(userId, bookId)) {
      throw new PermissionDeniedException(
          "Your are not allowed to rate a book if you haven't borrowed it first.");
    }
    Rating newRating = new Rating(ratingId, userId, bookId, score, description);
    ratingRepository.save(newRating);
  }

  /**
   * Retrieves all ratings for a given book.
   *
   * @param book The book to retrieve ratings for.
   * @return An ArrayList containing all ratings for the specified book.
   */
  public ArrayList<Rating> findByBook(Book book) {
    return ratingRepository.findByBookId(book.getId());
  }

  /**
   * Deletes a rating by its ID.
   *
   * @param id The ID of the rating to delete.
   */
  public void delete(String id) {
    ratingRepository.delete(id);
  }

  private boolean hasRatingPermission(String userId, String bookId) {
    return borrowingRepository.findAll().stream()
        .filter(borrowing -> borrowing.getUserId().equals(userId))
        .filter(borrowing -> borrowing.getBookId().equals(bookId))
        .anyMatch(borrowing -> borrowing.getApprovedAt() != null);
  }
}
