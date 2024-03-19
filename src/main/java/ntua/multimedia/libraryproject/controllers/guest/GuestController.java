package ntua.multimedia.libraryproject.controllers.guest;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ntua.multimedia.libraryproject.controllers.admin.AdminController;
import ntua.multimedia.libraryproject.controllers.user.UserController;
import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
import ntua.multimedia.libraryproject.exceptions.WrongCredentialsException;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.models.entities.User;
import ntua.multimedia.libraryproject.models.tableRows.BookRow;
import ntua.multimedia.libraryproject.services.Services;
import ntua.multimedia.libraryproject.utils.custom.components.CustomGridPane;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.SuccessAlert;

public class GuestController {

  private final Services services;
  @FXML private ToggleButton loginToggleBtn;
  @FXML private ToggleButton signupToggleBtn;
  @FXML private ToggleButton homeToggleBtn;
  @FXML private VBox contentVBox;

  public GuestController(Services services) {
    this.services = services;
  }

  public void initialize() {
    ToggleGroup toggleGroup = new ToggleGroup();
    homeToggleBtn.setToggleGroup(toggleGroup);
    homeToggleBtn.setSelected(true);
    signupToggleBtn.setToggleGroup(toggleGroup);
    loginToggleBtn.setToggleGroup(toggleGroup);

    toggleGroup
        .selectedToggleProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue == homeToggleBtn) {
                loadTopFiveBooks();
              } else if (newValue == loginToggleBtn) {
                loadLoginForm();
              } else if (newValue == signupToggleBtn) {
                loadSingupForm();
              }
            });

    loadTopFiveBooks();
  }

  public void loadTopFiveBooks() {
    contentVBox.getChildren().clear();

    List<Book> books = services.getBookService().findAll(false, 5);
    ObservableList<BookRow> bookTableData = FXCollections.observableArrayList();

    for (Book book : books) {
      double avgRating = services.getBookService().getAverageRating(book);
      int totalRatings = services.getBookService().getTotalRatings(book);
      bookTableData.add(
          new BookRow(
              book.getId(),
              book.getTitle(),
              book.getAuthor(),
              book.getIsbn(),
              Double.toString(avgRating),
              Integer.toString(totalRatings)));
    }

    TableView<BookRow> bookTableView = new TableView<>();

    TableColumn<BookRow, String> isbnColumn = new TableColumn<>("ISBN");
    isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
    bookTableView.getColumns().add(isbnColumn);

    TableColumn<BookRow, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    bookTableView.getColumns().add(titleColumn);

    TableColumn<BookRow, String> authorColumn = new TableColumn<>("Author");
    authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
    bookTableView.getColumns().add(authorColumn);

    TableColumn<BookRow, String> avgRatingColumn = new TableColumn<>("Rating Score");
    avgRatingColumn.setCellValueFactory(cellData -> cellData.getValue().avgRatingScoreProperty());
    bookTableView.getColumns().add(avgRatingColumn);

    TableColumn<BookRow, String> totalRatingsColumn = new TableColumn<>("Total Ratings");
    totalRatingsColumn.setCellValueFactory(cellData -> cellData.getValue().totalRatingsProperty());
    bookTableView.getColumns().add(totalRatingsColumn);

    bookTableView.setItems(bookTableData);
    bookTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

    for (TableColumn<BookRow, ?> column : bookTableView.getColumns()) {
      column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11pt");
    }

    VBox.setVgrow(bookTableView, Priority.ALWAYS);
    contentVBox.getChildren().add(bookTableView);
  }

  public void loadLoginForm() {
    contentVBox.getChildren().clear();
    CustomGridPane gridPane = new CustomGridPane(false);

    Label usernameLabel = new Label("Username:");
    TextField usernameField = new TextField();

    Label passwordLabel = new Label("Password:");
    PasswordField passwordField = new PasswordField();

    Button loginButton = new Button("Login");
    loginButton.setOnAction(e -> handleLoginSubmit(usernameField, passwordField));

    gridPane.add(usernameLabel, 0, 0);
    gridPane.add(usernameField, 1, 0);
    gridPane.add(passwordLabel, 0, 1);
    gridPane.add(passwordField, 1, 1);

    HBox hbox = new HBox(loginButton);
    hbox.setAlignment(javafx.geometry.Pos.CENTER);
    gridPane.add(hbox, 0, 2, 2, 1);

    contentVBox.getChildren().add(gridPane);
  }

  public void loadSingupForm() {
    contentVBox.getChildren().clear();
    CustomGridPane gridPane = new CustomGridPane(false);

    Label usernameLabel = new Label("Username:");
    TextField usernameField = new TextField();

    Label passwordLabel = new Label("Password:");
    PasswordField passwordField = new PasswordField();

    Label confirmPasswordLabel = new Label("Confirm password:");
    PasswordField confirmPasswordField = new PasswordField();

    Label firstNameLabel = new Label("First name:");
    TextField firstNameField = new TextField();

    Label lastNameLabel = new Label("Last name:");
    TextField lastNameField = new TextField();

    Label emailLabel = new Label("Email:");
    TextField emailField = new TextField();

    Label idCardNumberLabel = new Label("Identity number:");
    TextField idCardNumberField = new TextField();

    Button signupButton = new Button("Sign Up");
    signupButton.setOnAction(
        e ->
            handleSignupSubmit(
                usernameField,
                passwordField,
                confirmPasswordField,
                firstNameField,
                lastNameField,
                idCardNumberField,
                emailField));

    gridPane.add(usernameLabel, 0, 0);
    gridPane.add(usernameField, 1, 0);

    gridPane.add(passwordLabel, 0, 1);
    gridPane.add(passwordField, 1, 1);

    gridPane.add(confirmPasswordLabel, 0, 2);
    gridPane.add(confirmPasswordField, 1, 2);

    gridPane.add(firstNameLabel, 0, 3);
    gridPane.add(firstNameField, 1, 3);

    gridPane.add(lastNameLabel, 0, 4);
    gridPane.add(lastNameField, 1, 4);

    gridPane.add(emailLabel, 0, 5);
    gridPane.add(emailField, 1, 5);

    gridPane.add(idCardNumberLabel, 0, 6);
    gridPane.add(idCardNumberField, 1, 6);

    HBox hbox = new HBox(signupButton);
    hbox.setAlignment(javafx.geometry.Pos.CENTER);
    gridPane.add(hbox, 0, 7, 2, 1);

    contentVBox.getChildren().addAll(gridPane);
  }

  private void handleLoginSubmit(TextField usernameField, PasswordField passwordField) {
    try {
      if (usernameField.getText().equals("medialab")
          && passwordField.getText().equals("medialab_2024")) {
        setAdminScene();
      } else {
        User user =
            services.getUserService().login(usernameField.getText(), passwordField.getText());
        setUserScene(user);
      }
    } catch (WrongCredentialsException e) {

      FailureAlert alert = new FailureAlert(e.getMessage());
      alert.showAndWait();
    }
  }

  @FXML
  private void setAdminScene() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          getClass().getResource("/ntua/multimedia/libraryproject/admin/main-view.fxml"));
      loader.setControllerFactory(controllerType -> new AdminController(services));
      Scene adminScene = new Scene(loader.load());
      Stage stage = (Stage) loginToggleBtn.getScene().getWindow();
      stage.setScene(adminScene);
      stage.setTitle("Admin dashboard");
      stage.show();
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong. Please try again");
      alert.showAndWait();
    }
  }

  private void setUserScene(User user) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          getClass().getResource("/ntua/multimedia/libraryproject/user/main-view.fxml"));
      loader.setControllerFactory(controllerType -> new UserController(services, user.getId()));
      Scene userScene = new Scene(loader.load());
      Stage stage = (Stage) loginToggleBtn.getScene().getWindow();
      stage.setScene(userScene);
      stage.setTitle("User dashboard");
      stage.show();
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong. Please try again");
      alert.showAndWait();
    }
  }

  private void handleSignupSubmit(
      TextField usernameField,
      PasswordField passwordField,
      PasswordField confirmPasswordField,
      TextField firstNameField,
      TextField lastNameField,
      TextField idCardNumberField,
      TextField emailField) {
    String msg =
        validateFields(
            passwordField.getText(), confirmPasswordField.getText(), emailField.getText());
    if (!msg.isEmpty()) {
      FailureAlert alert = new FailureAlert(msg);
      alert.showAndWait();
      return;
    }
    try {
      services
          .getUserService()
          .createOrUpdate(
              null,
              usernameField.getText(),
              passwordField.getText(),
              firstNameField.getText(),
              lastNameField.getText(),
              idCardNumberField.getText(),
              emailField.getText());
      SuccessAlert alert = new SuccessAlert("User was successfully created");
      alert.showAndWait();
      usernameField.setText("");
      passwordField.setText("");
      confirmPasswordField.setText("");
      firstNameField.setText("");
      lastNameField.setText("");
      idCardNumberField.setText("");
      emailField.setText("");
    } catch (DuplicateFieldException e) {
      FailureAlert alert = new FailureAlert(e.getMessage());
      alert.showAndWait();
    }
  }

  private String validateFields(String password, String confirmPassword, String email) {
    String message = "";
    if (password.length() < 8) {
      message += "Password must be at least 8 characters long\n";
    }
    if (!password.equals(confirmPassword)) {
      message += "Passwords do not match\n";
    }
    if (!email.contains("@")) {
      message += "Invalid email\n";
    }
    return message;
  }
}
