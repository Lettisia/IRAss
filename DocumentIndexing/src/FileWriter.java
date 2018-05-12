import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

class FileWriter {
	private static final String MODE_READ_WRITE = "rw";
	private final HashMap<String, IndexEntry> lexicon;
	private final ArrayList<Article> articles;
	private double avgDocLength;

	FileWriter(HashMap<String, IndexEntry> lexicon, ArrayList<Article> articles) {
		this.lexicon = lexicon;
		this.articles = articles;
		calculateAvgLength();
	}

	void writeMapFile() {
		try (RandomAccessFile mapFile = new RandomAccessFile("map", MODE_READ_WRITE)) {
			mapFile.writeInt(Article.getArticleCount());
			for(Article article : articles){
				if(article!=null){
					int docIndex = article.getDocumentIndex();
					String docNo = article.getDocNo();
					double kValue = calculateKValue(article);
					mapFile.writeInt(docIndex);
					mapFile.writeUTF(docNo);
					mapFile.writeDouble(kValue);
				}
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

	//Calculating Average Length
	void calculateAvgLength(){
		Integer totalDocLength=0;
		for(Article article : articles){
			if(article!=null)
				totalDocLength += article.getDocumentLength();
		}
		this.avgDocLength = totalDocLength/Article.getArticleCount();
	}

	//Calculating K-value
	double calculateKValue(Article article){
		double K = 0.0;
		double k1 = 1.2;
		double b = 0.75;
		Integer docLength = article.getDocumentLength();

		K = k1 * ((1 - b) + ((b * docLength))/avgDocLength);
		return K;
	}
}
