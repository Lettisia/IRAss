import java.util.ArrayList;

public class Index {

    public static void main(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-p")) {
                    DocumentParser parser = new DocumentParser(args[++i]);
                    ArrayList<String> documentCollection = parser.parse();
                    i++;
                } else if (args[i].equals("-s")) {
                    StopwordRemover stopper = new StopwordRemover(args[++i]);
                    stopper.stop(documentCollection);
                }
            }
        }
    }

}
