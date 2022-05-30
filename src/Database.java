import java.io.*;

import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Base64;
import java.util.HashMap;

public class Database {

    static HashMap<String, User> users = new HashMap<>();
    static HashMap<String, User> passwords = new HashMap<>();
    static HashMap<String, Film> films = new HashMap<>();
    static HashMap<String, Hall> halls = new HashMap<>();

    public static void checkData() throws IOException {
        try {
            Scanner backup = new Scanner(new File("assets/data/backup.dat"));
            while (backup.hasNextLine()) {
                String line = backup.nextLine();
                if (line.equals("")) {
                    continue;
                } else {
                    String[] array = line.split("\t");
                    switch (array[0]) {
                        case ("user"):
                            User user = new User(array[1], array[2], Boolean.valueOf(array[3]),
                                    Boolean.valueOf(array[4]));
                            users.put(user.username, user);
                            passwords.put(user.username, user);
                            break;
                        case ("film"):
                            Film film = new Film(array[1], array[2], array[3]);
                            films.put(film.filmName, film);
                            break;
                        case ("hall"):
                            Hall hall = new Hall(array[1], array[2], Integer.parseInt(array[3]),
                                    Integer.parseInt(array[4]), Integer.parseInt(array[5]));
                            halls.put(hall.hallName, hall);
                            break;
                        case ("seat"):
                            Seat seat = new Seat(array[1], array[2], Integer.parseInt(array[3]),
                                    Integer.parseInt(array[4]), array[5], Integer.parseInt(array[6]));
                            seat.film = films.get(array[1]);
                            halls.get(array[2]).seats[Integer.parseInt(array[3])][Integer.parseInt(array[4])] = seat;
                    }
                }
            }

            /**
             * if there is no backup file it will create an initial backupdat which contains
             * only an admin that is club member namely admin with a password of "password"
             */
        } catch (FileNotFoundException e) {
            FileWriter backup = new FileWriter(new File("assets/data/backup.dat"));
            backup.append("user\tadmin\t" + hashPassword("password") + "\ttrue\ttrue");
            backup.close();
        }
    }

    /**
     * 
     * @throws IOException
     *                     This method is updating the backupdat.
     */
    public static void updateData() throws IOException {
        FileWriter backup = new FileWriter(new File("assets/data/backup.dat"));
        iterateMap(users, backup);
        iterateMap(films, backup);
        iterateMap(halls, backup);
        writeSeats(halls, backup);
        backup.close();
    }

    public static String hashPassword(String password) {
        byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
        byte[] md5Digest = new byte[0];
        try {
            md5Digest = MessageDigest.getInstance("MD5").digest(bytesOfPassword);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return Base64.getEncoder().encodeToString(md5Digest);
    }

    public static <K, V> void iterateMap(HashMap<K, V> map, FileWriter file) throws IOException {
        for (K key : map.keySet()) {
            file.write(map.get(key).toString());
        }

    }

    public static <K, V> void writeSeats(HashMap<String, Hall> halls, FileWriter file) throws IOException {
        for (String key : halls.keySet()) {
            for (int row = 0; row < halls.get(key).row; row++) {
                for (int col = 0; col < halls.get(key).column; col++) {
                    file.write(halls.get(key).seats[row][col].toString());
                }
            }
        }

    }

}