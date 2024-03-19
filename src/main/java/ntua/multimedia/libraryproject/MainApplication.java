package ntua.multimedia.libraryproject;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ntua.multimedia.libraryproject.controllers.guest.GuestController;
import ntua.multimedia.libraryproject.repositories.Repositories;
import ntua.multimedia.libraryproject.services.Services;
import ntua.multimedia.libraryproject.storage.Storage;

public class MainApplication extends Application {
  final Services services = new Services(new Repositories());
  private final Storage storage = Storage.getInstance();

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    storage.deserializeAll();
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
}
