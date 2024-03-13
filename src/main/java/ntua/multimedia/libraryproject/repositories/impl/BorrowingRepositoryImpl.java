package ntua.multimedia.libraryproject.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import ntua.multimedia.libraryproject.models.entities.Borrowing;
import ntua.multimedia.libraryproject.repositories.BorrowingRepository;
import ntua.multimedia.libraryproject.storage.Storage;

public class BorrowingRepositoryImpl extends BaseRepositoryImpl<Borrowing>
    implements BorrowingRepository {
  public BorrowingRepositoryImpl() {
    super(Storage.getInstance().getBorrowings());
  }

  @Override
  public List<Borrowing> findByUserId(String userId) {
    List<Borrowing> borrowings = new ArrayList<>();
    for (Borrowing borrowing : entities.values()) {
      if (borrowing.getUserId().equals(userId)) {
        borrowings.add(borrowing);
      }
    }
    return borrowings;
  }

  @Override
  public List<Borrowing> findByBookId(String bookId) {
    List<Borrowing> borrowings = new ArrayList<>();
    for (Borrowing borrowing : entities.values()) {
      if (borrowing.getBookId().equals(bookId)) {
        borrowings.add(borrowing);
      }
    }
    return borrowings;
  }
}
