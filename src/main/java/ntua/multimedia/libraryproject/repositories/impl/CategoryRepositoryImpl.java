package ntua.multimedia.libraryproject.repositories.impl;

import ntua.multimedia.libraryproject.models.entities.Category;
import ntua.multimedia.libraryproject.repositories.CategoryRepository;
import ntua.multimedia.libraryproject.storage.Storage;

import java.util.Optional;

public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category>
    implements CategoryRepository {

  public CategoryRepositoryImpl() {
    super(Storage.getInstance().getCategories());
  }

  public Optional<Category> findByTitle(String title) {
    return entities.values().stream()
        .filter(category -> category.getTitle().equals(title))
        .findFirst();
  }
}
