package ntua.multimedia.libraryproject.repositories;

import java.util.ArrayList;
import java.util.Optional;
import ntua.multimedia.libraryproject.models.BaseModel;

public interface BaseRepository<T extends BaseModel> {
  ArrayList<T> findAll();

  void save(T entity);

  Optional<T> findById(String id);

  void delete(String id);

  boolean exists(String id);
}
