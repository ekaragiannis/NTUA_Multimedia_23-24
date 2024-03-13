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

  public List<Borrowing> findAll() {
    List<Borrowing> borrowings = borrowingRepository.findAll();
    // Sort borrowings with PENDING > APPROVED order
    borrowings.sort(Comparator.comparing(b -> b.getApprovedAt() == null ? 0 : 1));
    return borrowings;
  }

  public int findAvailableCopies(Book book) {
    return book.getTotalCopies() - borrowingRepository.findByBookId(book.getId()).size();
  }

  public void createOrUpdate(String id, String userId, String bookId, LocalDate approvedAt) {
    String borrowingId = id == null ? GenerateIdUtil.generateUniqueId(borrowingRepository) : id;
    Borrowing newBorrowing = new Borrowing(borrowingId, userId, bookId, approvedAt);
    borrowingRepository.save(newBorrowing);
  }

  public void request(String userId, Book book) {
    String message = canBorrow(userId, book);
    if (!message.isEmpty()) {
      throw new InvalidBorrowingRequestException(message);
    }
    createOrUpdate(null, userId, book.getId(), null);
  }

  public void approve(String borrowingId) {
    Borrowing borrowing =
        borrowingRepository
            .findById(borrowingId)
            .orElseThrow(() -> new InvalidIdException("Borrowing not found"));
    createOrUpdate(borrowingId, borrowing.getUserId(), borrowing.getBookId(), LocalDate.now());
  }

  public void delete(String id) {
    borrowingRepository.delete(id);
  }

  public List<Borrowing> findByUser(User user) {
    return borrowingRepository.findByUserId(user.getId());
  }

  public String canBorrow(String userId, Book book) {
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
