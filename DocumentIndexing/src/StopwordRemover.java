import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class StopwordRemover {
    private final HashMap<String, String> stopwords = new HashMap<>();

    StopwordRemover(String filename) {
        readStopwordsFromFile(filename);
    }

    private void readStopwordsFromFile(String filename) {
        try {
            Scanner input = new Scanner(new FileInputStream(filename));
            while (input.hasNext()) {
                stopwords.put(input.next(), "");
            }

            input.close();
        } catch (Exception exception) {
            System.err.println("Error: Bad Stopword File - " + filename);
        }
    }

    ArrayList<String> removeStopwords(ArrayList<String> tokens) {
        ArrayList<String> result = new ArrayList<>();
        for (String word : tokens) {
            if (!stopwords.containsKey(word)) {
                result.add(word);
            }
        }
        return result;
    }

}