package ntua.multimedia.libraryproject.repositories.impl;

import java.util.Optional;
import ntua.multimedia.libraryproject.models.entities.User;
import ntua.multimedia.libraryproject.repositories.UserRepository;
import ntua.multimedia.libraryproject.storage.Storage;

public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {

  public UserRepositoryImpl() {
    super(Storage.getInstance().getUsers());
  }

  public Optional<User> findByUsername(String username) {
    for (User user : entities.values()) {
      if (user.getUsername().equals(username)) {
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }
}
