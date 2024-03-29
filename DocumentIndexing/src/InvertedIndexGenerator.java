import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InvertedIndexGenerator {
    private static final Pattern DOC_TAG_REGEX = Pattern.compile("<DOC>(.+?)</DOC>");
    private static final Pattern DOCNO_TAG_REGEX = Pattern.compile("<DOCNO>(.+?)</DOCNO>");
    private static final Pattern HEADLINE_TAG_REGEX = Pattern.compile("<HEADLINE>(.+?)</HEADLINE>");
    private static final Pattern TEXT_TAG_REGEX = Pattern.compile("<TEXT>(.+?)</TEXT>");
    private static final Pattern FILTER_REGEX = Pattern.compile("<P>|</P>|[\\p{Punct}]");
    private static final String DOC_END_TAG = "</DOC>";

    private Scanner scanner = null;
    private final HashMap<String, IndexEntry> lexicon = new HashMap<>();
    private boolean printTerms = false;
    private StopwordRemover stopwordRemover = null;


    InvertedIndexGenerator(String source, boolean printTerms, String stopFile) throws IOException {
        scanner = new Scanner(new FileInputStream(source));
        this.printTerms = printTerms;
        if (stopFile != null) {
            stopwordRemover = new StopwordRemover(stopFile);
        }
    }

    InvertedIndexGenerator(String source, boolean printTerms) throws IOException {
        this(source, printTerms, null);
    }

    void createInvertedIndex() {
        ArrayList<Document> documents = new ArrayList<>();
        while (scanner.hasNext()) {
            String document = readOneDocFromFile();
            Document oneDocument = loadOneDocument(document);

            if (oneDocument != null) {
                oneDocument.parse(stopwordRemover);
                if (hasTerms(oneDocument)) {
                    printTerms(oneDocument);
                    addToLexicon(oneDocument);
                }
                documents.add(oneDocument);
            }
        }

        FileWriter writer = new FileWriter(lexicon, documents);
        writer.writeMapFile();
        writer.writeIndexFiles();
    }

    private boolean hasTerms(Document document) {
        return document.getTerms().size() > 0;
    }

    private void addToLexicon(Document document) {
        HashMap<String, Integer> countedTerms = document.countTerms();
        for (String term : countedTerms.keySet()) {
            IndexEntry entry;

            if (lexicon.containsKey(term)) {
                entry = lexicon.get(term);
                entry.setDocumentFrequency(entry.getDocumentFrequency() + 1);
            } else {
                entry = new IndexEntry();
                entry.setTerm(term);
                entry.setDocumentFrequency(1);
            }

            TermFrequencyPair indexTermFrequencyPair = new TermFrequencyPair(document.getDocumentIndex(), countedTerms.get(term));
            entry.getInvertedList().add(indexTermFrequencyPair);
            lexicon.put(term, entry);
        }
    }

    private void printTerms(Document document) {
        if (printTerms) {
            System.out.println("Document: " + document.getDocNo() + " Terms: " + document.getTerms().size());
            System.out.println(document.printTerms());
        }
    }

    private String readOneDocFromFile() {
        StringBuilder document = new StringBuilder();
        String aLine = "";
        while (scanner.hasNext() && !aLine.contains(DOC_END_TAG)) {
            aLine = scanner.nextLine();
            document.append(aLine);
        }
        return document.toString();
    }

    private Document loadOneDocument(String document) {
        Document oneDocument = null;
        String docNo;
        String headline;
        String text;
        Matcher matcher = DOC_TAG_REGEX.matcher(document);

        while (matcher.find()) {
            docNo = findMatch(matcher.group(1), "DOCNO");
            headline = findMatch(matcher.group(1), "HEADLINE");
            text = findMatch(matcher.group(1), "TEXT");
            if (text == null && headline == null) {
                return null;
            } else {
                oneDocument = new Document(docNo, headline, text);
            }
        }
        return oneDocument;
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
                return (FILTER_REGEX.matcher(matcher.group(1)).replaceAll(" ")).replaceAll("\\d+", "");
            else
                return matcher.group(1);
        }
        return null;
    }

    private boolean isTextField(String tag) {
        return tag.equalsIgnoreCase("HEADLINE") || tag.equalsIgnoreCase("TEXT");
    }
}
