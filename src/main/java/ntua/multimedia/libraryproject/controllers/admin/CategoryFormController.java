package ntua.multimedia.libraryproject.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ntua.multimedia.libraryproject.exceptions.DuplicateFieldException;
import ntua.multimedia.libraryproject.models.entities.Category;
import ntua.multimedia.libraryproject.services.CategoryService;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.SuccessAlert;

public class CategoryFormController {
  private final CategoryService categoryService;
  private final Category categoryToUpdate;
  @FXML private TextField titleField;
  @FXML private Button saveButton;

  public CategoryFormController(CategoryService categoryService, Category categoryToUpdate) {
    this.categoryToUpdate = categoryToUpdate;
    this.categoryService = categoryService;
  }

  @FXML
  public void initialize() {
    saveButton.setDisable(true);
    saveButton.disableProperty().bind(titleField.textProperty().isEmpty());
    if (categoryToUpdate != null) {
      titleField.setText(categoryToUpdate.getTitle());
    }
  }

  @FXML
  private void handleSave() {
    try {
      categoryService.createOrUpdate(categoryToUpdate, titleField.getText());
      SuccessAlert alert = new SuccessAlert("Category was saved successfully!");
      alert.show();
      titleField.clear();
    } catch (DuplicateFieldException e) {
      FailureAlert alert = new FailureAlert(e.getMessage());
      alert.show();
    }
  }

  @FXML
  private void handleCancel() {
    titleField.getScene().getWindow().hide();
  }
}
