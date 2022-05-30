import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class LogInWindow extends Stage {

  static Button signUp = new Button("SIGN UP");
  static Button login = new Button("LOG IN");
  TextField username = new TextField();
  static Text warningMessage = new Text();
  PasswordField password = new PasswordField();
  Stage nextStage;
  static Scene loginScene;
  static Database database = new Database();

  public String getUsername() {
    return this.username.getText();
  }

  public String getPassword() {
    return this.password.getText();
  }

  public boolean usernameExists() {
    return Database.users.containsKey(this.username.getText());
  }

  public boolean checkPassword(String username, String password) {
    return Database.users.get(username).password.equals(Database.hashPassword(password));
  }

  public Text getWarningMessage() { 
    return warningMessage;
  }

  public void cleanFields() {
    username.clear();
    password.clear();
    warningMessage.setText("");
  }

  public LogInWindow(Stage stage) throws IOException {

    Database.checkData();
    GridPane gPane = new GridPane();
    Text text = new Text("    Welcome to the HUCS Cinema Reservation System!\n" +
        "    Please enter your credentials below and click LOGIN.\n You can create a new account by clicking SIGN UP button.");

    gPane.add(text, 0, 0, 2, 1);
    gPane.add(new Text("Username:"), 0, 1);
    gPane.add(username, 1, 1);
    gPane.add(new Text("Password:"), 0, 2);
    gPane.add(password, 1, 2);
    gPane.add(warningMessage, 0, 6, 2, 1);
    gPane.setHgap(10);
    gPane.setVgap(10);
    gPane.setPadding(new Insets(10, 10, 20, 20));
    gPane.setAlignment(Pos.CENTER);

    //gPane.setPadding(new Insets(15, 15, 15, 15));
    HBox hbox = new HBox(100, signUp, login);

    hbox.setPadding(new Insets(5, 15, 15, 15));
    gPane.add(hbox, 1, 4);
    Scene logInScene = new Scene(gPane, 375, 250);
    loginScene = logInScene;

    Main.getPrimaryStage().setScene(logInScene);

    signUp.setOnAction((event) -> {
      try {
        cleanFields();
        Main.getPrimaryStage().setScene(SignUpWindow.signUpScene);
      } catch (Exception e) {
      }
    });
    login.setOnAction((event) -> {

      if (!usernameExists()) {
        new ErrorException(this, "ERROR: There is no such a credential");
      } else {
        if (!checkPassword(getUsername(), getPassword())) {
          new ErrorException(this, "ERROR: There is no such a credential");
        } else {
          try {
            Main.getPrimaryStage().setScene(new WelcomeWindow(Database.users.get(getUsername())).getScene());
            cleanFields();
          } catch (IOException e) {
          }
        }
      }

    });

  }

}
