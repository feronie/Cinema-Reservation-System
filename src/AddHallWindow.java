import java.io.IOException;
import java.net.MalformedURLException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddHallWindow extends Stage {

    Button back = new Button(Character.toString((char)0x25C0)+" BACK");
    Button ok = new Button("OK");
    HBox buttons = new HBox(back, ok);
    Text row = new Text("Row:");
    Text column = new Text("Column:");
    Text name = new Text("Name:");
    Text price = new Text("Price:");
    TextField nameField = new TextField();
    TextField priceField = new TextField();
    Integer[] indices = { 3, 4, 5, 6, 7, 8, 9, 10 };
    ComboBox<Integer> rows = new ComboBox<>();
    ComboBox<Integer> columns = new ComboBox<>();

    Text message = new Text();

    AddHallWindow(String filmName, User user) throws IOException {
        Database.checkData();
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        rows.getItems().addAll(indices);
        columns.getItems().addAll(indices);
        Text film = new Text(filmName + " (" + Database.films.get(filmName).duration + "minutes)");
        VBox root = new VBox(film, createHBox(row, rows), createHBox(column, columns), createHBox(name, nameField),
        createHBox(price, priceField), buttons, message);
        root.setSpacing(5);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root,300,400);
        this.setScene(scene);

        ok.setOnAction(event -> {
            try {
                if (nameField.getText().isEmpty()) {
                    throw new ErrorException(this, "ERROR: Hall name could not be empty!");
                } else if (priceField.getText().isEmpty()) {
                    throw new ErrorException(this, "ERROR: Price could not be empty!");
                } else {
                    try {
                        if (Integer.parseInt(priceField.getText()) <= 0) {
                            throw new ErrorException(this, "ERROR: Price has to be a positive integer!");
                        }
                    } catch (NumberFormatException e) {
                        throw new ErrorException(this, "ERROR: Price has to be a positive integer!");

                    }
                }
                int row = rows.getValue();
                int column = columns.getValue();
                String hallName = nameField.getText();
                int price = Integer.parseInt(priceField.getText());
                if (Database.halls.containsKey(nameField.getText())) {
                    throw new ErrorException(this, "ERROR: This hall already exists!");
                } else {
                    Database.halls.put(hallName, new Hall(filmName, hallName, price, row, column));
                    for (int r = 0; r < row; r++) {
                        for (int col = 0; col < column; col++) {
                            Seat seat = new Seat(filmName, hallName, r, col, null, 0);
                            seat.film = Database.films.get(filmName);
                            Database.halls.get(hallName).seats[r][col] = seat;

                        }
                    }
                    Database.updateData();
                    message.setText("SUCCESS: Hall successfully created!");
                }

            } catch (Exception e) {}

        });
        back.setOnAction(event -> {
            message.setText("");
            try {
                Main.getPrimaryStage().setScene(new FilmWindow(filmName, user).getScene());
            } catch (MalformedURLException e) {
            } catch (IOException e) {}
        });

    }

    public HBox createHBox(Text text, TextField field) {
        HBox hbox = new HBox(text, field);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    public HBox createHBox(Text text, ComboBox<Integer> index) {
        index.getSelectionModel().selectFirst();
        HBox hBox = new HBox(text, index);
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }
    public void cleanFields() {
        nameField.clear();
        priceField.clear();
    }
}
