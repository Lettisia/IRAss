import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandling {
	private HashMap<Long, String> documentIDMap = new HashMap<>();
	private HashMap<String, IndexEntry> lexicon = new HashMap<>();

	public FileHandling(HashMap<Long, String> documentIDMap, HashMap<String, IndexEntry> lexicon) throws IOException{
		this.documentIDMap = documentIDMap;	
		this.lexicon = lexicon;
		writeFile();
		readFile();
	}
	
	private void writeFile() throws IOException {
		
		System.out.println("Writing data to the random access file!");
		//DocumentIDMap
		RandomAccessFile randomFile = new RandomAccessFile("DocumentIDMap", "rw");
		for (Long key : documentIDMap.keySet()){
			randomFile.writeLong(key);
			randomFile.writeBytes(documentIDMap.get(key));
		}
		randomFile.close();
		//InvertedIndex //Lexicon
		randomFile = new RandomAccessFile("InvertedIndex", "rw");
		RandomAccessFile randomLexiconFile = new RandomAccessFile("lexicon", "rw");
		int byteOffset = 0;
		for (String key : lexicon.keySet()){
			ArrayList<TermFrequencyPair> invertedList = lexicon.get(key).getInvertedList();	
			for(TermFrequencyPair termFreqPair : invertedList){
				randomFile.seek(byteOffset);
				byteOffset += termFreqPair.getDocID().byteValue() + termFreqPair.getTermFrequency().byteValue();
				randomFile.writeInt(termFreqPair.getDocID());
				randomFile.writeInt(termFreqPair.getTermFrequency());
				System.out.println(byteOffset+":"+termFreqPair.getDocID()+":"+termFreqPair.getTermFrequency());
			}
			lexicon.get(key).setByteOffset(byteOffset);
			randomLexiconFile.writeBytes(key);
			randomLexiconFile.writeLong(byteOffset);
			System.out.println(key+":"+byteOffset);
		}
		randomFile.close();
		randomLexiconFile.close();
		System.out.println("***Done writing to random access file!");
	}

	private void readFile() throws IOException {
		final int BYTE_SIZE = 4;  
		long byteNum;
		int num;
		
		final int STRING_BYTE_SIZE = 2;  
		char term;

		RandomAccessFile randomFile = new RandomAccessFile("InvertedIndex", "r");
		RandomAccessFile randomLexiconFile = new RandomAccessFile("lexicon", "r");
		
		byteNum = STRING_BYTE_SIZE * 0;
		randomFile.seek(byteNum);
		term = randomLexiconFile.readChar();
		System.out.println(term);
		
		byteNum = BYTE_SIZE * 1;
		randomFile.seek(byteNum);
		num = randomLexiconFile.readInt();
		System.out.println(num);
		
		byteNum = BYTE_SIZE * 0;
		randomFile.seek(byteNum);
		num = randomFile.readInt();
		System.out.println(num);

		byteNum = BYTE_SIZE * 1;
		randomFile.seek(byteNum);
		num = randomFile.readInt();
		System.out.println(num);
		
		randomFile.close();
		System.out.println("   ***Done with reading from a random access binary file.");
	}
}
