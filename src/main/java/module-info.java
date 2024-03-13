module ntua.multimedia.libraryproject {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafaker;

  opens ntua.multimedia.libraryproject to
      javafx.fxml;

  exports ntua.multimedia.libraryproject;
  exports ntua.multimedia.libraryproject.controllers.guest;

  opens ntua.multimedia.libraryproject.controllers.guest to
      javafx.fxml;

  exports ntua.multimedia.libraryproject.controllers.user;

  opens ntua.multimedia.libraryproject.controllers.user to
      javafx.fxml;

  exports ntua.multimedia.libraryproject.controllers.admin;

  opens ntua.multimedia.libraryproject.controllers.admin to
      javafx.fxml;

  exports ntua.multimedia.libraryproject.repositories;

  opens ntua.multimedia.libraryproject.repositories to
      javafx.fxml;

  exports ntua.multimedia.libraryproject.models;

  opens ntua.multimedia.libraryproject.models to
      javafx.fxml;
  opens ntua.multimedia.libraryproject.services to
      javafx.fxml;

  exports ntua.multimedia.libraryproject.services;

  opens ntua.multimedia.libraryproject.models.entities to
      javafx.fxml;

  exports ntua.multimedia.libraryproject.models.entities;
}
