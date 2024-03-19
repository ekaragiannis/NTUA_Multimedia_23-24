package ntua.multimedia.libraryproject.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import ntua.multimedia.libraryproject.exceptions.InvalidBorrowingRequestException;
import ntua.multimedia.libraryproject.exceptions.InvalidIdException;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.models.entities.Borrowing;
import ntua.multimedia.libraryproject.models.entities.User;
import ntua.multimedia.libraryproject.repositories.BorrowingRepository;
import ntua.multimedia.libraryproject.utils.GenerateIdUtil;

public class BorrowingService {
  private final BorrowingRepository borrowingRepository;

  public BorrowingService(BorrowingRepository borrowingRepository) {
    this.borrowingRepository = borrowingRepository;
  }

  /**
   * Retrieves all borrowings sorted by status.
   *
   * @return A list of all borrowings sorted by status (PENDING > APPROVED).
   */
  public List<Borrowing> findAll() {
    List<Borrowing> borrowings = borrowingRepository.findAll();
    borrowings.sort(Comparator.comparing(b -> b.getApprovedAt() == null ? 0 : 1));
    return borrowings;
  }

  /**
   * Finds the number of available copies for a given book.
   *
   * @param book The book to find the available copies for.
   * @return The number of available copies for the specified book.
   */
  public int findAvailableCopies(Book book) {
    return book.getTotalCopies() - borrowingRepository.findByBookId(book.getId()).size();
  }

  /**
   * Creates or updates a borrowing record.
   *
   * @param id The id of the borrowing record. If null, a new record will be created.
   * @param userId The ID of the user borrowing the book.
   * @param bookId The ID of the book being borrowed.
   * @param approvedAt The date when the borrowing is approved.
   */
  public void createOrUpdate(String id, String userId, String bookId, LocalDate approvedAt) {
    String borrowingId = id == null ? GenerateIdUtil.generateUniqueId(borrowingRepository) : id;
    Borrowing newBorrowing = new Borrowing(borrowingId, userId, bookId, approvedAt);
    borrowingRepository.save(newBorrowing);
  }

  /**
   * Requests to borrow a book.
   *
   * @param userId The ID of the user requesting to borrow the book.
   * @param book The book to be borrowed.
   * @throws InvalidBorrowingRequestException If the book request is invalid.
   */
  public void request(String userId, Book book) throws InvalidBorrowingRequestException {
    String message = canBorrow(userId, book);
    if (!message.isEmpty()) {
      throw new InvalidBorrowingRequestException(message);
    }
    createOrUpdate(null, userId, book.getId(), null);
  }

  /**
   * Approves a borrowing request.
   *
   * @param borrowingId The ID of the borrowing request to approve.
   */
  public void approve(String borrowingId) {
    Borrowing borrowing =
        borrowingRepository
            .findById(borrowingId)
            .orElseThrow(() -> new InvalidIdException("Borrowing not found"));
    createOrUpdate(borrowingId, borrowing.getUserId(), borrowing.getBookId(), LocalDate.now());
  }

  /**
   * Deletes a borrowing request by its ID.
   *
   * @param id The ID of the borrowing to delete.
   */
  public void delete(String id) {
    borrowingRepository.delete(id);
  }

  /**
   * Retrieves all borrowings associated with a user.
   *
   * @param user The user to retrieve borrowings for.
   * @return A list of borrowings associated with the specified user.
   */
  public List<Borrowing> findByUser(User user) {
    return borrowingRepository.findByUserId(user.getId());
  }

  private String canBorrow(String userId, Book book) {
    List<Borrowing> userBorrowings = borrowingRepository.findByUserId(userId);
    int availableCopies = findAvailableCopies(book);
    List<Borrowing> borrowings = borrowingRepository.findAll();
    if (userBorrowings.size() >= 2) {
      return "You have reached maximum number of borrowings";
    }
    if (availableCopies < 1) {
      return "There are no available copies of this book";
    }
    // Check if the user have already borrowed this book and hasn't returned it
    for (Borrowing b : borrowings) {
      if (b.getBookId().equals(book.getId()) && b.getUserId().equals(userId)) {
        return "You have already borrowed this book";
      }
    }
    return "";
  }
}
