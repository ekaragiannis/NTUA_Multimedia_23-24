package ntua.multimedia.libraryproject;

import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ntua.multimedia.libraryproject.controllers.guest.GuestController;
import ntua.multimedia.libraryproject.models.entities.User;
import ntua.multimedia.libraryproject.repositories.Repositories;
import ntua.multimedia.libraryproject.services.Services;
import ntua.multimedia.libraryproject.storage.Storage;

public class MainApplication extends Application {
  final Services services = new Services(new Repositories());
  private final Storage storage = Storage.getInstance();

  public static void main(String[] args) {
    launch();
  }

  /********************************
   * TODO: ADD JAVA DOCUMENTATION *
   * TODO: ADD FAKE DATA          *
   * TODO: FINAL TESTING          *
   ********************************/

  @Override
  public void start(Stage stage) throws IOException {
//    storage.deserializeAll();
        populateFakeData();
    // TODO: Delete this
    List<User> users = services.getUserService().findAll();
    for (User user : users) {
      System.out.println(user);
    }
    FXMLLoader fxmlLoader =
        new FXMLLoader(MainApplication.class.getResource("guest/main-view.fxml"));
    fxmlLoader.setControllerFactory(controllerType -> new GuestController(services));
    Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
    stage.setTitle("Library Management System");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    storage.serializeAll();
  }

  // TODO: Remove this when everything is done
  public void populateFakeData() {
    // Add dummy users
    services
        .getUserService()
        .createOrUpdate(
            null, "jane_smith", "password456", "Jane", "Smith", "987654321", "jane@example.com");

    services
        .getUserService()
        .createOrUpdate(
            null, "john_doe", "password123", "John", "Doe", "123456789", "john@example.com");
    services
        .getUserService()
        .createOrUpdate(
            null, "alice_green", "password789", "Alice", "Green", "555555555", "alice@example.com");
    services
        .getUserService()
        .createOrUpdate(
            null, "bob_jones", "password321", "Bob", "Jones", "777777777", "bob@example.com");
    services
        .getUserService()
        .createOrUpdate(
            null, "emily_white", "passwordabc", "Emily", "White", "999999999", "emily@example.com");

    // Create categories
    services.getCategoryService().createOrUpdate(null, "Science Fiction");
    services.getCategoryService().createOrUpdate(null, "Fantasy");
    services.getCategoryService().createOrUpdate(null, "Mystery");
    services.getCategoryService().createOrUpdate(null, "Thriller");
    services.getCategoryService().createOrUpdate(null, "Romance");
    // Create additional categories
    services.getCategoryService().createOrUpdate(null, "Historical Fiction");
    services.getCategoryService().createOrUpdate(null, "Horror");

    // Define additional book details
    String[] additionalIsbns = {
      "978-0446310789", "978-0451524935", "978-0316769488", "978-0441172719", "978-0380813810",
      "978-0142437204", "978-0440212560", "978-0062073486", "978-0141185064", "978-1400079171",
      "978-0553212419", "978-0345362278", "978-0380813811", "978-0446310788", "978-0316769487",
      "978-0441172718", "978-0142437205", "978-0380813812", "978-0451524936", "978-0345362279",
      "978-0446310780", "978-0380813813", "978-0316769486", "978-0451524937", "978-0441172717",
      "978-0553212418", "978-0380813814", "978-0142437206", "978-0451524938", "978-0345362277",
      "978-0316769485", "978-0446310781", "978-0441172716", "978-0142437207", "978-0380813815",
      "978-0451524939", "978-0316769484", "978-0345362276", "978-0446310782", "978-0380813816",
      "978-0451524940", "978-0316769483", "978-0441172715", "978-0142437208", "978-0380813817",
      "978-0553212417", "978-0316769482", "978-0345362275", "978-0446310783", "978-0451524941"
    };

    String[] additionalTitles = {
      "To Kill a Mockingbird",
      "The Catcher in the Rye",
      "The Great Gatsby",
      "The Lord of the Rings",
      "Animal Farm",
      "Catch-22",
      "1984",
      "Fahrenheit 451",
      "Pride and Prejudice",
      "The Book Thief",
      "The Hobbit",
      "Neuromancer",
      "Catch-22",
      "To Kill a Mockingbird",
      "The Catcher in the Rye",
      "The Great Gatsby",
      "Animal Farm",
      "Catch-22",
      "The Lord of the Rings",
      "To Kill a Mockingbird",
      "Animal Farm",
      "The Great Gatsby",
      "The Catcher in the Rye",
      "The Lord of the Rings",
      "Foundation",
      "Animal Farm",
      "Catch-22",
      "The Catcher in the Rye",
      "Neuromancer",
      "The Great Gatsby",
      "To Kill a Mockingbird",
      "The Lord of the Rings",
      "Animal Farm",
      "Catch-22",
      "The Catcher in the Rye",
      "The Great Gatsby",
      "Neuromancer",
      "To Kill a Mockingbird",
      "Animal Farm",
      "The Great Gatsby",
      "The Catcher in the Rye",
      "The Lord of the Rings",
      "Foundation",
      "Animal Farm",
      "Foundation",
      "To Kill a Mockingbird",
      "Catch-22",
      "The Great Gatsby",
      "The Catcher in the Rye"
    };

    String[] additionalAuthors = {
      "Harper Lee", "J.D. Salinger", "F. Scott Fitzgerald", "J.R.R. Tolkien", "George Orwell",
      "Joseph Heller", "George Orwell", "Ray Bradbury", "Jane Austen", "Markus Zusak",
      "J.R.R. Tolkien", "William Gibson", "Joseph Heller", "Harper Lee", "J.D. Salinger",
      "F. Scott Fitzgerald", "George Orwell", "Joseph Heller", "J.R.R. Tolkien", "Harper Lee",
      "George Orwell", "F. Scott Fitzgerald", "J.D. Salinger", "J.R.R. Tolkien", "Isaac Asimov",
      "George Orwell", "Joseph Heller", "J.D. Salinger", "William Gibson", "F. Scott Fitzgerald",
      "Harper Lee", "J.R.R. Tolkien", "George Orwell", "Joseph Heller", "J.D. Salinger",
      "F. Scott Fitzgerald", "William Gibson", "Harper Lee", "George Orwell", "F. Scott Fitzgerald",
      "J.D. Salinger", "J.R.R. Tolkien", "Isaac Asimov", "George Orwell", "Isaac Asimov",
      "Harper Lee", "Joseph Heller", "F. Scott Fitzgerald", "J.D. Salinger"
    };
    int[] additionalTotalCopies = {
      12, 8, 10, 15, 20,
      10, 12, 8, 15, 10,
      18, 10, 12, 15, 10,
      14, 10, 12, 8, 10,
      12, 8, 10, 15, 20,
      10, 12, 8, 15, 10,
      18, 10, 12, 15, 10,
      14, 10, 12, 8, 10,
      12, 8, 10, 15, 20,
      10, 12, 8, 15, 10
    };

    String[] additionalPublishedYears = {
      "1960", "1951", "1925", "1954", "1945",
      "1961", "1949", "1953", "1813", "2005",
      "1960", "2005", "1951", "2005", "1984",
      "1951", "1925", "1945", "1961", "1954",
      "1960", "1945", "1925", "1951", "1954",
      "1953", "1945", "1961", "1951", "1984",
      "1925", "1960", "1954", "1945", "1961",
      "1951", "1925", "1984", "1960", "1945",
      "1951", "1951", "1954", "1953", "1945",
      "1953", "1960", "1961", "1925", "1951"
    };

    // Add additional books
    for (int i = 0; i < 49; i++) {
      services
          .getBookService()
          .createOrUpdate(
              null,
              additionalIsbns[i],
              additionalTitles[i],
              additionalAuthors[i],
              "Publisher",
              additionalPublishedYears[i],
              getCategory(i),
              additionalTotalCopies[i]);
    }
  }

  private String getCategory(int index) {
    return switch (index % 5) {
      case 0 -> "Science Fiction";
      case 1 -> "Fantasy";
      case 2 -> "Mystery";
      case 3 -> "Thriller";
      case 4 -> "Romance";
      default -> null;
    };
  }
}
