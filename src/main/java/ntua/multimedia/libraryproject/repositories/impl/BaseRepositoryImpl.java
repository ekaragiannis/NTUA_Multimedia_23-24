package ntua.multimedia.libraryproject.repositories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import ntua.multimedia.libraryproject.models.BaseModel;
import ntua.multimedia.libraryproject.repositories.BaseRepository;

public abstract class BaseRepositoryImpl<T extends BaseModel> implements BaseRepository<T> {
  protected HashMap<String, T> entities;

  public BaseRepositoryImpl(HashMap<String, T> entities) {
    this.entities = entities;
  }

  public ArrayList<T> findAll() {
    return new ArrayList<>(entities.values());
  }

  public void save(T entity) {
    entities.put(entity.getId(), entity);
  }

  public Optional<T> findById(String id) {
    return Optional.ofNullable(entities.get(id));
  }

  public void delete(String id) {
    entities.remove(id);
  }

  public boolean exists(String id) {
    return entities.containsKey(id);
  }
}
