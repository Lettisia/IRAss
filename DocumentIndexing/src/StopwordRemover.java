import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class StopwordRemover {
    private HashMap<String, String> stopwords = new HashMap<>();


    public StopwordRemover(String filename) {
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
            System.err.println("Bad File " + filename);
            exception.printStackTrace();
        }
    }

    public ArrayList<String> removeStopwords(ArrayList<String> tokens) {
        ArrayList<String> result = new ArrayList<>();
        for (String word : tokens) {
            if (!stopwords.containsKey(word)) {
                result.add(word);
            }
        }
        return result;
    }

    // Test
    public static void main(String [] args) {
        String sentence = "Your program must be called index and should accept an optional command line argument";
        ArrayList<String> document = new ArrayList<>(Arrays.asList(sentence.split(" ")));
        StopwordRemover remover = new StopwordRemover("DocumentIndexing/src/resources/stoplist");
        ArrayList<String> stopped = remover.removeStopwords(document);
        System.out.println(sentence);
        System.out.println(stopped);
    }


}