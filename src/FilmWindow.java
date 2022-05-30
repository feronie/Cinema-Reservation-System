import java.io.File;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FilmWindow extends Stage {

    Button play = new Button();
    Button skipBack = new Button(Character.toString((char)0x23EA));
    Button skipForward = new Button(Character.toString((char)0x23E9));
    Button rewind = new Button(Character.toString((char)0x23EE));
    Slider volumeSlider = new Slider();
    Text text = new Text();
    Button back = new Button(Character.toString((char)0x25C0)+" BACK");
    
    Button addHall = new Button("Add Hall");
    Button removeHall = new Button("Remove Hall");
    Button ok = new Button("OK");
    ComboBox<String> halls = new ComboBox<>();
    Text message = new Text();
    HBox hBox;

    FilmWindow(String filmName, User user) throws IOException {

        Database.checkData();
        Film film = Database.films.get(filmName);
        GridPane root = new GridPane();
        root.setHgap(5);
        root.setVgap(5);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30, 30, 30, 30));

        Media media = new Media(new File("assets/trailers/" + film.trailerPath).toURI().toURL().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView view = new MediaView(mediaPlayer);

        root.add(view, 0, 2);
        view.setFitWidth(650);

        play.setText(Character.toString((char)0x23F5));

        text.setText(filmName + " (" + film.duration + " minutes)");
        root.add(text, 0, 0, 1, 1);

        volumeSlider.setOrientation(Orientation.VERTICAL);
        VBox vBox = new VBox(play, skipBack, skipForward, rewind, volumeSlider);
        vBox.setSpacing(5);
        root.add(vBox, 1, 2);
        root.add(message, 0, 4);
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100));
        volumeSlider.valueProperty().set(50); // setting the initial value of volume

        /**To add halls to combobox,which are related with this movie this method is
        /*created.
        */
        for (String key : Database.halls.keySet()) {
            if (Database.halls.get(key).film.filmName.equals(filmName)) {
                halls.getItems().add(key);
            }
        }
        halls.getSelectionModel().selectLast();

        if (user.isAdmin) {
            hBox = new HBox(back, addHall, removeHall, halls, ok);
            hBox.setSpacing(5);

        } else {
            hBox = new HBox(back, halls, ok);
            hBox.setSpacing(10);
        }
        root.add(hBox, 0, 3);

        Scene scene = new Scene(root, 800, 600);
        this.setScene(scene);

        play.setOnAction(event -> {
            if (play.getText().equals(Character.toString((char)0x23F5))) {
                mediaPlayer.play();
                play.setText(Character.toString((char)0x23F8));
            } else {
                mediaPlayer.pause();
                play.setText(Character.toString((char)0x23F5));
            }
        });
        skipBack.setOnAction(event -> {
            mediaPlayer.seek(Duration.seconds((mediaPlayer.getCurrentTime().toSeconds() - 5)));
        });
        skipForward.setOnAction(event -> {
            mediaPlayer.seek(Duration.seconds((mediaPlayer.getCurrentTime().toSeconds() + 5)));
        });
        rewind.setOnAction(event -> {
            mediaPlayer.seek(Duration.ZERO);
        });

        back.setOnAction(event -> {
            try {
                message.setText("");
                mediaPlayer.stop();
                Main.getPrimaryStage().setScene(new WelcomeWindow(user).getScene());
            } catch (IOException e) {
            }
        });
        removeHall.setOnAction(event -> {
            String selectedHallName = halls.getSelectionModel().getSelectedItem();
            Database.halls.remove(selectedHallName);
            halls.getItems().remove(selectedHallName);
            halls.getSelectionModel().selectLast();
            try {
                Database.updateData();
            } catch (IOException e) {
            }
        });
        addHall.setOnAction(event -> {
            try {
                message.setText("");
                mediaPlayer.stop();
                Main.getPrimaryStage().setScene(new AddHallWindow(filmName, user).getScene());
            } catch (IOException e) {
            }
        });
        ok.setOnAction(event -> {
            String selectedHallName = halls.getSelectionModel().getSelectedItem();
            try {
                Main.getPrimaryStage().setScene(new CinemaHall(Database.halls.get(selectedHallName), user).getScene());
                /** if there is no hall in combobox exception will be thrown
                 */
            } catch (NullPointerException e) {
                try {
                    new ErrorException(this, "ERROR: There is no hall for this film!");
                } catch (Exception e1) {
                }
            } catch (IOException e) {
            }

        });

    }
}