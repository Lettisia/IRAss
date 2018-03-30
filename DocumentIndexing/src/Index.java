import java.io.File;
import java.io.IOException;

public class Index {

    public static void main(String[] args) {
        String stopFile = "DocumentIndexing/src/resources/stoplist";
        String articleFile = null;
        boolean removeStopWords = true;
        boolean printTerms = true;

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-p")) {
                    printTerms = true;
                } else if (args[i].equals("-s")) {
                    removeStopWords = true;
                    stopFile = args[i + 1];
                    i++;
                } else {
                    articleFile = args[i];
                }
            }
        }

        try {
            if (articleFile == null) {
                File file = new File("DocumentIndexing/src/resources/latimes-100");
                articleFile = file.getAbsolutePath();
            }
            if (removeStopWords)
                new InvertedIndexGenerator(articleFile, printTerms, stopFile);
            else
                new InvertedIndexGenerator(articleFile, printTerms);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
