import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Indexer {

    private IndexWriter writer;
    private IndexWriterConfig indexConfig;
    Analyzer stdAnalyzer = new StandardAnalyzer();

    private File indexDirectory;
    private File documentsDirectory;

    public Indexer(String indexDirectoryPath, String documentsDirectoryPath) {
        this.indexDirectory = new File(indexDirectoryPath);
        this.documentsDirectory = new File(documentsDirectoryPath);
    }

    public void createIndex() throws IOException {

        Directory dir = FSDirectory.open(indexDirectory.toPath().toAbsolutePath());
        System.out.println(indexDirectory.toPath());

        indexConfig = new IndexWriterConfig(stdAnalyzer);

        writer = new IndexWriter(dir, indexConfig);

        File[] song_files = this.documentsDirectory.listFiles();

        ArrayList<File> files = new ArrayList<>();

        for (File f : song_files)
            if (f.getName().toLowerCase().endsWith(".txt")) {
                files.add(f);
            }

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);

            Document doc = new Document();

            // for the lyrics
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));

            lines.remove(0);
            lines.remove(0);
            lines.remove(0);

            String lyrics = String.join(System.lineSeparator(), lines);
            // for the lyrics end

            Scanner scanner = new Scanner(file, UTF_8);

            Field id = new TextField("id", scanner.nextLine(), Field.Store.YES);
            Field artist = new TextField("artist", scanner.nextLine(), Field.Store.YES);
            Field song = new TextField("song", scanner.nextLine(), Field.Store.YES);
            Field text = new TextField("text", lyrics, Field.Store.YES);

            doc.add(id);
            doc.add(artist);
            doc.add(song);
            doc.add(text);
            scanner.close();

            System.out.println(doc.getField("id").toString());
            System.out.println(lines.get(0));

            writer.addDocument(doc);

        }

        writer.close();
        dir.close();
    }
}
