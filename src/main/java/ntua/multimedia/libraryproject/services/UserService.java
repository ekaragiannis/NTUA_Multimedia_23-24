package ntua.multimedia.libraryproject.services;

import java.util.ArrayList;
import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
import ntua.multimedia.libraryproject.exceptions.InvalidIdException;
import ntua.multimedia.libraryproject.exceptions.WrongCredentialsException;
import ntua.multimedia.libraryproject.models.entities.User;
import ntua.multimedia.libraryproject.repositories.BorrowingRepository;
import ntua.multimedia.libraryproject.repositories.UserRepository;
import ntua.multimedia.libraryproject.utils.EncryptionUtil;
import ntua.multimedia.libraryproject.utils.GenerateIdUtil;

public class UserService {
  private final UserRepository userRepository;
  private final BorrowingRepository borrowingRepository;

  public UserService(UserRepository userRepository, BorrowingRepository borrowingRepository) {
    this.userRepository = userRepository;
    this.borrowingRepository = borrowingRepository;
  }

  public User login(String username, String password) {
    return userRepository
        .findByUsername(username)
        .map(
            user -> {
              if (password.equals(EncryptionUtil.decrypt(user.getPassword()))) {
                return user;
              } else {
                throw new WrongCredentialsException("Wrong password");
              }
            })
        .orElseThrow(() -> new WrongCredentialsException("Username not found"));
  }

  public ArrayList<User> findAll() {
    return userRepository.findAll();
  }

  public User findById(String id) {
    return userRepository.findById(id).orElseThrow(() -> new InvalidIdException("User not found"));
  }

  public void delete(String id) {
    borrowingRepository
        .findByBookId(id)
        .forEach(borrowing -> borrowingRepository.delete(borrowing.getId()));
    userRepository.delete(id);
  }

  public void createOrUpdate(
      User user,
      String username,
      String password,
      String firstName,
      String lastName,
      String idCardNumber,
      String email) {
    String userId = user == null ? GenerateIdUtil.generateUniqueId(userRepository) : user.getId();
    String encryptedPassword = EncryptionUtil.encrypt(password);
    User newUser =
        new User(userId, username, encryptedPassword, firstName, lastName, idCardNumber, email);
    String msg = checkUniqueConstraints(newUser);
    if (!msg.isEmpty()) {
      throw new DuplicateFieldException(msg);
    }
    userRepository.save(newUser);
  }

  private String checkUniqueConstraints(User newUser) {
    ArrayList<User> users = userRepository.findAll();
    for (User user : users) {
      // Skip the user that we are updating
      if (user.getId().equals(newUser.getId())) {
        continue;
      }
      if (user.getUsername().equals(newUser.getUsername())) {
        return "Username already exists";
      }
      if (user.getEmail().equals(newUser.getEmail())) {
        return "Email already exists";
      }
      if (user.getIdCardNumber().equals(newUser.getIdCardNumber())) {
        return "Id Card Number already exists";
      }
    }
    return "";
  }
}
