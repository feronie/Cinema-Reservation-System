public class Seat {

    String filmName;
    String hallName;
    int row, column;
    String ownerName;
    int price;
    Film film;

    Seat(String filmName, String hallName, int row, int column, String ownerName, int price) {
        this.filmName = filmName;
        this.hallName = hallName;
        this.row = row;
        this.column = column;
        this.ownerName = ownerName;
        this.price = price;
    }

    @Override
    public String toString() {

        return "seat\t" + filmName + "\t" + hallName + "\t" + row + "\t" + column + "\t" + ownerName + "\t" + price+ "\n";

    }

}
