package ntua.multimedia.libraryproject.repositories;

import java.util.ArrayList;
import ntua.multimedia.libraryproject.models.entities.Rating;

public interface RatingRepository extends BaseRepository<Rating> {
  ArrayList<Rating> findByBookId(String id);
}
