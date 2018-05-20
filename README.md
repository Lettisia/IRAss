# Information Ranked Retrieval

To implement a ranked querying system, run a set of sample queries, evaluate system performance, and design and implement appropriate data structures for efficient searching. This search system will produce short document summaries with each answer item returned in a search results list. Two summary creation approaches - graph based and query based summarisation.

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
- Run a search with ```java Search -BM25 -q <query-label> -n <num-results> -l <lexicon> -i <invlists> -m <map> [-s <stoplist>] [-gb <collectionfile>] [-qb <collectionfile>] <queryterm-1> [<queryterm-2> ...<queryterm-N>]``` where:
  - ```-BM25``` specifies the BM25 similarity function to be used for ranked retrieval.
  - ```<query-label>``` is an integer that identifies the query.
  - ```<num-results>``` is an integer that specifies the number of top-ranked documents that should be returned as an answer.
  - ```<lexicon>```, ```<invlists>``` and  ```<map>``` are the file name of a lexicon, inverted list and map file generated with our Index program.
  - ```-s``` is also an optional parameter that, with a valid ```<stoplist>``` file as the next argument in the list, will remove the terms that are listed in the ```<stoplist>``` from the query.
  - ```-gb``` is an optional parameter that, with a valid ```latimes``` file as the next argument in the list, will do a graph based summarisation.
  - ```-qb``` is an optional parameter that, with a valid ```latimes``` file as the next argument in the list, will do a query based summarisation.
  - *NOTE: Run either -gb or -qb to produce a short document summary.*
  - ```<queryterm 1> [... <queryterm N>]``` are any number of query terms which will be searched for separately.
- For example use ```java Search -BM25 -q 401 -n 1 -l lexicon -i invlists -m map -s /home/inforet/a1/stoplist -gb /home/inforet/a1/latimes ancient city ruins``` to perform a ranked retrieval for all documents containing "ancient city ruins" and do a graph based document summarisation using of the highest ranked document.
- use ```java Search -BM25 -q 401 -n 100 -l lexicon -i invlists -m map -s /home/inforet/a1/stoplist ancient city ruins``` to perform a ranked retrieval for all documents containing "ancient city ruins"

## Built With

* Java jdk v1.8

