package ntua.multimedia.libraryproject.repositories.impl;

import java.util.ArrayList;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.repositories.BookRepository;
import ntua.multimedia.libraryproject.storage.Storage;

public class BookRepositoryImpl extends BaseRepositoryImpl<Book> implements BookRepository {
  public BookRepositoryImpl() {
    super(Storage.getInstance().getBooks());
  }

  @Override
  public ArrayList<Book> findByCategoryId(String categoryId) {
    ArrayList<Book> books = new ArrayList<>();
    for (Book book : entities.values()) {
      if (book.getCategoryId().equals(categoryId)) {
        books.add(book);
      }
    }
    return books;
  }
}
