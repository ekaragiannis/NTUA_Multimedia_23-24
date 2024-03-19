package ntua.multimedia.libraryproject.controllers.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import ntua.multimedia.libraryproject.exceptions.InvalidBorrowingRequestException;
import ntua.multimedia.libraryproject.models.entities.*;
import ntua.multimedia.libraryproject.models.tableRows.BookRow;
import ntua.multimedia.libraryproject.services.Services;
import ntua.multimedia.libraryproject.utils.LogoutUtil;
import ntua.multimedia.libraryproject.utils.PopupStageUtil;
import ntua.multimedia.libraryproject.utils.custom.components.CustomGridPane;
import ntua.multimedia.libraryproject.utils.custom.components.WrapLabel;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.SuccessAlert;

public class UserController {

  private final Services services;
  private final String currentUserId;
  @FXML private BorderPane borderPane;
  @FXML private Button settingsButton;
  @FXML private TableView<BookRow> bookTableView;
  @FXML private TableColumn<BookRow, String> titleColumn;
  @FXML private TableColumn<BookRow, String> authorColumn;
  @FXML private TableColumn<BookRow, String> isbnColumn;
  @FXML private TableColumn<BookRow, String> avgRatingScoreColumn;
  @FXML private TableColumn<BookRow, String> totalRatingColumn;
  @FXML private TextField titleFilterField;
  @FXML private TextField authorFilterField;
  @FXML private TextField publishedYearFilterField;
  @FXML private VBox leftVBox;
  @FXML private VBox rightVBox;
  @FXML private VBox innerRightVBox;
  @FXML private ToggleButton detailsToggleBtn;
  @FXML private ToggleButton ratingsToggleBtn;

  public UserController(Services services, String currentUserId) {
    this.services = services;
    this.currentUserId = currentUserId;
  }

  @FXML
  public void initialize() {
    titleFilterField.setText("");
    authorFilterField.setText("");
    publishedYearFilterField.setText("");
    borderPane.setRight(null);
    setBooksTable("", "", "");
    setActiveBorrowings();
  }

  @FXML
  private void handleSettingsButtonClick(MouseEvent event) {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem updateInfoItem = new MenuItem("Update Personal Info");
    updateInfoItem.setOnAction(e -> loadUserInfoForm());
    MenuItem logoutItem = new MenuItem("Logout");
    logoutItem.setOnAction(e -> LogoutUtil.logout(settingsButton.getScene().getWindow(), services));
    contextMenu.getItems().addAll(updateInfoItem, logoutItem);
    contextMenu.show(settingsButton, event.getScreenX(), event.getScreenY());
  }

  @FXML
  private void handleSearchButtonClick() {
    String titlePart = titleFilterField.getText();
    String authorPart = authorFilterField.getText();
    String publishedYear = publishedYearFilterField.getText();
    try {
      // If published year filter is applied, check if the input is integer.
      if (!publishedYear.isEmpty()) Integer.parseInt(publishedYear);
      setBooksTable(titlePart.toLowerCase(), authorPart.toLowerCase(), publishedYear);
    } catch (NumberFormatException e) {
      FailureAlert alert = new FailureAlert("Published year field should be a number");
      alert.showAndWait();
    }
  }

  @FXML
  private void handleCloseRightPanel() {
    borderPane.setRight(null);
  }

  private void handleBorrowButtonClick(Book book) {
    try {
      services.getBorrowingService().request(currentUserId, book);
      SuccessAlert alert = new SuccessAlert("Borrowing request successfully added");
      alert.showAndWait();
      setActiveBorrowings();
      setBookDetails(book);
    } catch (InvalidBorrowingRequestException e) {
      FailureAlert alert = new FailureAlert(e.getMessage());
      alert.showAndWait();
    }
  }

  private void handleRateButtonClick(Rating rating, Book book) {
    loadRatingForm(rating, book);
    setBookRatings(book);
    setBooksTable("", "", "");
  }

  private void handleDeleteRatingClick(Rating rating, Book book) {
    services.getRatingService().delete(rating.getId());
    setBookRatings(book);
  }

  private void handleCancelBorrowingClick(String borrowingId) {
    services.getBorrowingService().delete(borrowingId);
    setActiveBorrowings();
  }

  private void setBooksTable(String titlePart, String authorPart, String publishedYear) {
    List<Book> filteredBooks =
        services.getBookService().filteredBooks(titlePart, authorPart, publishedYear);
    ObservableList<BookRow> bookTableData = FXCollections.observableArrayList();

    for (Book book : filteredBooks) {
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

    bookTableView.setItems(bookTableData);

    titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
    isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
    avgRatingScoreColumn.setCellValueFactory(
        cellData -> cellData.getValue().avgRatingScoreProperty());
    totalRatingColumn.setCellValueFactory(cellData -> cellData.getValue().totalRatingsProperty());

    bookTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    bookTableView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                // Fetch details of the selected book
                BookRow selectedBookRow = bookTableView.getSelectionModel().getSelectedItem();
                Book selectedBook = services.getBookService().findById(selectedBookRow.getId());
                setRightPanel(selectedBook);
              }
            });
  }

  private CustomGridPane createBorrowingGridPane(Borrowing borrowing) {
    CustomGridPane gridPane = new CustomGridPane(true);
    Book book = services.getBookService().findById(borrowing.getBookId());

    WrapLabel titleLabel = new WrapLabel("Title:");
    WrapLabel titleValue = new WrapLabel(book.getTitle());

    WrapLabel isbnLabel = new WrapLabel("ISBN:");
    WrapLabel isbnValue = new WrapLabel(book.getIsbn());

    WrapLabel approvedAtLabel = new WrapLabel("Approved At:");
    LocalDate approvedAt = borrowing.getApprovedAt();
    WrapLabel approvedAtValue =
        approvedAt == null ? new WrapLabel("--") : new WrapLabel(approvedAt.toString());

    WrapLabel returnBeforeLabel = new WrapLabel("Return Before:");
    LocalDate returnBefore = borrowing.getReturnBefore();
    WrapLabel returnBeforeValue =
        returnBefore == null ? new WrapLabel("--") : new WrapLabel(returnBefore.toString());

    gridPane.add(titleLabel, 0, 0);
    gridPane.add(titleValue, 1, 0);
    gridPane.add(isbnLabel, 0, 1);
    gridPane.add(isbnValue, 1, 1);
    gridPane.add(approvedAtLabel, 0, 2);
    gridPane.add(approvedAtValue, 1, 2);
    gridPane.add(returnBeforeLabel, 0, 3);
    gridPane.add(returnBeforeValue, 1, 3);

    gridPane.setAlignment(Pos.TOP_LEFT);

    if (approvedAt == null) {
      HBox buttonBox = new HBox();
      Button cancelButton = new Button("Cancel");
      cancelButton.setOnMouseClicked(e -> handleCancelBorrowingClick(borrowing.getId()));
      buttonBox.getChildren().add(cancelButton);
      gridPane.add(buttonBox, 0, 4, 2, 1);
    }

    return gridPane;
  }

  private CustomGridPane createBookDetailsGridPane(Book book) {
    Category category = services.getCategoryService().getBookCategory(book);
    int availableCopies = services.getBorrowingService().findAvailableCopies(book);
    double averageRating = services.getBookService().getAverageRating(book);
    int totalRatings = services.getBookService().getTotalRatings(book);

    CustomGridPane gridPane = new CustomGridPane(true);

    gridPane.add(new WrapLabel("ISBN:"), 0, 0);
    gridPane.add(new WrapLabel(book.getIsbn()), 1, 0);

    WrapLabel titleLabel = new WrapLabel(book.getTitle());
    gridPane.add(new WrapLabel("Title:"), 0, 1);
    gridPane.add(titleLabel, 1, 1);

    gridPane.add(new WrapLabel("Author:"), 0, 2);
    gridPane.add(new WrapLabel(book.getAuthor()), 1, 2);

    gridPane.add(new WrapLabel("Publisher:"), 0, 3);
    gridPane.add(new WrapLabel(book.getPublisher()), 1, 3);

    gridPane.add(new WrapLabel("Published Year:"), 0, 4);
    gridPane.add(new WrapLabel(book.getPublishedYear()), 1, 4);

    gridPane.add(new WrapLabel("Category:"), 0, 5);
    gridPane.add(new WrapLabel(category.getTitle()), 1, 5);

    gridPane.add(new WrapLabel("Total Copies:"), 0, 6);
    gridPane.add(new WrapLabel(Integer.toString(book.getTotalCopies())), 1, 6);

    gridPane.add(new WrapLabel("Available Copies:"), 0, 7);
    gridPane.add(new WrapLabel(Integer.toString(availableCopies)), 1, 7);

    gridPane.add(new WrapLabel("Average Rating Score:"), 0, 8);
    gridPane.add(new WrapLabel(Double.toString(averageRating)), 1, 8);

    gridPane.add(new WrapLabel("Total Ratings:"), 0, 9);
    gridPane.add(new WrapLabel(Integer.toString(totalRatings)), 1, 9);

    return gridPane;
  }

  private CustomGridPane createRatingGridPane(Rating rating) {
    CustomGridPane gridPane = new CustomGridPane(true);

    User user = services.getUserService().findById(rating.getUserId());

    WrapLabel userLabel = new WrapLabel("User:");
    WrapLabel userValue = new WrapLabel(user.getUsername());

    WrapLabel yourRateLabel = new WrapLabel("Rate:");
    WrapLabel yourRateValue = new WrapLabel(Integer.toString(rating.getScore()));

    WrapLabel ratedAtLabel = new WrapLabel("Rated At:");
    WrapLabel ratedAtValue = new WrapLabel(rating.getRatedAt().toString());

    gridPane.add(userLabel, 0, 0);
    gridPane.add(userValue, 1, 0);
    gridPane.add(yourRateLabel, 0, 1);
    gridPane.add(yourRateValue, 1, 1);
    gridPane.add(ratedAtLabel, 2, 0);
    gridPane.add(ratedAtValue, 2, 1);

    String description = rating.getDescription();
    if (!description.isEmpty()) {
      WrapLabel descriptionLabel = new WrapLabel("Comment:");
      WrapLabel descriptionValue = new WrapLabel(description);
      gridPane.add(descriptionLabel, 0, 2);
      gridPane.add(descriptionValue, 0, 3, 3, 1);
    }

    gridPane.setAlignment(Pos.TOP_LEFT);

    return gridPane;
  }

  private void setActiveBorrowings() {
    User currentUser = services.getUserService().findById(currentUserId);
    List<Borrowing> borrowings = services.getBorrowingService().findByUser(currentUser);
    leftVBox.getChildren().clear();
    for (Borrowing borrowing : borrowings) {
      CustomGridPane borrowingGridPane = createBorrowingGridPane(borrowing);
      leftVBox.getChildren().add(borrowingGridPane);
    }
  }

  private void setBookDetails(Book book) {
    innerRightVBox.getChildren().clear();
    Button borrowButton = new Button("Borrow");

    borrowButton.setOnAction(event -> handleBorrowButtonClick(book));
    HBox buttonBox = new HBox();
    Region region = new Region();
    HBox.setHgrow(region, Priority.ALWAYS);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(region, borrowButton);

    CustomGridPane gridPane = createBookDetailsGridPane(book);

    innerRightVBox.getChildren().addAll(gridPane, buttonBox);
  }

  private void setBookRatings(Book book) {
    innerRightVBox.getChildren().clear();

    ArrayList<Rating> ratings = services.getRatingService().findByBook(book);
    Rating yourRating =
        ratings.stream()
            .filter(rating -> rating.getUserId().equals(currentUserId))
            .findFirst()
            .orElse(null);

    HBox buttonBox = new HBox();
    buttonBox.setAlignment(Pos.CENTER_LEFT);

    Region region = new Region();
    HBox.setHgrow(region, Priority.ALWAYS);
    buttonBox.getChildren().add(region);

    if (yourRating != null) {
      ratings.sort(Comparator.comparingInt(rating -> rating == yourRating ? 0 : 1));
      Button deleteBtn = new Button("Delete");
      buttonBox.getChildren().add(deleteBtn);
      deleteBtn.setOnAction(e -> handleDeleteRatingClick(yourRating, book));
    }

    Button rateBtn = new Button("Rate");
    buttonBox.getChildren().add(rateBtn);
    rateBtn.setOnAction(e -> handleRateButtonClick(yourRating, book));

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    VBox.setVgrow(scrollPane, Priority.ALWAYS);
    scrollPane.setStyle("-fx-border-width: 0; -fx-background-color: white;");
    scrollPane.setPadding(new Insets(0, 5, 0, 0));

    VBox scrollContent = new VBox(10);
    VBox.setVgrow(scrollContent, Priority.ALWAYS);
    scrollContent.setStyle("-fx-background-color: white;");

    for (Rating r : ratings) {
      CustomGridPane gridPane = createRatingGridPane(r);
      scrollContent.getChildren().add(gridPane);
    }
    scrollPane.setContent(scrollContent);
    innerRightVBox.getChildren().add(buttonBox);
    if (!scrollContent.getChildren().isEmpty()) {
      innerRightVBox.getChildren().add(scrollContent);
    }
  }

  private void setRightPanel(Book book) {
    borderPane.setRight(rightVBox);
    ToggleGroup toggleGroup = new ToggleGroup();
    detailsToggleBtn.setToggleGroup(toggleGroup);
    ratingsToggleBtn.setToggleGroup(toggleGroup);

    toggleGroup
        .selectedToggleProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue == detailsToggleBtn) {
                setBookDetails(book);
              } else if (newValue == ratingsToggleBtn) {
                setBookRatings(book);
              }
            });
    // Default tab
    detailsToggleBtn.setSelected(true);
    setBookDetails(book);
  }

  private void loadUserInfoForm() {
    User currentUser = services.getUserService().findById(currentUserId);
    try {
      FXMLLoader loader =
          new FXMLLoader(
              UserController.class.getResource(
                  "/ntua/multimedia/libraryproject/user/user-form-view.fxml"));
      loader.setControllerFactory(
          controllerType -> new UserFormController(services.getUserService(), currentUser));
      Parent root = loader.load();
      PopupStageUtil.showPopupStage(
          root, settingsButton.getScene().getWindow(), 420, 440, "Update Personal Info");
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong loading personal info form");
      alert.showAndWait();
    }
  }

  private void loadRatingForm(Rating rating, Book book) {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              UserController.class.getResource(
                  "/ntua/multimedia/libraryproject/user/rating-form-view.fxml"));
      loader.setControllerFactory(
          controllerType ->
              new RatingFormController(services.getRatingService(), rating, currentUserId, book));
      Parent root = loader.load();
      PopupStageUtil.showPopupStage(
          root, settingsButton.getScene().getWindow(), 340, 340, "Add Rating");
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong loading rating form.");
      alert.showAndWait();
    }
  }
}
