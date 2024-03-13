package ntua.multimedia.libraryproject.storage;

import java.io.*;
import java.util.HashMap;
import ntua.multimedia.libraryproject.models.BaseModel;
import ntua.multimedia.libraryproject.models.entities.*;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;

public class Storage {
  private static final String USERS_FILE = "users.ser";
  private static final String BOOKS_FILE = "books.ser";
  private static final String CATEGORIES_FILE = "categories.ser";
  private static final String BORROWINGS_FILE = "borrowings.ser";
  private static final String RATINGS_FILE = "ratings.ser";
  private static Storage instance;
  private final HashMap<String, User> users = new HashMap<>();
  private final HashMap<String, Category> categories = new HashMap<>();
  private final HashMap<String, Book> books = new HashMap<>();
  private final HashMap<String, Borrowing> borrowings = new HashMap<>();
  private final HashMap<String, Rating> ratings = new HashMap<>();

  private Storage() {}

  public static Storage getInstance() {
    if (instance == null) {
      instance = new Storage();
    }
    return instance;
  }

  public HashMap<String, User> getUsers() {
    return users;
  }

  public HashMap<String, Category> getCategories() {
    return categories;
  }

  public HashMap<String, Book> getBooks() {
    return books;
  }

  public HashMap<String, Borrowing> getBorrowings() {
    return borrowings;
  }

  public HashMap<String, Rating> getRatings() {
    return ratings;
  }

  public void deserializeAll() {
    deserializeMap(USERS_FILE, users);
    deserializeMap(CATEGORIES_FILE, categories);
    deserializeMap(BOOKS_FILE, books);
    deserializeMap(BORROWINGS_FILE, borrowings);
    deserializeMap(RATINGS_FILE, ratings);
  }

  public void serializeAll() {
    serializeMap(USERS_FILE, users);
    serializeMap(CATEGORIES_FILE, categories);
    serializeMap(BOOKS_FILE, books);
    serializeMap(BORROWINGS_FILE, borrowings);
    serializeMap(RATINGS_FILE, ratings);
  }

  private <V extends BaseModel> void deserializeMap(String filePath, HashMap<String, V> map) {
    File file = new File("medialab/" + filePath);
    if (!file.exists()) {
      return;
    }
    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
      map.clear();
      @SuppressWarnings("unchecked")
      HashMap<String, V> deserializedMap = (HashMap<String, V>) inputStream.readObject();
      map.putAll(deserializedMap);
    } catch (IOException | ClassNotFoundException e) {
      FailureAlert alert = new FailureAlert("Something went wrong retrieving the data");
      alert.showAndWait();
    }
  }

  private <V extends BaseModel> void serializeMap(String filePath, HashMap<String, V> map) {
    try (ObjectOutputStream outputStream =
        new ObjectOutputStream(new FileOutputStream("medialab/" + filePath))) {
      outputStream.writeObject(map);
    } catch (IOException e) {
      FailureAlert alert = new FailureAlert("Something went wrong saving the data");
      alert.showAndWait();
    }
  }
}
