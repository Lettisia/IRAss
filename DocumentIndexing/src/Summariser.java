import java.io.File;
import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Summariser {
    private File file;
    private static final Pattern HEADLINE_TAG_REGEX = Pattern.compile("<HEADLINE>(.+?)</HEADLINE>");
    private static final Pattern TEXT_TAG_REGEX = Pattern.compile("<TEXT>(.+?)</TEXT>");
    private static final Pattern PARAGRAPH_REGEX = Pattern.compile("<P>|</P>");
    private static final String DOC_END_TAG = "</DOC>";

    public Summariser(String filename) {
        file = new File(filename);
    }

    public ArrayList<String> generateSummaries(ArrayList<String> docNumbers, int summaryType, ArrayList<String> query) {
        ArrayList<String> summaries = new ArrayList<>();
        for (String docNo :
                docNumbers) {
            String document = readDocument(docNo);
            ArrayList<String> sentences = makeSentences(document);
            if (sentences == null) {
                summaries.add("Article " + docNo + " does not contain text. It may be an image.");
                System.out.println("Article " + docNo + " does not contain text. It may be an image.");
            } else {
                SentenceSimilarity similar = new SentenceSimilarity(sentences);
                String summary = similar.generateSummary(summaryType, query);
                summaries.add(summary);
                System.out.println(docNo + ": \n" + summary + "\n|");
            }
        }
        return summaries;
    }

    private String readDocument(String docNo) {
        StringBuilder builder = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains(docNo)) {
                    while (scanner.hasNext() && !line.contains(DOC_END_TAG)) {
                        line = scanner.nextLine();
                        builder.append(line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private ArrayList<String> makeSentences(String document) {
        ArrayList<String> sentences = new ArrayList<>();

        String headline = getText(document, HEADLINE_TAG_REGEX);
        sentences.add(headline);

        String text = getText(document, TEXT_TAG_REGEX);

        if (headline.equals("") && text.equals("")) {
            return null;
        }

        BreakIterator boundary = BreakIterator.getSentenceInstance();
        boundary.setText(text);
        int start = boundary.first();
        int end = boundary.next();

        if (start >= 0 && end >= 0) {
            do {
                sentences.add(text.substring(start, end).trim());
                start = end;
                end = boundary.next();
            } while (end != BreakIterator.DONE);
        } else {
            sentences.add(text);
        }

        return sentences;
    }

    private String getText(String document, Pattern regex) {
        String headline = "";
        Matcher matcher = regex.matcher(document);
        if (matcher.find()) {
            headline = matcher.group(1);
        }
        matcher = PARAGRAPH_REGEX.matcher(headline);
        if (matcher.find()) {
            headline = matcher.replaceAll(" ");
        }
        return headline.trim();
    }

    public static void main(String[] args) {
        Summariser sum = new Summariser("latimes-100");
        ArrayList<String> docNos = new ArrayList<>();
//        for (int i=1; i<10; i++) {
//            docNos.add("LA010189-000" + i);
//        }
//        for (int i=10; i<100; i++) {
//            docNos.add("LA010189-00" + i);
//        }
        docNos.add("LA010189-0002");

        ArrayList<String> query = new ArrayList<>();
        query.add("Tocqueville");
        query.add("Andre");
        query.add("Jardin");


//        docNos.add("LA010189-00" + 19);
        ArrayList<String> summaries = sum.generateSummaries(docNos, SentenceSimilarity.QUERY_BIASED, query);
//        for (String summary :
//                summaries) {
//            System.out.println(summary + "\n");
//        }
    }

}
