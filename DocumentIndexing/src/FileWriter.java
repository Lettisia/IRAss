import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class FileWriter {

    private HashMap<Integer, String> documentIDMap;
    private HashMap<String, IndexEntry> lexicon;


    public FileWriter(HashMap<Integer, String> documentIDMap, HashMap<String, IndexEntry> lexicon) {
        this.documentIDMap = documentIDMap;
        this.lexicon = lexicon;
    }


    public void writeMapFile() {
        try (RandomAccessFile mapFile = new RandomAccessFile("map", "rw");) {
            for (Integer key : documentIDMap.keySet()) {
                mapFile.writeInt(key);
                mapFile.writeUTF(documentIDMap.get(key));
            }
        } catch (IOException e) {
            System.err.println("Problem with writing to map file");
            e.printStackTrace();
        }
    }


    public void writeIndexFiles() {
        System.out.println("Writing data to the random access file!");

        try (RandomAccessFile indexFile = new RandomAccessFile("invlists", "rw")) {
            try (RandomAccessFile randomLexiconFile = new RandomAccessFile("lexicon", "rw")) {
                long byteOffset = 0;
                for (String key : lexicon.keySet()) {
                    ArrayList<TermFrequencyPair> invertedList = lexicon.get(key).getInvertedList();
                    lexicon.get(key).setByteOffset(byteOffset);
                    randomLexiconFile.writeUTF(key);
                    randomLexiconFile.writeInt(lexicon.get(key).getDocumentFrequency());
                    randomLexiconFile.writeLong(byteOffset);
                    //System.out.println("Term:"+key+"||Doc Freq:"+lexicon.get(key).getDocumentFrequency()+"||byte offset:"+byteOffset);
                    for (TermFrequencyPair termFreqPair : invertedList) {
                        //System.out.println(byteOffset+":"+termFreqPair.getDocID()+":"+termFreqPair.getTermFrequency());
                        indexFile.seek(byteOffset);
                        indexFile.writeInt(termFreqPair.getDocID());
                        indexFile.writeInt(termFreqPair.getTermFrequency());
                        byteOffset = indexFile.getFilePointer();
                    }
                }
            } catch (IOException e) {
                System.err.println("Problem with writing to lexicon file");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Problem with writing to invlists file");
            e.printStackTrace();
        }
        System.out.println("***Done writing to random access file!");
    }

}
