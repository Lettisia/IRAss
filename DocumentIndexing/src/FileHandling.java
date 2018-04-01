import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandling {

	private HashMap<Integer, String> documentIDMap;
	private HashMap<String, IndexEntry> lexicon;
	private String query;

	public FileHandling(HashMap<Integer, String> documentIDMap, HashMap<String, IndexEntry> lexicon) throws IOException{
		this.documentIDMap = documentIDMap;
		this.lexicon = lexicon;
		writeFile();
	}

	public FileHandling(HashMap<Integer, String> documentIDMap, String query) throws IOException{
		this.documentIDMap = documentIDMap;
		this.query = query;
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

		randomFile = new RandomAccessFile("InvertedIndex", "rw");
		RandomAccessFile randomLexiconFile = new RandomAccessFile("lexicon", "rw");
		long byteOffset = 0;
		for (String key : lexicon.keySet()){
			ArrayList<TermFrequencyPair> invertedList = lexicon.get(key).getInvertedList();	
			lexicon.get(key).setByteOffset(byteOffset);
			randomLexiconFile.writeUTF(key);
			randomLexiconFile.writeInt(lexicon.get(key).getDocumentFrequency());
			randomLexiconFile.writeLong(byteOffset);
			//System.out.println("Term:"+key+"||Doc Freq:"+lexicon.get(key).getDocumentFrequency()+"||byte offset:"+byteOffset);
			for(TermFrequencyPair termFreqPair : invertedList){
				//System.out.println(byteOffset+":"+termFreqPair.getDocID()+":"+termFreqPair.getTermFrequency());
				randomFile.seek(byteOffset);
				randomFile.writeInt(termFreqPair.getDocID());
				randomFile.writeInt(termFreqPair.getTermFrequency());
				byteOffset = randomFile.getFilePointer();
			}
		}
		randomFile.close();
		randomLexiconFile.close();
		System.out.println("***Done writing to random access file!");
	}

	private void readFile() throws IOException {

		String term;
		long byteOffset;
		int docFrequency;
		int docID;
		int termFrequency;

		RandomAccessFile indexFile = new RandomAccessFile("InvertedIndex", "r");
		RandomAccessFile lexiconFile = new RandomAccessFile("lexicon", "r");

		lexiconFile.seek(0);

		try
		{
			while(true){
				term = lexiconFile.readUTF();
				docFrequency = lexiconFile.readInt();
				byteOffset = lexiconFile.readLong();
				if(term.equals(query)){
					System.out.print("term: " + term);
					System.out.print("||docFrequency:" + docFrequency);
					System.out.print("||byteOffset:" + byteOffset);
					System.out.println("");
					indexFile.seek(byteOffset);
					for(int i=0;i<docFrequency;i++){
						docID = indexFile.readInt();
						termFrequency = indexFile.readInt();
						for(Integer key : documentIDMap.keySet()){
							if(docID == key){
								System.out.print("DocID: " + docID);
								System.out.print("||DocNum:"+documentIDMap.get(key));
							}
						}
						System.out.print("||termFrequency:" + termFrequency);
						System.out.println("");
					}
					break;
				}
			}
		}
		catch (IOException ex)
		{
			System.out.println("End of file reached!");
		}
		finally
		{
			lexiconFile.close();
			indexFile.close();
			System.out.println("***Done with reading from a random access binary file.");
		}
	}
}
