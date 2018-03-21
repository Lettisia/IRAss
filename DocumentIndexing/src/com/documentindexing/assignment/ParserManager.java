package com.documentindexing.assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserManager {

	private static final Pattern DOC_TAG_REGEX = Pattern.compile("<doc>(.+?)</doc>");
	private static final Pattern DOCNO_TAG_REGEX = Pattern.compile("<docno>(.+?)</docno>");
	private static final Pattern HEADLINE_TAG_REGEX = Pattern.compile("<headline>(.+?)</headline>");
	private static final Pattern TEXT_TAG_REGEX = Pattern.compile("<text>(.+?)</text>");
	private static final Pattern FILTER_REGEX = Pattern.compile("<p>|</p>|[\\p{Punct}]");

	private Map<Integer, String> hmDoc = new HashMap<>();
	private List<Article> articles = new ArrayList<>();

	public ParserManager(String source) throws IOException{
		loadAllValues(readingFile(source));
		printList();
		printMap();
	}

	private String readingFile(String source) throws IOException{
		String document = "";
		BufferedReader inputFile = new BufferedReader(new InputStreamReader(new FileInputStream( new File(source))));
		String aLine = null;
		while ((aLine = inputFile.readLine()) != null) {
			document += aLine;	
		}
		inputFile.close();	

		return document.toLowerCase();
	}

	private void loadAllValues(String document){
		Article article =null;
		int articleCount =0;

		String docNo = null;
		String headline = null;
		String text = null;
		Matcher matcher = DOC_TAG_REGEX.matcher(document);

		while (matcher.find()) {
			articleCount++;
			docNo = findMatch(matcher.group(1),  "DOCNO");
			headline = findMatch(matcher.group(1),  "HEADLINE");
			text = findMatch(matcher.group(1),  "TEXT");
			article = new Article(articleCount, docNo, headline, text);
			hmDoc.put(articleCount, docNo);
			articles.add(article);
		}
	}

	private String findMatch(String str, String tag){
		Matcher matcher = null;
		switch(tag){
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
			//throw new IllegalArgumentException
		}

		if(matcher.find()){
			if(tag.equalsIgnoreCase("HEADLINE") || tag.equalsIgnoreCase("TEXT"))
				return FILTER_REGEX.matcher(matcher.group(1)).replaceAll("");
			else
				return matcher.group(1);
		}
		return null;	
	}

	private void printList(){
		for(Article article : articles){
			System.out.println(article.toString());
		}
	}

	private void printMap(){
		for(Entry<Integer, String> entry : hmDoc.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}

}
