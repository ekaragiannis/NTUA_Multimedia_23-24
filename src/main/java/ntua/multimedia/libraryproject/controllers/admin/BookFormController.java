package ntua.multimedia.libraryproject.controllers.admin;

import java.time.Year;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.models.entities.Category;
import ntua.multimedia.libraryproject.services.BookService;
import ntua.multimedia.libraryproject.services.CategoryService;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.SuccessAlert;

public class BookFormController {
  private final BookService bookService;
  private final CategoryService categoryService;
  private final Book bookToUpdate;
  @FXML private TextField isbnField;
  @FXML private TextField titleField;
  @FXML private TextField authorField;
  @FXML private TextField publisherField;
  @FXML private Spinner<Integer> publishedYearSpinner;
  @FXML private ComboBox<String> categoryComboBox;
  @FXML private Spinner<Integer> totalCopiesSpinner;
  @FXML private Button saveButton;

  public BookFormController(
      BookService bookService, CategoryService categoryService, Book bookToUpdate) {
    this.categoryService = categoryService;
    this.bookService = bookService;
    this.bookToUpdate = bookToUpdate;
  }

  public void initialize() {
    List<String> categories = categoryService.findAll().stream().map(Category::getTitle).toList();
    categoryComboBox.getItems().addAll(categories);

    SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
    totalCopiesSpinner.setValueFactory(valueFactory);
    setSpinnerEditable(totalCopiesSpinner);

    valueFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(
            0, Year.now().getValue(), Year.now().getValue());
    publishedYearSpinner.setValueFactory(valueFactory);
    setSpinnerEditable(publishedYearSpinner);

    saveButton.setDisable(true);

    // Disable save button if an empty field exists
    BooleanBinding anyFieldEmpty =
        Bindings.createBooleanBinding(
            () ->
                isbnField.getText().isEmpty()
                    || titleField.getText().isEmpty()
                    || authorField.getText().isEmpty()
                    || publisherField.getText().isEmpty()
                    || categoryComboBox.getValue() == null,
            isbnField.textProperty(),
            titleField.textProperty(),
            authorField.textProperty(),
            publisherField.textProperty(),
            publishedYearSpinner.valueProperty(),
            categoryComboBox.valueProperty());
    saveButton.disableProperty().bind(anyFieldEmpty);

    // if we are updating a book, fill the form with the book's current data
    if (bookToUpdate != null) {
      isbnField.setText(bookToUpdate.getIsbn());
      titleField.setText(bookToUpdate.getTitle());
      authorField.setText(bookToUpdate.getAuthor());
      publisherField.setText(bookToUpdate.getPublisher());
      publishedYearSpinner
          .getValueFactory()
          .setValue(Integer.parseInt(bookToUpdate.getPublishedYear()));
      categoryComboBox.setValue(categoryService.getBookCategory(bookToUpdate).getTitle());
      totalCopiesSpinner.getValueFactory().setValue(bookToUpdate.getTotalCopies());
    }
  }

  private void setSpinnerEditable(Spinner<Integer> spinner) {
    spinner.setEditable(true);
    spinner
        .getEditor()
        .setTextFormatter(
            new TextFormatter<>(
                change -> {
                  // Only allow digits (0-9)
                  String newText = change.getControlNewText();
                  if (newText.matches("\\d*")) {
                    return change;
                  } else {
                    return null;
                  }
                }));
  }

  @FXML
  private void handleSave() {
    try {
      bookService.createOrUpdate(
          bookToUpdate,
          isbnField.getText().trim(),
          titleField.getText().trim(),
          authorField.getText().trim(),
          publisherField.getText().trim(),
          publishedYearSpinner.getValue().toString(),
          categoryComboBox.getValue(),
          totalCopiesSpinner.getValue());
      SuccessAlert alert = new SuccessAlert("Book was saved successfully!");
      alert.showAndWait();
      isbnField.clear();
      titleField.clear();
      authorField.clear();
      publisherField.clear();
      publishedYearSpinner.getValueFactory().setValue(Year.now().getValue());
      categoryComboBox.getSelectionModel().clearSelection();
      totalCopiesSpinner.getValueFactory().setValue(1);
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert(e.getMessage());
      alert.showAndWait();
    }
  }

  @FXML
  private void handleCancel() {
    isbnField.getScene().getWindow().hide();
  }
}
