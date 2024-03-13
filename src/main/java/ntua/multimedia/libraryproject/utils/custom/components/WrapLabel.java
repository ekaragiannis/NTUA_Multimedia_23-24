package ntua.multimedia.libraryproject.utils.custom.components;

import javafx.scene.control.Label;

public class WrapLabel extends Label {
  public WrapLabel(String text) {
    super(text);
    setWrapText(true);
  }
}
