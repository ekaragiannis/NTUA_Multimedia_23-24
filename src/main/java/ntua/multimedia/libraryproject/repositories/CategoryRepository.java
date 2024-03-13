package ntua.multimedia.libraryproject.repositories;

import java.util.Optional;
import ntua.multimedia.libraryproject.models.entities.Category;

public interface CategoryRepository extends BaseRepository<Category> {
  Optional<Category> findByTitle(String title);
}
