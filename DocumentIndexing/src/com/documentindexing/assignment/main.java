package com.documentindexing.assignment;

import java.io.File;
import java.io.IOException;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dir = new File("src/resources");
		String source;
		try {
			source = dir.getCanonicalPath() + File.separator + "latimes";
			new ParserManager(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
