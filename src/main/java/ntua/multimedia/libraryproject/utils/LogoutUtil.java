package ntua.multimedia.libraryproject.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import ntua.multimedia.libraryproject.controllers.guest.GuestController;
import ntua.multimedia.libraryproject.services.Services;
import ntua.multimedia.libraryproject.utils.custom.components.alerts.FailureAlert;

public class LogoutUtil {
  public static void logout(Window currentWindow, Services services) {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              GuestController.class.getResource(
                  "/ntua/multimedia/libraryproject/guest/main-view.fxml"));
      loader.setControllerFactory(controllerType -> new GuestController(services));
      Stage stage = (Stage) currentWindow.getScene().getWindow();
      Scene guestScene = new Scene(loader.load());
      stage.setScene(guestScene);
      stage.setTitle("Welcome!");
    } catch (Exception e) {
      FailureAlert alert = new FailureAlert("Something went wrong. Please try again");
      alert.showAndWait();
    }
  }
}
