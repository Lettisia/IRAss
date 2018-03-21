import java.io.File;
import java.io.IOException;

public class Index {

    public static void main(String[] args) {
        String stopFile = null;
        String articleFile = null;
        boolean removeStopWords = false;
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
            new ParserManager(articleFile, printTerms, removeStopWords, stopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
