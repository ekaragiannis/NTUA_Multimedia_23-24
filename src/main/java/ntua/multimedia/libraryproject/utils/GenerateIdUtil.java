package ntua.multimedia.libraryproject.utils;

import java.util.UUID;
import ntua.multimedia.libraryproject.models.BaseModel;
import ntua.multimedia.libraryproject.repositories.BaseRepository;

public class GenerateIdUtil {
  public static <T extends BaseModel> String generateUniqueId(BaseRepository<T> repository) {
    String id;
    do {
      id = UUID.randomUUID().toString();
    } while (repository.exists(id));
    return id;
  }
}
