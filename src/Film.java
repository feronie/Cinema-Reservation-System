public class Film {

    String filmName;
    String duration;
    String trailerPath;

    Film(String filmName, String trailerPath, String duration) {
        this.filmName = filmName;
        this.duration = duration;
        this.trailerPath = trailerPath;
    }

    Film(String filmName) {
        this.filmName = filmName;
    }

    @Override
    public String toString() {
        return "film" + "\t" + filmName + "\t" + trailerPath + "\t" + duration + "\n";
    }

}
