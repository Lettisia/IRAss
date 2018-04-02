import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

class SearchEngine {
    private static final String READ_MODE = "r";
    private static final boolean VERBOSE = false;

    private final HashMap<String, IndexEntry> lexicon = new HashMap<>();
    private final HashMap<Integer, String> documentIDMap = new HashMap<>();
    private final String indexFilename;


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
			System.err.println("You read the map file!");
		} catch (IOException e) {
			System.err.println("Oops! Problem with reading from map file!");
			e.printStackTrace();
		}
	}

	private void readLexiconFile(String lexiconFilename) {
		try (RandomAccessFile lexiconFile = new RandomAccessFile(lexiconFilename, READ_MODE)) {
			long endOfFile = lexiconFile.length();
			long startOfFile = 0L;

			lexiconFile.seek(startOfFile);

			while (lexiconFile.getFilePointer() <= endOfFile) {
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
			System.err.println("You read the lexicon file!\n");
		} catch (IOException e) {
			System.err.println("Problem with reading from lexicon file!\n");
			e.printStackTrace();
		}
	}

	public void processQueryTerms(String query){
        boolean removeStopwords = !isTheInLexicon();
		QueryProcessing queryProcessor = new QueryProcessing(query, removeStopwords);
		ArrayList<String> queryTerm = queryProcessor.getQueryTerms();
        for (String aQueryTerm : queryTerm) {
            System.out.println(search(aQueryTerm));
        }
    }

    private boolean isTheInLexicon() {
        return lexicon.containsKey("the");
	}

	public String search(String query) {
		if (!query.equals("") && lexicon.containsKey(query)) {
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
						System.out.print("||DocNo:" + docNo);
						System.out.print("||termFrequency:" + termFrequency);
						System.out.println();
					}

					queryResult.append(docNo).append(" ").append(termFrequency).append("\n");
				}

			} catch (IOException e) {
				System.err.println("Problem with reading from index file!\n");
				e.printStackTrace();
			}
			return queryResult.toString();
		} else {
			return "\nSearch term:"+ query +" not found!";
		}
	}
}
