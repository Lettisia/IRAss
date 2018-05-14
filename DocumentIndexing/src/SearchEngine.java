import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

class SearchEngine {
    private static final String READ_MODE = "r";
    private static final boolean VERBOSE = false;
    
    private final String invlistFile;
    private final String lexiconFile;
    private final String mapFile;
    private String stoplistFile;

    private final HashMap<String, IndexEntry> lexicon = new HashMap<>();
    private final HashMap<Integer, Document> documentIDMap = new HashMap<>();
    
    QueryProcessing queryProcessor = null;

    SearchEngine(String lexiconFile, String invlistFile, String mapFile) {
        this.mapFile = mapFile;
        this.invlistFile = invlistFile;
        this.lexiconFile = lexiconFile;
        loadDataFromFile();
    }
    
    private void loadDataFromFile(){
        readMapFile();
        readLexiconFile();
    }
    
    public void search(Integer queryLabel,Integer numResults, String stoplist, String query) {
        this.stoplistFile = stoplist;
    	queryProcessor = new QueryProcessing(queryLabel, query, numResults, lexicon, documentIDMap, invlistFile, stoplistFile);
    	queryProcessor.displayResults();
    }

    private void readMapFile() {
        try (RandomAccessFile mapFileName = new RandomAccessFile(mapFile, READ_MODE)) {
            long endOfFile = mapFileName.length();
            
    		Document.setAvgDocumentLength(mapFileName.readDouble());
    		Document.setNumberOfDocuments(mapFileName.readInt());
    		
            if (VERBOSE) {
            	System.out.println("Number of Documents: " + Document.getNumberOfDocuments());
            	System.out.println("Average Document Length: " + Document.getAvgDocumentLength());
            }
            while (mapFileName.getFilePointer() <= endOfFile) {
                int docIndex = mapFileName.readInt();
                String docNo = mapFileName.readUTF();
                int documentLength = mapFileName.readInt();
                Document document = new Document(docIndex, docNo, documentLength);
                double kValue = document.calculateKValue();
                documentIDMap.put(docIndex, document);
                if (VERBOSE) {
                    System.out.print("DocIndex: " + docIndex);
                    System.out.print("||DocNo:" + docNo);
                    System.out.print("||DocLength:" + documentLength);
                    System.out.print("||kValue:" + kValue);
                    System.out.println();
                }
            }
        }catch (EOFException e) {
            System.err.println("You read the map file!");
        }catch (IOException e) {
            System.err.println("Oops! Problem with reading from map file!");
        }
    }

    private void readLexiconFile() {
        try (RandomAccessFile lexiconFileName = new RandomAccessFile(lexiconFile, READ_MODE)) {
            long endOfFile = lexiconFileName.length();
            long startOfFile = 0L;

            lexiconFileName.seek(startOfFile);

            while (lexiconFileName.getFilePointer() <= endOfFile) {
                IndexEntry entry = new IndexEntry();
                String term = lexiconFileName.readUTF();
                int docFrequency = lexiconFileName.readInt();
                long byteOffset = lexiconFileName.readLong();
                entry.setTerm(term);
                entry.setDocumentFrequency(docFrequency);
                entry.setByteOffset(byteOffset);

                if (VERBOSE) {
                    System.out.print("term: " + term);
                    System.out.print("||docFrequency:" + docFrequency);
                    System.out.print("||byteOffset:" + byteOffset);
                    System.out.println();
                }

                lexicon.put(term, entry);
            }
        }catch (EOFException e) {
            System.err.println("You read the lexicon file!");
        }catch (IOException e) {
            System.err.println("Problem with reading from lexicon file!");
        }
    }
}
