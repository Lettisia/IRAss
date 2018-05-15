# Information Retrieval

To implement an inverted index and use it to store term occurrence information. This project reads data from disk, filters it using some predefined rules, tokenizes the data, removes stopwords from a predefine list of words, writes the processed data into appropriate files on disk and finally able to retrive data from disk. 

## Contributors

- s3671532: Lettisia George
- s3640623: Rochelle Gracias

## Getting Started

### Prerequisites to run project

Have a Java Runtime environment installed on your computer.
- Download and Install Java jdk v1.8
- Set the environment variables PATH

### How to compile and run the program?

- Extract all files into a single directory.
- Use the command ```javac *.java``` to compile all the java files.
- Run the indexing program with ```java Index [-p] [-s <stoplist>] <sourcefile>``` where:
  - ```-p``` is an optional parameter which causes all the terms in each article to be printed in the order they appeared.
  - ```-s``` is also an optional parameter that, with a valid ```<stoplist>``` file as the next argument in the list, will remove the terms that are listed in the ```<stoplist>``` from the document.
- For example use ```java Index -p -s /home/inforet/a1/stoplist /home/inforet/a1/latimes``` to run the code with stopword removal and terms printed.
  - Note: Printing terms slows down the process considerably and is unreadable when processing the entire latimes collection.
- Run a search with ```java Search <lexicon> <invlists> <map> [-s <stoplist>] <queryterm 1> [... <queryterm N>]``` where:
  - ```<lexicon>```, ```<invlists>``` and  ```<map>``` are the file name of a lexicon, inverted list and map file generated with our Index program.
  - ```-s``` is also an optional parameter that, with a valid ```<stoplist>``` file as the next argument in the list, will remove the terms that are listed in the ```<stoplist>``` from the query.
  - ```<queryterm 1> [... <queryterm N>]``` are any number of query terms which will be searched for separately.
- For example use ```java Search lexicon invlists map nuclear``` to search for all documents containing the term "nuclear".
- use ```java Search lexicon invlists map -s /home/inforet/a1/stoplist where is paris``` to search for "paris" but not "where" or "is".
- Updated search command ```java Search -BM25 -q 401 -n 101 -l lexicon -i invlists -m map -s /home/inforet/a1/stoplist  ancient city ruins```

## Built With

* Java jdk v1.8

