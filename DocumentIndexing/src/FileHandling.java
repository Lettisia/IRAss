import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandling {
	private HashMap<Integer, String> documentIDMap;
	private HashMap<String, IndexEntry> lexicon;

	public FileHandling(HashMap<Integer, String> documentIDMap, HashMap<String, IndexEntry> lexicon) throws IOException{
		this.documentIDMap = documentIDMap;	
		this.lexicon = lexicon;
		writeFile();
		readFile();
	}
	
	private void writeFile() throws IOException {
		
		System.out.println("Writing data to the random access file!");
		//DocumentIDMap
		RandomAccessFile randomFile = new RandomAccessFile("DocumentIDMap", "rw");
		for (Integer key : documentIDMap.keySet()){
			randomFile.writeInt(key);
			randomFile.writeBytes(documentIDMap.get(key));
		}
		randomFile.close();
		//InvertedIndex //Lexicon
		RandomAccessFile indexFile = new RandomAccessFile("InvertedIndex", "rw");
		RandomAccessFile lexiconFile = new RandomAccessFile("lexicon", "rw");
		int byteOffset = 0;

		for (String key : lexicon.keySet()){
			System.out.println(lexicon.get(key));
			ArrayList<TermFrequencyPair> invertedList = lexicon.get(key).getInvertedList();

			for(TermFrequencyPair termFreqPair : invertedList){
				indexFile.seek(byteOffset);
				byteOffset += termFreqPair.getDocID().byteValue() + termFreqPair.getTermFrequency().byteValue();
				indexFile.writeInt(termFreqPair.getDocID());
				indexFile.writeInt(termFreqPair.getTermFrequency());
				System.out.println(byteOffset+":"+termFreqPair.getDocID()+":"+termFreqPair.getTermFrequency());
			}

			lexicon.get(key).setByteOffset(byteOffset);
			lexiconFile.writeBytes(key);
			lexiconFile.writeInt(byteOffset);
			System.out.println(key+":"+byteOffset);
		}

		indexFile.close();
		lexiconFile.close();
		System.out.println("***Done writing to random access file!");
	}

	private void readFile() throws IOException {
		final int BYTE_SIZE = 4;  
		long byteNum;
		int num;
		
		final int STRING_BYTE_SIZE = 2;  
		char term;

		RandomAccessFile indexFile = new RandomAccessFile("InvertedIndex", "rw");
		RandomAccessFile lexiconFile = new RandomAccessFile("lexicon", "rw");
		
		byteNum = 0;
		indexFile.seek(byteNum);
		term = lexiconFile.readChar();
		System.out.println("string: " + term);
		
		byteNum = BYTE_SIZE;
		indexFile.seek(byteNum);
		num = lexiconFile.readInt();
		System.out.println("first int:" + num);
		
		byteNum = 0;
		indexFile.seek(byteNum);
		num = indexFile.readInt();
		System.out.println("second int:" + num);

		byteNum = BYTE_SIZE;
		indexFile.seek(byteNum);
		num = indexFile.readInt();
		System.out.println("third int:" + num);
		
		indexFile.close();
		System.out.println("   ***Done with reading from a random access binary file.");
	}
}
