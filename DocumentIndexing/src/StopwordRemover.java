import java.util.Scanner;
import java.util.ArrayList;

public class StopwordRemover {
    private String [] stopwords;

    public StopwordRemover(String filename) {
        parseStopwordList(filename);
    }

    private ArrayList<Article> parseStopwordList(String filename) {
        try {
            Scanner input = new Scanner(filename);

            while (input.hasNext()) {
                String line = input.next();
            }

            input.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }




}