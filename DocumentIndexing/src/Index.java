import java.awt.*;
import java.io.File;
import java.io.IOException;

class Index {

    public static void main(String[] args) {
        String stopFile = "/home/inforet/a1/stoplist";
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
                File file = new File("home/inforet/a1/latimes");
                articleFile = file.getAbsolutePath();
            }

            long startTime = System.nanoTime();
            InvertedIndexGenerator generator;

            if (removeStopWords) {
                generator = new InvertedIndexGenerator(articleFile, printTerms, stopFile);
            } else {
                generator = new InvertedIndexGenerator(articleFile, printTerms);
            }

            generator.createInvertedIndex();
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) * 0.000000001 / 60.0;
            Toolkit.getDefaultToolkit().beep();
            System.out.println("Time: " + duration + "seconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
