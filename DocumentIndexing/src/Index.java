import java.io.File;
import java.io.IOException;

public class Index {

    public static void main(String[] args) {
        String stopFile = null;
        String articleFile = null;
        boolean removeStopWords = false;
        boolean printTerms = false;

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
                File dir = new File("src/resources");
                articleFile = dir.getCanonicalPath() + File.separator + "latimes";
            }
            new ParserManager(articleFile, printTerms, removeStopWords, stopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
