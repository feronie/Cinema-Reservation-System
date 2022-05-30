import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ErrorException extends Exception {

    ErrorException(SignUpWindow signUpWindow, String message) {
        signUpWindow.cleanFields();
        Media errorSound = new Media(new File("assets/effects/error.mp3").toURI().toString());
        MediaPlayer sound = new MediaPlayer(errorSound);
        sound.play();
        signUpWindow.getWarningMessage().setText(message);
    }

    ErrorException(LogInWindow window, String message) {

        window.username.clear();
        window.password.clear();
        Media errorSound = new Media(new File("assets/effects/error.mp3").toURI().toString());
        MediaPlayer sound = new MediaPlayer(errorSound);
        sound.play();
        window.getWarningMessage().setText(message);
    }

    ErrorException(AddFilmWindow window, String message) {
        window.cleanFields();
        Media errorSound = new Media(new File("assets/effects/error.mp3").toURI().toString());
        MediaPlayer sound = new MediaPlayer(errorSound);
        sound.play();
        window.message.setText(message);
    }

    ErrorException(AddHallWindow window, String message) {
        window.cleanFields();
        Media errorSound = new Media(new File("assets/effects/error.mp3").toURI().toString());
        MediaPlayer sound = new MediaPlayer(errorSound);
        sound.play();
        window.message.setText(message);
    }

    ErrorException(FilmWindow window, String message) {
        Media errorSound = new Media(new File("assets/effects/error.mp3").toURI().toString());
        MediaPlayer sound = new MediaPlayer(errorSound);
        sound.play();
        window.message.setText(message);
    }

    ErrorException(WelcomeWindow window, String message) {
        Media errorSound = new Media(new File("assets/effects/error.mp3").toURI().toString());
        MediaPlayer sound = new MediaPlayer(errorSound);
        sound.play();
        window.message.setText(message);
    }

}