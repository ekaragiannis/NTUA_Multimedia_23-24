package ntua.multimedia.libraryproject.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ntua.multimedia.libraryproject.exceptions.PermissionDeniedException;
import ntua.multimedia.libraryproject.models.entities.Book;
import ntua.multimedia.libraryproject.models.entities.Rating;
import ntua.multimedia.libraryproject.services.RatingService;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.SuccessAlert;

public class RatingFormController {

  private final RatingService ratingService;
  private final Rating existingRating;
  private final Book book;
  private final String userId;
  @FXML private Slider ratingSlider;
  @FXML private TextArea descriptionArea;

  public RatingFormController(
      RatingService ratingService, Rating existingRating, String userId, Book book) {
    this.ratingService = ratingService;
    this.existingRating = existingRating;
    this.book = book;
    this.userId = userId;
  }

  @FXML
  private void initialize() {
    if (existingRating != null) {
      ratingSlider.setValue(existingRating.getScore());
      descriptionArea.setText(existingRating.getDescription());
    }
  }

  @FXML
  private void handleClose() {
    descriptionArea.getScene().getWindow().hide();
  }

  @FXML
  private void handleSave() {
    try {
      ratingService.createOrUpdate(
          existingRating,
          userId,
          book.getId(),
          (int) ratingSlider.getValue(),
          descriptionArea.getText());
      SuccessAlert alert = new SuccessAlert("Rating added successfully!");
      alert.showAndWait();
    } catch (PermissionDeniedException e) {
      FailureAlert alert = new FailureAlert(e.getMessage());
      alert.showAndWait();
    }
    descriptionArea.getScene().getWindow().hide();
  }
}
