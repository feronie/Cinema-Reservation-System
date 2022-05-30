import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    static Scene scene;
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        setPrimaryStage(primaryStage);
        PropertyReader properties = new PropertyReader("assets/data/properties.dat");

        stage.setTitle(properties.title); // Set the stage title
        stage.getIcons().add(new Image("assets/icons/logo.png"));
        new LogInWindow(new SignUpWindow());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setScene(Scene root) {
        stage.setScene(root);
    }

    public static void setPrimaryStage(Stage stage) {
        Main.stage = stage;
    }

    public static Stage getPrimaryStage() {
        return stage;
    }

}