import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedIndexGenerator {

    private static final Pattern DOC_TAG_REGEX = Pattern.compile("<doc>(.+?)</doc>");
    private static final Pattern DOCNO_TAG_REGEX = Pattern.compile("<docno>(.+?)</docno>");
    private static final Pattern HEADLINE_TAG_REGEX = Pattern.compile("<headline>(.+?)</headline>");
    private static final Pattern TEXT_TAG_REGEX = Pattern.compile("<text>(.+?)</text>");
    private static final Pattern FILTER_REGEX = Pattern.compile("<p>|</p>|[\\p{Punct}]");
    private static final String DOC_END_TAG = "</DOC>";

    private Map<Integer, String> DocumentIDMap = new HashMap<>();
    private List<Article> articles = new ArrayList<>();
    private int articleCount = 0;
    private Scanner scanner = null;
    private StopwordRemover stopwordRemover = null;

    public InvertedIndexGenerator(String source, boolean printTerms, String stopFile) throws IOException {
        try {
            scanner = new Scanner(new FileInputStream(source));
            String document = readOneDocFromFile().toLowerCase();
            Article article = loadOneArticle(document);
            stopwordRemover = new StopwordRemover(stopFile);
            article.parse(stopwordRemover);
            System.out.println("Terms: " + article.getTerms().size());
            System.out.println(article.getTerms());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public InvertedIndexGenerator(String source, boolean printTerms) {
        try {
            scanner = new Scanner(new FileInputStream(source));
            String document = readOneDocFromFile().toLowerCase();
            loadAllValues(document);
            articles.get(0).parse();
            printList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
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


    private String readFile(String source) throws IOException {
        String document = "";
        BufferedReader inputFile = new BufferedReader(new InputStreamReader(new FileInputStream(new File(source))));
        String aLine;
        while ((aLine = inputFile.readLine()) != null) {
            document += aLine;
        }
        inputFile.close();

        return document.toLowerCase();
    }

    private Article loadOneArticle(String document) {
        Article article = null;
        int articleCount = 0;
        String docNo = null;
        String headline = null;
        String text = null;
        Matcher matcher = DOC_TAG_REGEX.matcher(document);

        while (matcher.find()) {
            docNo = findMatch(matcher.group(1), "DOCNO");
            headline = findMatch(matcher.group(1), "HEADLINE");
            text = findMatch(matcher.group(1), "TEXT");
            article = new Article(articleCount, docNo, headline, text);
            DocumentIDMap.put(articleCount, docNo);
        }
        return article;
    }

    private void loadAllValues(String document) {
        Article article = null;
        int articleCount = 0;
        String docNo = null;
        String headline = null;
        String text = null;
        Matcher matcher = DOC_TAG_REGEX.matcher(document);

        while (matcher.find()) {
            articleCount++;

            docNo = findMatch(matcher.group(1), "DOCNO");
            headline = findMatch(matcher.group(1), "HEADLINE");
            text = findMatch(matcher.group(1), "TEXT");
            article = new Article(articleCount, docNo, headline, text);
            DocumentIDMap.put(articleCount, docNo);
            articles.add(article);
        }
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
            //throw new IllegalArgumentException
        }

        if (matcher.find()) {
            if (tag.equalsIgnoreCase("HEADLINE") || tag.equalsIgnoreCase("TEXT"))
                return FILTER_REGEX.matcher(matcher.group(1)).replaceAll("");
            else
                return matcher.group(1);
        }
        return null;
    }

    private void printList() {
        for (Article article : articles) {
            System.out.println(article.getTerms());
        }
    }

    private void printMap() {
        for (Entry<Integer, String> entry : DocumentIDMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

}
