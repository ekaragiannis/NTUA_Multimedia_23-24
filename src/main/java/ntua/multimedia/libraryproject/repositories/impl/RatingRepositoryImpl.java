package ntua.multimedia.libraryproject.repositories.impl;

import java.util.ArrayList;
import ntua.multimedia.libraryproject.models.entities.Rating;
import ntua.multimedia.libraryproject.repositories.RatingRepository;
import ntua.multimedia.libraryproject.storage.Storage;

public class RatingRepositoryImpl extends BaseRepositoryImpl<Rating> implements RatingRepository {
  public RatingRepositoryImpl() {
    super(Storage.getInstance().getRatings());
  }

  @Override
  public ArrayList<Rating> findByBookId(String id) {
    ArrayList<Rating> ratings = new ArrayList<>();
    for (Rating rating : entities.values()) {
      if (rating.getBookId().equals(id)) {
        ratings.add(rating);
      }
    }
    return ratings;
  }
}
