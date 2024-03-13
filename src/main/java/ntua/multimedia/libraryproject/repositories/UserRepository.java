package ntua.multimedia.libraryproject.repositories;

import java.util.Optional;
import ntua.multimedia.libraryproject.models.entities.User;

public interface UserRepository extends BaseRepository<User> {
  Optional<User> findByUsername(String username);
}
