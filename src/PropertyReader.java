import java.util.*;
import java.io.*;

public class PropertyReader {
    String title;
    int discountPercentage;

    PropertyReader(String pathname) throws IOException {
        FileReader input = new FileReader(new File(pathname));
        Properties property = new Properties();
        property.load(input);
        this.title = property.getProperty("title");
        this.discountPercentage = Integer.parseInt(property.getProperty("discount-percentage"));

    }

}
