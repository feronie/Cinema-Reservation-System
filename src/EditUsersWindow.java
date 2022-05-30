import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditUsersWindow extends Stage {

    TableView<User> table = new TableView<>();
    TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
    TableColumn<User, Boolean> isClubMemberColumn = new TableColumn<>("Club Member");
    TableColumn<User, Boolean> isAdminColumn = new TableColumn<>("Admin");
    Button back = new Button(Character.toString((char)0x25C0)+" BACK");
    Button secondButton = new Button("Promote/Demote Club Member");
    Button thirdButton = new Button("Promote/Demote Admin");
    Scene scene;
    User currentUser;

    EditUsersWindow(User user) throws IOException {
        this.currentUser = user;
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        isClubMemberColumn.setCellValueFactory(new PropertyValueFactory<>("isClubMember"));

        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));
        table.getColumns().add(usernameColumn);
        table.getColumns().add(isClubMemberColumn);
        table.getColumns().add(isAdminColumn);
        Database.checkData();
        table.setItems(getUser(Database.users));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectFirst();
        table.setPlaceholder(new Label("No user available in the database!"));
        HBox buttons = new HBox(back, secondButton, thirdButton);
        buttons.setSpacing(5);
        VBox vBox = new VBox(table, buttons);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 50, 10, 50));
        scene = new Scene(vBox);
        this.setScene(scene);
        if (table.getItems().isEmpty()) {
            secondButton.setVisible(false);
            thirdButton.setVisible(false);
        }
        back.setOnAction(event -> {
            try {
                Main.getPrimaryStage().setScene(new WelcomeWindow(user).getScene());
            } catch (IOException e) {
            }

        });
        secondButton.setOnAction(event -> {
            User selectedUser = table.getSelectionModel().getSelectedItem();
            if (selectedUser.isClubMember) {
                Database.users.get(selectedUser.username).setClubMember(false);
            } else {
                Database.users.get(selectedUser.username).setClubMember(true);
            }
            try {
                Database.updateData();
                table.refresh();

            } catch (IOException e) {
            }

        });
        thirdButton.setOnAction(event -> {
            User selectedUser = table.getSelectionModel().getSelectedItem();
            if (selectedUser.isAdmin) {
                Database.users.get(selectedUser.username).setAdmin(false);
            } else {
                Database.users.get(selectedUser.username).setAdmin(true);
            }
            try {
                Database.updateData();
                table.refresh();

            } catch (IOException e) {
            }
        });
    }

    public ObservableList<User> getUser(HashMap<String, User> map) {
        ArrayList<User> users = new ArrayList<>();
        for (String name : map.keySet()) {
            if (!currentUser.getUsername().equals(map.get(name).getUsername())) {
                users.add(map.get(name));
            }
        }
        ObservableList<User> list = FXCollections.observableArrayList(users);
        return list;
    }

}