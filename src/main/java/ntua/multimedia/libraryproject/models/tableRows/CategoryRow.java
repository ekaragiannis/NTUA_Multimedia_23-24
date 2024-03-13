package ntua.multimedia.libraryproject.models.tableRows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ntua.multimedia.libraryproject.models.BaseModel;

public class CategoryRow extends BaseModel {
  private final StringProperty title;

  public CategoryRow(String id, String title) {
    super(id);
    this.title = new SimpleStringProperty(title);
  }

  public StringProperty titleProperty() {
    return title;
  }
}
