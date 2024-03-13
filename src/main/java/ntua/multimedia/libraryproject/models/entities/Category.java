package ntua.multimedia.libraryproject.models.entities;

import ntua.multimedia.libraryproject.models.BaseModel;

public class Category extends BaseModel {
  private String title;

  public Category(String id, String title) {
    super(id);
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "Category{" + "id='" + id + '\'' + ", title='" + title + '\'' + '}';
  }
}
