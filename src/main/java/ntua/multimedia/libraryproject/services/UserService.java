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

  /**
   * Logs in a user with the provided username and password.
   *
   * @param username The username of the user.
   * @param password The password of the user.
   * @return The user object if login is successful.
   * @throws WrongCredentialsException If the provided username or password is incorrect.
   */
  public User login(String username, String password) throws WrongCredentialsException {
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

  /**
   * Retrieves all users
   *
   * @return An ArrayList containing all users.
   */
  public ArrayList<User> findAll() {
    return userRepository.findAll();
  }

  /**
   * Retrieves a user by its ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The user with the specified ID.
   * @throws InvalidIdException If the user with the specified ID is not found.
   */
  public User findById(String id) throws InvalidIdException {
    return userRepository.findById(id).orElseThrow(() -> new InvalidIdException("User not found"));
  }

  /**
   * Deletes a user by ID with associated borrowings.
   *
   * @param userId The ID of the user to delete.
   */
  public void delete(String userId) {
    borrowingRepository
        .findByUserId(userId)
        .forEach(borrowing -> borrowingRepository.delete(borrowing.getId()));
    userRepository.delete(userId);
  }

  /**
   * Creates or updates a user with the provided information.
   *
   * @param user The user to update or create. If null, a new user will be created.
   * @param username The username of the user.
   * @param password The password of the user.
   * @param firstName The first name of the user.
   * @param lastName The last name of the user.
   * @param idCardNumber The identification card number of the user.
   * @param email The email address of the user.
   * @throws DuplicateFieldException If any of the fields (username, email, idCardNumber) already
   *     exists.
   */
  public void createOrUpdate(
      User user,
      String username,
      String password,
      String firstName,
      String lastName,
      String idCardNumber,
      String email)
      throws DuplicateFieldException {
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
