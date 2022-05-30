import java.io.File;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class AddFilmWindow {

    GridPane root = new GridPane();
    Text text = new Text("Please give name, relative path of the trailer and duration of the film.");
    TextField name = new TextField();
    TextField trailerPath = new TextField();
    TextField duration = new TextField();
    Text message = new Text();
    Button back = new Button(Character.toString((char)0x25C0)+" BACK");
    Button ok = new Button("OK");
    HBox hbox = new HBox(back, ok);
    Scene scene;
    static Database database = new Database();

    public void cleanFields() {
        name.clear();
        trailerPath.clear();
        duration.clear();
    }

    AddFilmWindow() {
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        root.add(text, 0, 0,2,1);
        root.add(new Text("Name:"), 0, 2);
        root.add(name, 1, 2);
        root.add(new Text("Trailer (Path):"), 0, 3);
        root.add(trailerPath, 1, 3);
        root.add(new Text("Duration (m):"), 0, 4);
        root.add(duration, 1, 4);
        root.add(hbox, 0, 5);
        root.add(message, 0, 7, 1, 1);
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 5, 10));

        scene = new Scene(root, 450, 300);
        back.setOnAction(event -> {
            message.setText("");
            Main.getPrimaryStage().setScene(WelcomeWindow.scene);
        });
        ok.setOnAction(event -> {
            try {
                if (name.getText().equals("")) {
                    throw new ErrorException(this, "ERROR: Film name can not be empty!");
                } else if (trailerPath.getText().equals("")) {
                    throw new ErrorException(this, "ERROR: Trailer path could not be empty!");
                } else {
                    try {
                        if (Integer.parseInt(duration.getText()) <= 0) {
                            throw new ErrorException(this, "ERROR: Duration has to be a positive integer!");
                        }
                    } catch (NumberFormatException e) {
                        throw new ErrorException(this, "ERROR: Duration has to be a positive integer!");

                    }
                }

                File film = new File("assets/trailers/" + trailerPath.getText());

                if (!Database.films.containsKey(name.getText())) {
                    /**
                     * If the film is not found in the trailers file it will give error message to
                     * user.
                     */
                    if (!film.exists()) {
                        new ErrorException(this, "ERROR : There is no such a trailer!");
                    } else {
                        Database.films.put(name.getText(),
                                new Film(name.getText(), trailerPath.getText(), duration.getText()));
                        WelcomeWindow.comboBox.getItems().add(name.getText());
                        WelcomeWindow.comboBox.getSelectionModel().selectLast();
                        cleanFields();
                        message.setText("SUCCESS: Film added successfully!");
                        Database.updateData();
                    }

                } else {
                    new ErrorException(this, "ERROR: This film already exists!");
                }

            } catch (ErrorException e) {
            } catch (Exception e) {
            }

        });

    }
}
