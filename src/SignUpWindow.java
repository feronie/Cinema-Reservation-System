import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class SignUpWindow extends Stage {

    static GridPane signUpWindow = new GridPane();
    static TextField username = new TextField();
    static PasswordField password = new PasswordField();
    static PasswordField password2 = new PasswordField();
    static Text message = new Text();

    static Button signUp = new Button("SIGN UP");
    static Button login = new Button("LOG IN");

    static Scene signUpScene;
    static Database database = new Database();

    public void cleanFields() {

        username.clear();
        password.clear();
        password2.clear();

    }

    public SignUpWindow() throws IOException {
        Database.checkData();

        signUpWindow.add(new Text(
                "   Welcome to the HUCS Cinema Reservation System!\n      Fill the form below to create a new account.\n You can go to Log In page by clicking LOG IN Button. "),
                0, 0, 2, 2);
        signUpWindow.add(new Text("Username:"), 0, 2);
        signUpWindow.add(username, 1, 2);
        signUpWindow.add(new Text("Password:"), 0, 3);
        signUpWindow.add(password, 1, 3);
        signUpWindow.add(new Text("Password:"), 0, 4);
        signUpWindow.add(password2, 1, 4);
        signUpWindow.setHgap(10);
        signUpWindow.setVgap(10);
        signUpWindow.setAlignment(Pos.CENTER);
        signUpWindow.setPadding(new Insets(10, 10, 5, 10));
        HBox hBox = new HBox(75, login, signUp);
        signUpWindow.add(hBox, 1, 6);
        signUpWindow.add(message, 0, 8, 2, 1);
        Scene scene = new Scene(signUpWindow, 350, 250);
        signUpScene = scene;
        

        login.setOnAction((event) -> {
            message.setText("");
            cleanFields();
            Main.getPrimaryStage().setScene(LogInWindow.loginScene);

        });
        signUp.setOnAction((event) -> {
            try {
                if (usernameExists()) {
                    throw new ErrorException(this, "ERROR: This username already exists!");

                } else if (usernameIsEmpty()) {
                    throw new ErrorException(this, "ERROR: Username can not be empty!");

                } else if (!passwordsMatch()) {

                    throw new ErrorException(this, "ERROR: Passwords do not match!");

                } else if (passwordIsEmpty()) {
                    throw new ErrorException(this, "ERROR: Password can not be empty!");
                } else {
                    Database.users.put(getUsername().getText(), new User(getUsername().getText(),
                            Database.hashPassword(getPassword().getText()), false, false));
                    Database.updateData();
                    message.setText("SUCCESS: You have successfully registered with your new credentials!");
                }
                cleanFields();
            } catch (Exception e1) {
            }

        });
    }

    public static boolean usernameIsEmpty() {
        return username.getText().equals("");
    }

    public static boolean passwordsMatch() {
        return password.getText().equals(password2.getText());
    }

    public static boolean usernameExists() {
        return Database.users.containsKey(username.getText());
    }

    public static boolean passwordIsEmpty() {
        return password.getText().equals("") && password2.getText().equals("");
    }

    public TextField getUsername() {
        return username;
    }

    public PasswordField getPassword() {
        return password;
    }

    public PasswordField getPassword2() {
        return password2;
    }

    public Text getWarningMessage() {
        return message;
    }

}
