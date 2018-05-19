import java.io.File;
import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Summariser {
    private final File file;
    private final int summaryType;
    private static final Pattern HEADLINE_TAG_REGEX = Pattern.compile("<HEADLINE>(.+?)</HEADLINE>");
    private static final Pattern TEXT_TAG_REGEX = Pattern.compile("<TEXT>(.+?)</TEXT>");
    private static final Pattern PARAGRAPH_REGEX = Pattern.compile("<P>|</P>");
    private static final String DOC_END_TAG = "</DOC>";

    Summariser(String filename, int summaryType) {
        file = new File(filename);
        this.summaryType = summaryType;

    }

    private ArrayList<String> generateSummaries(ArrayList<String> docNumbers, ArrayList<String> query) {
        ArrayList<String> summaries = new ArrayList<>();
        for (String docNo : docNumbers) {
            summaries.add(generateSummary(query, docNo));
        }
        return summaries;
    }

    String generateSummary(ArrayList<String> query, String docNo) {
        String document = readDocument(docNo);
        ArrayList<String> sentences = makeSentences(document);
        if (sentences == null) {
            return "Article " + docNo + " does not contain text. It may be an image.";
        } else {
            SentenceRanker ranker = new SentenceRanker(sentences);
            return ranker.generateSummary(summaryType, query);
        }
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
        String headline = extractText(document, HEADLINE_TAG_REGEX);
        String text = extractText(document, TEXT_TAG_REGEX);
        if (headline.equals("") && text.equals("")) {
            return null;
        }
        return tokeniseSentences(headline, text);
    }

    private ArrayList<String> tokeniseSentences(String headline, String text) {
        ArrayList<String> sentences = new ArrayList<>();
        sentences.add(headline);

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

    private String extractText(String document, Pattern regex) {
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
        Summariser sum = new Summariser("latimes-100", SentenceRanker.QUERY_BIASED);
        ArrayList<String> docNos = new ArrayList<>();
        for (int i=1; i<10; i++) {
            docNos.add("LA010189-000" + i);
        }
        for (int i=10; i<100; i++) {
            docNos.add("LA010189-00" + i);
        }
        docNos.add("LA010189-0002");

        ArrayList<String> query = new ArrayList<>();
        query.add("Tocqueville");
        query.add("Andre");
        query.add("Jardin");

        ArrayList<String> summaries = sum.generateSummaries(docNos, query);
        for (String summary :
                summaries) {
            System.out.println(summary + "\n");
        }
    }

}
