package ntua.multimedia.libraryproject.controllers.admin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import ntua.multimedia.libraryproject.models.entities.*;
import ntua.multimedia.libraryproject.models.tableRows.BookRow;
import ntua.multimedia.libraryproject.models.tableRows.BorrowingRow;
import ntua.multimedia.libraryproject.models.tableRows.CategoryRow;
import ntua.multimedia.libraryproject.models.tableRows.UserRow;
import ntua.multimedia.libraryproject.services.Services;
import ntua.multimedia.libraryproject.utils.CommonFunctionalityUtil;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;

public class AdminController {
  private final Services services;
  @FXML private Button settingsButton;
  @FXML private VBox contentVBox;
  @FXML private ToggleButton booksToggleBtn;
  @FXML private ToggleButton categoriesToggleBtn;
  @FXML private ToggleButton borrowingsToggleBtn;
  @FXML private ToggleButton usersToggleBtn;

  public AdminController(Services services) {
    this.services = services;
  }

  public void initialize() {
    ToggleGroup toggleGroup = new ToggleGroup();
    usersToggleBtn.setToggleGroup(toggleGroup);
    usersToggleBtn.setSelected(true);
    borrowingsToggleBtn.setToggleGroup(toggleGroup);
    categoriesToggleBtn.setToggleGroup(toggleGroup);
    booksToggleBtn.setToggleGroup(toggleGroup);

    toggleGroup
        .selectedToggleProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue == usersToggleBtn) {
                setUsersTable();
              } else if (newValue == categoriesToggleBtn) {
                setCategoriesTable();
              } else if (newValue == booksToggleBtn) {
                setBookTable();
              } else if (newValue == borrowingsToggleBtn) {
                setBorrowingsTable();
              }
            });

    setUsersTable();
  }

  @FXML
  private void handleSettingsBtnClick(MouseEvent event) {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem addCategoryItem = new MenuItem("Add category");
    MenuItem addBookItem = new MenuItem("Add book");
    MenuItem logoutItem = new MenuItem("Logout");
    contextMenu.getItems().addAll(addCategoryItem, addBookItem, logoutItem);
    addCategoryItem.setOnAction(e -> handleCreateCategory());
    addBookItem.setOnAction(e -> handleCreateBook());
    logoutItem.setOnAction(
        e -> CommonFunctionalityUtil.logout(settingsButton.getScene().getWindow(), services));
    contextMenu.show(settingsButton, event.getScreenX(), event.getScreenY());
  }

  // Utils
  private void updateContentVBox(Node node) {
    contentVBox.getChildren().clear();
    VBox.setVgrow(node, Priority.ALWAYS);
    contentVBox.getChildren().add(node);
  }

  // Handlers for Create/Update/Delete Category and Book
  private void handleDeleteBook(String bookId) {
    services.getBookService().delete(bookId);
    setBookTable();
  }

  private void handleUpdateBook(Book book) {
    createBookForm(book);
    setBookTable();
  }

  private void handleCreateBook() {
    createBookForm(null);
    setBookTable();
  }

  private void handleCreateCategory() {
    createCategoryForm(null);
    setCategoriesTable();
  }

  private void handleUpdateCategory(Category category) {
    createCategoryForm(category);
    setCategoriesTable();
  }

  private void handleDeleteCategory(String categoryId) {
    services.getCategoryService().delete(categoryId);
    setCategoriesTable();
  }

  // Functions that create category and books forms for Create/Update
  private void createBookForm(Book book) {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              AdminController.class.getResource(
                  "/ntua/multimedia/libraryproject/admin/book-form-view.fxml"));
      loader.setControllerFactory(
          controllerType ->
              new BookFormController(
                  services.getBookService(), services.getCategoryService(), book));
      Parent root = loader.load();
      CommonFunctionalityUtil.showPopupStage(
          root, settingsButton.getScene().getWindow(), 360, 400, "Add Book");
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong loading the book form.");
      alert.showAndWait();
    }
  }

  void createCategoryForm(Category category) {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              AdminController.class.getResource(
                  "/ntua/multimedia/libraryproject/admin/category-form-view.fxml"));
      loader.setControllerFactory(
          controllerType -> new CategoryFormController(services.getCategoryService(), category));
      Parent root = loader.load();
      CommonFunctionalityUtil.showPopupStage(
          root, settingsButton.getScene().getWindow(), 280, 140, "Add Category");
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong loading the category form.");
      alert.showAndWait();
    }
  }

  // Table loaders
  private void setBookTable() {
    ArrayList<Book> books = services.getBookService().findAll();
    ObservableList<BookRow> bookTableRows = FXCollections.observableArrayList();
    for (Book book : books) {
      double averageRating = services.getBookService().getAverageRating(book);
      int totalRatings = services.getBookService().getTotalRatings(book);
      bookTableRows.add(
          new BookRow(
              book.getId(),
              book.getTitle(),
              book.getAuthor(),
              book.getIsbn(),
              Double.toString(averageRating),
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

    bookTableView.setItems(bookTableRows);
    bookTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

    for (TableColumn<BookRow, ?> column : bookTableView.getColumns()) {
      column.setStyle("-fx-alignment: CENTER; -fx-font-size: 11pt");
    }

    ContextMenu contextMenu = new ContextMenu();
    MenuItem updateItem = new MenuItem("Update");
    MenuItem deleteItem = new MenuItem("Delete");
    MenuItem cancelItem = new MenuItem("Cancel");
    contextMenu.getItems().addAll(updateItem, deleteItem);

    updateItem.setOnAction(
        event -> {
          BookRow selectedItem = bookTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            handleUpdateBook(services.getBookService().findById(selectedItem.getId()));
          }
        });

    deleteItem.setOnAction(
        event -> {
          BookRow selectedItem = bookTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            handleDeleteBook(selectedItem.getId());
          }
        });

    cancelItem.setOnAction(
        event -> {
          BookRow selectedItem = bookTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            contextMenu.hide();
          }
        });

    bookTableView.setRowFactory(
        tv -> {
          TableRow<BookRow> row = new TableRow<>();
          row.setOnMouseClicked(
              event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                  contextMenu.show(bookTableView, event.getScreenX(), event.getScreenY());
                }
              });
          return row;
        });

    updateContentVBox(bookTableView);
  }

  private void setCategoriesTable() {
    ArrayList<Category> categories = services.getCategoryService().findAll();
    ObservableList<CategoryRow> categoryTableData = FXCollections.observableArrayList();

    categories.forEach(
        category -> categoryTableData.add(new CategoryRow(category.getId(), category.getTitle())));

    TableView<CategoryRow> categoryTableView = new TableView<>();

    TableColumn<CategoryRow, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    titleColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 11pt");
    categoryTableView.getColumns().add(titleColumn);

    categoryTableView.setItems(categoryTableData);
    categoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

    ContextMenu contextMenu = new ContextMenu();
    MenuItem updateItem = new MenuItem("Update");
    MenuItem deleteItem = new MenuItem("Delete");
    MenuItem cancelItem = new MenuItem("Cancel");
    contextMenu.getItems().addAll(updateItem, deleteItem);

    updateItem.setOnAction(
        event -> {
          CategoryRow selectedItem = categoryTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            handleUpdateCategory(services.getCategoryService().findById(selectedItem.getId()));
          }
        });

    deleteItem.setOnAction(
        event -> {
          CategoryRow selectedItem = categoryTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            handleDeleteCategory(selectedItem.getId());
          }
        });

    cancelItem.setOnAction(
        event -> {
          CategoryRow selectedItem = categoryTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            contextMenu.hide();
          }
        });

    categoryTableView.setRowFactory(
        tv -> {
          TableRow<CategoryRow> row = new TableRow<>();
          row.setOnMouseClicked(
              event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                  contextMenu.show(categoryTableView, event.getScreenX(), event.getScreenY());
                }
              });
          return row;
        });

    updateContentVBox(categoryTableView);
  }

  private void setUsersTable() {
    ArrayList<User> users = services.getUserService().findAll();

    ObservableList<UserRow> userTableData = FXCollections.observableArrayList();
    users.forEach(
        user ->
            userTableData.add(
                new UserRow(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getIdCardNumber(),
                    user.getEmail())));

    TableView<UserRow> userTableView = new TableView<>();

    TableColumn<UserRow, String> usernameColumn = new TableColumn<>("Username");
    usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
    userTableView.getColumns().add(usernameColumn);

    TableColumn<UserRow, String> firstNameColumn = new TableColumn<>("First Name");
    firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
    userTableView.getColumns().add(firstNameColumn);

    TableColumn<UserRow, String> lastNameColumn = new TableColumn<>("Last Name");
    lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
    userTableView.getColumns().add(lastNameColumn);

    TableColumn<UserRow, String> idCardNumberColumn = new TableColumn<>("ID Card Number");
    idCardNumberColumn.setCellValueFactory(cellData -> cellData.getValue().idCardNumberProperty());
    userTableView.getColumns().add(idCardNumberColumn);

    TableColumn<UserRow, String> emailColumn = new TableColumn<>("Email");
    emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
    userTableView.getColumns().add(emailColumn);

    userTableView.setItems(userTableData);
    userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

    ContextMenu contextMenu = new ContextMenu();
    MenuItem deleteItem = new MenuItem("Delete");
    contextMenu.getItems().add(deleteItem);

    deleteItem.setOnAction(
        event -> {
          UserRow selectedItem = userTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            services.getUserService().delete(selectedItem.getId());
            setUsersTable();
          }
        });

    userTableView.setRowFactory(
        tv -> {
          TableRow<UserRow> row = new TableRow<>();
          row.setOnMouseClicked(
              event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                  contextMenu.show(userTableView, event.getScreenX(), event.getScreenY());
                }
              });
          return row;
        });

    updateContentVBox(userTableView);
  }

  private void setBorrowingsTable() {
    List<Borrowing> borrowings = services.getBorrowingService().findAll();
    ObservableList<BorrowingRow> borrowingTableData = FXCollections.observableArrayList();

    for (Borrowing borrowing : borrowings) {
      Book book = services.getBookService().findById(borrowing.getBookId());
      User user = services.getUserService().findById(borrowing.getUserId());
      LocalDate approvedAt = borrowing.getApprovedAt();
      LocalDate returnBefore = borrowing.getReturnBefore();

      borrowingTableData.add(
          new BorrowingRow(
              borrowing.getId(),
              book.getIsbn(),
              book.getTitle(),
              user.getUsername(),
              user.getEmail(),
              approvedAt != null ? approvedAt.toString() : "--",
              returnBefore != null ? returnBefore.toString() : "--"));
    }

    TableView<BorrowingRow> borrowingTableView = new TableView<>();

    TableColumn<BorrowingRow, String> isbnColumn = new TableColumn<>("ISBN");
    isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
    borrowingTableView.getColumns().add(isbnColumn);

    TableColumn<BorrowingRow, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    borrowingTableView.getColumns().add(titleColumn);

    TableColumn<BorrowingRow, String> usernameColumn = new TableColumn<>("Username");
    usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
    borrowingTableView.getColumns().add(usernameColumn);

    TableColumn<BorrowingRow, String> emailColumn = new TableColumn<>("Email");
    emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
    borrowingTableView.getColumns().add(emailColumn);

    TableColumn<BorrowingRow, String> statusColumn = new TableColumn<>("Approved At");
    statusColumn.setCellValueFactory(cellData -> cellData.getValue().approvedAtProperty());
    borrowingTableView.getColumns().add(statusColumn);

    TableColumn<BorrowingRow, String> returnBefore = new TableColumn<>("Return Before");
    returnBefore.setCellValueFactory(cellData -> cellData.getValue().returnBeforeProperty());
    borrowingTableView.getColumns().add(returnBefore);

    borrowingTableView.setItems(borrowingTableData);
    borrowingTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

    ContextMenu contextMenu = new ContextMenu();
    MenuItem approveItem = new MenuItem("Approve");
    MenuItem deleteItem = new MenuItem("Delete");
    contextMenu.getItems().addAll(approveItem, deleteItem);

    approveItem.setOnAction(
        event -> {
          BorrowingRow selectedItem = borrowingTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            services.getBorrowingService().approve(selectedItem.getId());
            setBorrowingsTable();
          }
        });

    deleteItem.setOnAction(
        event -> {
          BorrowingRow selectedItem = borrowingTableView.getSelectionModel().getSelectedItem();
          if (selectedItem != null) {
            services.getBorrowingService().delete(selectedItem.getId());
            setBorrowingsTable();
          }
        });

    borrowingTableView.setRowFactory(
        tv -> {
          TableRow<BorrowingRow> row = new TableRow<>();
          row.setOnMouseClicked(
              event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                  contextMenu.show(borrowingTableView, event.getScreenX(), event.getScreenY());
                }
              });
          return row;
        });

    updateContentVBox(borrowingTableView);
  }
}
