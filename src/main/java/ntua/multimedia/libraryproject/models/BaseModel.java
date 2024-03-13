package ntua.multimedia.libraryproject.models;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseModel implements Serializable {
  @Serial private static final long serialVersionUID = 1L;
  protected final String id;

  public BaseModel(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "BaseModel{" + "id='" + id + '\'' + '}';
  }
}
