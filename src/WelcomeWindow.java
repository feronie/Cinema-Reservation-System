import javafx.scene.Scene;
import java.io.IOException;
import java.net.MalformedURLException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeWindow extends Stage {

    Button addFilm = new Button("Add Film");
    Button removeFilm = new Button("Remove Film");
    Button editUsers = new Button("Edit Users");
    Button ok = new Button("OK");
    Button logOut = new Button("LOG OUT");
    GridPane root = new GridPane();
    static ComboBox<String> comboBox;
    static Database database = new Database();

    static Scene scene;
    Text message = new Text();

    public WelcomeWindow(User user) throws IOException {

        Database.checkData();
        String sentence = "          Welcome " + user.username;
        String secondSentence;
        Text text = new Text();
        root.add(message, 0, 3);
        comboBox = new ComboBox<>();
        comboBox.getItems().addAll(Database.films.keySet());
        comboBox.getSelectionModel().selectLast();
        root.add(text, 0, 0);
        root.add(comboBox, 0, 1);
        root.add(ok, 1, 1);
        root.add(logOut, 1, 3);

        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);

        if (user.isAdmin) {
            secondSentence = "\n      You can either select film below or do edits.";
            if (user.isClubMember) {
                text.setText(sentence + "(Admin - Club Member)!" + secondSentence);
            } else {
                text.setText(sentence + "(Admin)!" + secondSentence);
            }

            HBox hBox = new HBox(2, addFilm, removeFilm, editUsers);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            root.add(hBox, 0, 2);
            addFilm.setOnAction(e -> {

            });
        } else {

            secondSentence = " \n      Select a film and then click OK to continue ";
            if (user.isClubMember) {
                text.setText(sentence + "(Club Member)!");
            } else {
                text.setText(sentence + "!");
            }

        }
        scene = new Scene(root, 450, 200);
        this.setScene(scene);

        logOut.setOnAction(event -> {
            message.setText("");
            Main.getPrimaryStage().setScene(LogInWindow.loginScene);
        });
        removeFilm.setOnAction(event -> {
            try {
                Main.getPrimaryStage().setScene(new RemoveFilm().scene);
                message.setText("");
            } catch (IOException e) {
            }
        });
        addFilm.setOnAction(event -> {
            Main.getPrimaryStage().setScene(new AddFilmWindow().scene);
            message.setText("");
        });
        editUsers.setOnAction(event -> {
            try {
                Main.getPrimaryStage().setScene(new EditUsersWindow(user).getScene());
                message.setText("");
            } catch (IOException e) {
            }
        });
        ok.setOnAction(event -> {
            String filmName = comboBox.getSelectionModel().getSelectedItem();
            try {
                Main.getPrimaryStage().setScene(new FilmWindow(filmName, user).getScene());
                ;
            } catch (NullPointerException e) {
                new ErrorException(this, "ERROR :There is no film to show!");

            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
        });

    }

}