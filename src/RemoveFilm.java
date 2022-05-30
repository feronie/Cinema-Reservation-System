import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RemoveFilm extends Stage {

    Text text = new Text("Select the film that you desire to remove and then click OK.");
    GridPane pane = new GridPane();
    Button back = new Button(Character.toString((char)0x25C0)+" BACK");
    Button ok = new Button("OK");
    HBox hbox = new HBox(10, back, ok);
    Scene scene;
    static Database database = new Database();

    RemoveFilm() throws IOException {

        Database.checkData();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(Database.films.keySet());
        comboBox.getSelectionModel().selectFirst();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.add(text, 0, 0, 2, 2);
        pane.add(comboBox, 0, 2);
        pane.add(hbox, 0, 4);
        scene = new Scene(pane, 400, 150);

        back.setOnAction(event -> {
            Main.getPrimaryStage().setScene(WelcomeWindow.scene);
        });
        ok.setOnAction(event -> {
            Database.films.remove(comboBox.getValue());
            WelcomeWindow.comboBox.getItems().remove(comboBox.getValue());
            String filmName = comboBox.getValue();
            ArrayList<String> removeList = new ArrayList<>();
            for (String key : Database.halls.keySet()) {
                if (Database.halls.get(key).film.filmName.equals(filmName)) {
                    removeList.add(key);
                }
            }
            for (String key : removeList) {
                Database.halls.remove(key);
            }

            comboBox.getItems().remove(comboBox.getValue());

            try {
                Database.updateData();
            } catch (IOException e) {
            }
            comboBox.getSelectionModel().selectLast();
        });

    }
}
