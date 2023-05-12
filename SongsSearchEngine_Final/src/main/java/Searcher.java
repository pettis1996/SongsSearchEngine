import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Searcher {

    IndexSearcher indexSearcher;

    Query query;
    QueryParser queryParser;        // simple query
    QueryParser queryParserArtist;  // artist name
    QueryParser queryParserSong;    // song name
    QueryParser queryParserText;    // lyrics

    List<Document> documents = new ArrayList<>();
    StandardAnalyzer stdAnalyzer = new StandardAnalyzer();
    File documentFile;

    public Searcher(String indexDirPath) throws IOException, ParseException {
        documentFile = new File(indexDirPath);
        Directory directory = FSDirectory.open(documentFile.toPath());
        DirectoryReader reader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(reader);

        queryParser = new QueryParser("text", stdAnalyzer);
        queryParserArtist = new QueryParser("artist", stdAnalyzer);
        queryParserSong = new QueryParser("song", stdAnalyzer);
        queryParserText = new QueryParser("text", stdAnalyzer);
    }

    public List<Document> search(String queryInput, String fieldInput) throws ParseException, IOException {

        System.out.println("Searching for: " + queryInput);
        System.out.println("Searching in field: " + fieldInput);

        if(fieldInput.equals("")){
            try {
                query = queryParser.parse(queryInput);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(fieldInput.equals("artist")){
            try {
                query = queryParserArtist.parse(queryInput);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(fieldInput.equals("song")) {
            try {
                query = queryParserSong.parse(queryInput);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(fieldInput.equals("lyrics")) {
            try {
                query = queryParserText.parse(queryInput);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ScoreDoc[] hits = indexSearcher.search(query, 1000).scoreDocs;

        for(int i = 0; i < hits.length; i++){
            Document hitDoc = indexSearcher.doc(hits[i].doc);
            documents.add(hitDoc);
        }

        return documents;
    }

    // deprecated
    public List<Document> phraseSearch(String firstQueryInput, String secondQueryInput) throws IOException {
        query = new PhraseQuery(1, "text", new BytesRef(firstQueryInput), new BytesRef(secondQueryInput));

        TopDocs docs = indexSearcher.search(query, 10);
        documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            System.out.println(scoreDoc.toString());
            documents.add(indexSearcher.doc(scoreDoc.doc));
        }

        return documents;
    }

}
