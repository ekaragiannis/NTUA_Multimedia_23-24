package ntua.multimedia.libraryproject.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PopupStageUtil {

  public static void showPopupStage(
      Parent root, Window owner, double width, double height, String title) {
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(owner);
    stage.setScene(new Scene(root));
    stage.setMinWidth(width);
    stage.setMaxWidth(width);
    stage.setMinHeight(height);
    stage.setMaxHeight(height);
    stage.setTitle(title);
    stage.showAndWait();
  }
}
