import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Index {

    public static void main(String[] args) {
        String stopFile = "DocumentIndexing/src/resources/stoplist";
        String articleFile = null;
        boolean removeStopWords = false;
        boolean printTerms = false;


        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-p":
                        printTerms = true;
                        break;
                    case "-s":
                        removeStopWords = true;
                        stopFile = args[i + 1];
                        i++;
                        break;
                    default:
                        articleFile = args[i];
                        break;
                }
            }
        }

        try {
            if (articleFile == null) {
                File file = new File("DocumentIndexing/src/resources/latimes");
                articleFile = file.getAbsolutePath();
            }
            if (removeStopWords) {
                long startTime = System.nanoTime();

                InvertedIndexGenerator generator = new InvertedIndexGenerator(articleFile, printTerms, stopFile);
                generator.createInvertedIndex();

                long endTime = System.nanoTime();

                double duration = (endTime - startTime) * 0.000000001;
                Toolkit.getDefaultToolkit().beep();
                System.out.println("Time: " + duration + "seconds");
            } else {
                InvertedIndexGenerator generator = new InvertedIndexGenerator(articleFile, printTerms);
                generator.createInvertedIndex();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
