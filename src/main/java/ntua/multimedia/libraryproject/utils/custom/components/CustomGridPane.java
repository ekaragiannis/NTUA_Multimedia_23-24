package ntua.multimedia.libraryproject.utils.custom.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class CustomGridPane extends GridPane {

  public CustomGridPane(boolean hasBorder) {
    setPadding(new Insets(10));
    setAlignment(Pos.CENTER);
    setVgap(20);
    setHgap(10);
    if (hasBorder) {
      setStyle("-fx-border-color: lightgrey; -fx-border-width: 1;");
    }
  }
}
