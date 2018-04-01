import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class SearchEngine {
    private static final String READ_MODE = "r";
    private static final boolean VERBOSE = true;

    private HashMap<String, IndexEntry> lexicon = new HashMap<>();
    private HashMap<Integer, String> documentIDMap = new HashMap<>();
    private String indexFilename;


    public SearchEngine(String lexiconFilename, String indexFilename, String mapFilename) {
        readMapFile(mapFilename);
        readLexiconFile(lexiconFilename);
        this.indexFilename = indexFilename;
    }

    private void readMapFile(String mapFilename) {
        try (RandomAccessFile mapFile = new RandomAccessFile(mapFilename, READ_MODE)) {
            long endOfFile = mapFile.length();

            while (mapFile.getFilePointer() < endOfFile) {
                int docIndex = mapFile.readInt();
                String docNo = mapFile.readUTF();
                documentIDMap.put(docIndex, docNo);
            }
        } catch (EOFException e) {
            System.err.println("Naughty! You read past end of map file");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Problem with reading from map file");
            e.printStackTrace();
        }
    }


    private void readLexiconFile(String lexiconFilename) {
        try (RandomAccessFile lexiconFile = new RandomAccessFile(lexiconFilename, READ_MODE)) {
            long endOfFile = lexiconFile.length();
            long startOfFile = 0L;

            lexiconFile.seek(startOfFile);

            while (lexiconFile.getFilePointer() < endOfFile) {
                IndexEntry entry = new IndexEntry();
                String term = lexiconFile.readUTF();
                int docFrequency = lexiconFile.readInt();
                long byteOffset = lexiconFile.readLong();
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
        } catch (EOFException e) {
            System.err.println("Naughty! You read past end of lexicon file");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Problem with reading from lexicon file");
            e.printStackTrace();
        }
    }

    public String search(String query) {
        if (lexicon.containsKey(query)) {
            StringBuilder queryResult = new StringBuilder();
            queryResult.append(query).append("\n");

            IndexEntry entry = lexicon.get(query);

            int docFrequency = entry.getDocumentFrequency();
            queryResult.append(docFrequency).append("\n");

            try (RandomAccessFile indexFile = new RandomAccessFile(indexFilename, READ_MODE)) {
                indexFile.seek(entry.getByteOffset());

                for (int i = 0; i < docFrequency; i++) {
                    int docID = indexFile.readInt();
                    int termFrequency = indexFile.readInt();
                    String docNo = documentIDMap.get(docID);

                    if (VERBOSE) {
                        System.out.print("DocID: " + docID);
                        System.out.print("||DocNum:" + docNo);
                        System.out.print("||termFrequency:" + termFrequency);
                        System.out.println();
                    }

                    queryResult.append(docNo).append(" ").append(termFrequency).append("/n");
                }

            } catch (IOException e) {
                System.err.println("Problem with reading from index file");
                e.printStackTrace();
            }
            return queryResult.toString();
        } else {
            return "Ha ha, not found!";
        }
    }
}
