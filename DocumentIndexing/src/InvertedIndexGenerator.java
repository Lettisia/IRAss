import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedIndexGenerator {

    private static final Pattern DOC_TAG_REGEX = Pattern.compile("<doc>(.+?)</doc>");
    private static final Pattern DOCNO_TAG_REGEX = Pattern.compile("<docno>(.+?)</docno>");
    private static final Pattern HEADLINE_TAG_REGEX = Pattern.compile("<headline>(.+?)</headline>");
    private static final Pattern TEXT_TAG_REGEX = Pattern.compile("<text>(.+?)</text>");
    private static final Pattern FILTER_REGEX = Pattern.compile("<p>|</p>|[\\p{Punct}]");
    private static final String DOC_END_TAG = "</DOC>";

    private Scanner scanner = null;
    private HashMap<String, IndexEntry> lexicon = new HashMap<>();
    private HashMap<Integer, String> DocumentIDMap = new HashMap<>();
    private boolean printTerms = false;
    private StopwordRemover stopwordRemover = null;


    public InvertedIndexGenerator(String source, boolean printTerms, String stopFile) throws IOException {
        scanner = new Scanner(new FileInputStream(source));
        this.printTerms = printTerms;
        if (stopFile != null) {
            stopwordRemover = new StopwordRemover(stopFile);
        }
    }


    public InvertedIndexGenerator(String source, boolean printTerms) throws IOException {
        this(source, printTerms, null);
    }


    public void createInvertedIndex() {
        while (scanner.hasNext()) {
            String document = readOneDocFromFile().toLowerCase();
            Article article = loadOneArticle(document);
            article.parse(stopwordRemover);
            printTerms(article);
            addToLexicon(article);
        }
        if (printTerms) {
            for (String term : lexicon.keySet()) {
                System.out.println(lexicon.get(term));
            }
            System.out.println(lexicon.size());
        }
    }


    private void addToLexicon(Article article) {
        HashMap<String, Integer> countedTerms = article.countTerms();
        for (String term : countedTerms.keySet()) {
            IndexEntry entry;

            if (lexicon.containsKey(term)) {
                entry = lexicon.get(term);
                entry.documentFrequency++;
            } else {
                entry = new IndexEntry();
                entry.term = term;
                entry.documentFrequency = 1;
            }

            TermFrequencyPair indexTermFrequencyPair = new TermFrequencyPair(article.getDocumentIndex(), countedTerms.get(term));
            entry.invertedList.add(indexTermFrequencyPair);
            lexicon.put(term, entry);
        }
    }

    private void printTerms(Article article) {
        if (printTerms) {
            System.out.println("Terms: " + article.getTerms().size());
            System.out.println(article.getTerms());
        }
    }

    private String readOneDocFromFile() {
        String document = "";
        String aLine = "";
        while (scanner.hasNext() && !aLine.contains(DOC_END_TAG)) {
            aLine = scanner.nextLine();
            document += aLine;
        }
        return document;
    }


    private Article loadOneArticle(String document) {
        Article article = null;
        int articleCount = 0;
        String docNo;
        String headline;
        String text;
        Matcher matcher = DOC_TAG_REGEX.matcher(document);

        while (matcher.find()) {
            docNo = findMatch(matcher.group(1), "DOCNO");
            headline = findMatch(matcher.group(1), "HEADLINE");
            text = findMatch(matcher.group(1), "TEXT");
            article = new Article(docNo, headline, text);
            DocumentIDMap.put(articleCount, docNo);
        }
        return article;
    }


    private String findMatch(String str, String tag) {
        Matcher matcher = null;
        switch (tag) {
            case "DOCNO":
                matcher = DOCNO_TAG_REGEX.matcher(str);
                break;
            case "HEADLINE":
                matcher = HEADLINE_TAG_REGEX.matcher(str);
                break;
            case "TEXT":
                matcher = TEXT_TAG_REGEX.matcher(str);
                break;
            default:
                break;
        }

        if (matcher != null && matcher.find()) {
            if (isTextField(tag))
                return FILTER_REGEX.matcher(matcher.group(1)).replaceAll("");
            else
                return matcher.group(1);
        }
        return null;
    }

    private boolean isTextField(String tag) {
        return tag.equalsIgnoreCase("HEADLINE") || tag.equalsIgnoreCase("TEXT");
    }


    private void printMap() {
        for (Entry<Integer, String> entry : DocumentIDMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

}
