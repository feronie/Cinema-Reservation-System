import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CinemaHall extends Stage {

    ArrayList<Seat> seats = new ArrayList<>();
    GridPane pane = new GridPane();
    GridPane buttons = new GridPane();
    Button back = new Button(Character.toString((char)0x25C0)+" BACK");
    Text header = new Text();
    ImageView seat;
    User user;
    Hall hall;
    ComboBox<String> users = new ComboBox<>();

    public CinemaHall(Hall hall, User user) throws IOException {
        this.user = user;
        this.hall = hall;
        Database.checkData();
        users.getItems().addAll(Database.users.keySet());
        users.getSelectionModel().selectLast();
        Text warningMessage = new Text();
        Text message = new Text();
        warningMessage.setTextAlignment(TextAlignment.CENTER);
        header.setText(hall.film.filmName + " (" + Database.films.get(hall.film.filmName).duration + " Minutes) "
                + "Hall: " + hall.hallName);
        header.setTextAlignment(TextAlignment.CENTER);

        buttons.setHgap(10);
        buttons.setVgap(10);
        buttons.setAlignment(Pos.CENTER);

        pane.setHgap(5);
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(15, 15, 15, 15));

        int row = hall.row;
        int column = hall.column;

        for (int i = 0; i < row; i++) {
            for (int k = 0; k < column; k++) {
                if (hall.seats[i][k].ownerName.equals("null")) {
                    seat = new ImageView(new Image("assets/icons/empty_seat.png"));
                } else {
                    seat = new ImageView(new Image("assets/icons/reserved_seat.png"));
                }
                seat.setFitHeight(40);
                seat.setFitWidth(40);
                buttons.add(new Button("", seat), k, i);
                seats.add(hall.seats[i][k]);
                if (!user.isAdmin) {
                    setbuttonsForNonAdmins(seats, seats.size() - 1);
                }

            }
        }
        pane.add(header, 0, 0);
        pane.add(buttons, 0, 1);
        Scene scene = new Scene(pane);
        this.setScene(scene);

        if (user.isAdmin) {
            if (!hall.hallName.equals("")) {
                pane.add(users, 0, 3);
            }

            pane.add(warningMessage, 0, 4);
            pane.add(message, 0, 5);
            pane.add(back, 0, 6);
            for (int i = 0; i < buttons.getChildren().size(); i++) {
                int index = i;
                buttons.getChildren().get(i).setOnMouseEntered(mouseEvent -> {
                    if (seats.get(index).ownerName.equals("null")) {
                        warningMessage.setText("Not bought yet!");
                    } else {
                        warningMessage.setText(
                                "Bought by " + seats.get(index).ownerName + " for " + seats.get(index).price + " TL !");
                    }
                    ;
                });
            }

            for (int i = 0; i < buttons.getChildren().size(); i++) {
                buttons.getChildren().get(i).setOnMouseExited(mouseEvent -> {
                    warningMessage.setText("");
                });
            }

        } else {
            setbuttonsForNonAdmins(seats, seats.size() - 1);
            pane.add(warningMessage, 0, 2);
            pane.add(message, 0, 3);
            pane.add(back, 0, 4);
        }
        for (int i = 0; i < buttons.getChildren().size(); i++) {
            int index = i;
            ((Button) buttons.getChildren().get(i)).setOnAction(mouseEvent -> {

                Seat seat = seats.get(index);
                int r = seat.row + 1;
                int c = seat.column + 1;
                if (!seat.ownerName.equals("null")) {

                    message.setText("Seat at " + r + "-" + c + " is refunded to " + seat.ownerName + " successfully!");
                    seat.ownerName = "null";
                    Database.halls.get(hall.hallName).seats[seat.row][seat.column].ownerName = "null";

                    try {
                        Database.updateData();
                    } catch (IOException e) {
                    }
                    this.seat = new ImageView(new Image("assets/icons/empty_seat.png"));
                    this.seat.setFitHeight(40);
                    this.seat.setFitWidth(40);
                    ((Button) buttons.getChildren().get(index)).setGraphic(this.seat);
                } else {
                    String selectedUserName;
                    if (user.isAdmin) {
                        selectedUserName = users.getSelectionModel().getSelectedItem();
                    } else {
                        selectedUserName = user.username;
                    }
                    Database.halls.get(hall.hallName).seats[seat.row][seat.column].ownerName = selectedUserName;
                    seat.ownerName = selectedUserName;
                    try {
                        setPrice(Database.users.get(selectedUserName), seat);
                        Database.halls.get(hall.hallName).seats[seat.row][seat.column].price = seat.price;
                        Database.updateData();
                    } catch (IOException e) {
                    }
                    this.seat = new ImageView(new Image("assets/icons/reserved_seat.png"));
                    this.seat.setFitHeight(40);
                    this.seat.setFitWidth(40);
                    ((Button) buttons.getChildren().get(index)).setGraphic(this.seat);
                    message.setText("Seat at " + r + "-" + c + " is bought for " + seat.ownerName + " for " + seat.price
                            + "  TL successfully!");

                }

            });
        }
        back.setOnAction(event -> {
            try {
                Main.getPrimaryStage().setScene(new FilmWindow(hall.film.filmName, user).getScene());
            } catch (MalformedURLException e) {
            } catch (IOException e) {

            }
        });
    }

    public void setbuttonsForNonAdmins(ArrayList<Seat> seats, int i) {
        if (seats.get(i).ownerName.equals(user.username) || seats.get(i).ownerName.equals("null")) {
            ((Button) buttons.getChildren().get(i)).setDisable(false);
        } else {
            ((Button) buttons.getChildren().get(i)).setDisable(true);

        }
    }

    public void setPrice(User user, Seat seat) throws IOException {
        PropertyReader properties = new PropertyReader("assets/data/properties.dat");
        int discountPercentage = properties.discountPercentage;
        if (user.isClubMember) {
            int price = (hall.price * (100 - discountPercentage)) / 100;
            seat.price = price;
        } else {
            seat.price = hall.price;
        }
    }
}