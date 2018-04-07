import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

class FileWriter {
    private static final String MODE_READ_WRITE = "rw";
    private final HashMap<Integer, String> documentIDMap;
    private final HashMap<String, IndexEntry> lexicon;

    FileWriter(HashMap<Integer, String> documentIDMap, HashMap<String, IndexEntry> lexicon) {
        this.documentIDMap = documentIDMap;
        this.lexicon = lexicon;
    }

    void writeMapFile() {
        try (RandomAccessFile mapFile = new RandomAccessFile("map", MODE_READ_WRITE)) {
            for (Integer key : documentIDMap.keySet()) {
                mapFile.writeInt(key);
                mapFile.writeUTF(documentIDMap.get(key));
            }
        } catch (IOException e) {
            System.err.println("Problem with writing to map file!\n");
        }
    }

    void writeIndexFiles() {
        try (RandomAccessFile indexFile = new RandomAccessFile("invlists", MODE_READ_WRITE)) {
            try (RandomAccessFile randomLexiconFile = new RandomAccessFile("lexicon", MODE_READ_WRITE)) {
                long byteOffset = 0;
                for (String key : lexicon.keySet()) {
                    if (!key.equals("")) {
                        ArrayList<TermFrequencyPair> invertedList = lexicon.get(key).getInvertedList();
                        lexicon.get(key).setByteOffset(byteOffset);
                        randomLexiconFile.writeUTF(key);
                        randomLexiconFile.writeInt(lexicon.get(key).getDocumentFrequency());
                        randomLexiconFile.writeLong(byteOffset);
                        for (TermFrequencyPair termFreqPair : invertedList) {
                            indexFile.seek(byteOffset);
                            indexFile.writeInt(termFreqPair.getDocID());
                            indexFile.writeInt(termFreqPair.getTermFrequency());
                            byteOffset = indexFile.getFilePointer();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Problem with writing to lexicon file!\n");
            }
        } catch (IOException e) {
            System.err.println("Problem with writing to invlists file!\n");
        }
    }
}
