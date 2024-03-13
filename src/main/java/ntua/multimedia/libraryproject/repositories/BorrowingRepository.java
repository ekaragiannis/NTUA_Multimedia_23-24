package ntua.multimedia.libraryproject.repositories;

import java.util.List;
import ntua.multimedia.libraryproject.models.entities.Borrowing;

public interface BorrowingRepository extends BaseRepository<Borrowing> {
  List<Borrowing> findByUserId(String userId);

  List<Borrowing> findByBookId(String bookId);
}
