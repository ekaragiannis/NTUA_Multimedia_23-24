package ntua.multimedia.libraryproject.repositories;

import java.util.List;
import ntua.multimedia.libraryproject.models.entities.Book;

public interface BookRepository extends BaseRepository<Book> {
  List<Book> findByCategoryId(String categoryId);
}
