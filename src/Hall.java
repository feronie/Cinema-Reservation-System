public class Hall {

    Film film;
    String hallName;
    int price;
    Seat[][] seats;
    int row;
    int column;

    Hall(String filmName, String hallName, int price, int row, int column) {
        film = new Film(filmName);
        this.hallName = hallName;
        this.price = price;
        this.row = row;
        this.column = column;
        this.seats = new Seat[row][column];
    }

    @Override
    public String toString() {
        return "hall\t" + film.filmName + "\t" + hallName + "\t" + price + "\t" + row + "\t" + column + "\n";
    }
}
