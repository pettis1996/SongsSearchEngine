import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import javax.swing.*;
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

        queryParser = new QueryParser("file_content", stdAnalyzer);
        queryParserArtist = new QueryParser("artist", stdAnalyzer);
        queryParserSong = new QueryParser("song", stdAnalyzer);
        queryParserText = new QueryParser("text", stdAnalyzer);
    }

    public List<Document> search(String queryInput, String fieldInput) throws ParseException, IOException {

        System.out.println("Searching for: " + queryInput);
        System.out.println("Searching in field: " + fieldInput);

        switch (fieldInput) {
            case "":
                try {
                    query = queryParser.parse(queryInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "artist":
                try {
                    query = queryParserArtist.parse(queryInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "song":
                try {
                    query = queryParserSong.parse(queryInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "lyrics":
                try {
                    query = queryParserText.parse(queryInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Field not found. \n" +
                        "Try typing one of the available options listed below. \n" +
                        "Available Option Fields:\n" +
                        "[artist, song, lyrics]");
                break;
        }

        // TODO: fix this
//        SortField sortField
//                = new SortField("song", SortField.Type.STRING_VAL, false);
//        Sort sortByLyrics = new Sort(sortField);

        ScoreDoc[] search_hits = indexSearcher.search(query, 1000).scoreDocs;

        for(int i = 0; i < search_hits.length; i++){
            Document hitDoc = indexSearcher.doc(search_hits[i].doc);
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
