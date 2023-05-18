import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DocValuesType;
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

    Analyzer stdAnalyzer = new StandardAnalyzer();

    private final File indexDirectory;
    private final File documentsDirectory;

    public Indexer(String indexDirectoryPath, String documentsDirectoryPath) {
        this.indexDirectory = new File(indexDirectoryPath);
        this.documentsDirectory = new File(documentsDirectoryPath);
    }

    public void createIndex() throws IOException {

        Directory dir = FSDirectory.open(indexDirectory.toPath().toAbsolutePath());
        System.out.println(indexDirectory.toPath());

        IndexWriterConfig indexConfig = new IndexWriterConfig(stdAnalyzer);

        IndexWriter writer = new IndexWriter(dir, indexConfig);

        File[] song_files = this.documentsDirectory.listFiles();

        ArrayList<File> files = new ArrayList<>();

        for (File f : song_files)
            if (f.getName().toLowerCase().endsWith(".txt")) {
                files.add(f);
            }

        for (File file : files) {
            Document doc = new Document();

            // for the full file content used in general search
            List<String> file_content = Files.readAllLines(Paths.get(file.getPath()));

            String content = String.join(System.lineSeparator(), file_content);

            // for the lyrics
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));

            lines.remove(0);
            lines.remove(0);
            lines.remove(0);

            String lyrics = String.join(System.lineSeparator(), lines);
            // for the lyrics end

            Scanner scanner = new Scanner(file, UTF_8);

            Field idField = new TextField("id", scanner.nextLine(), Field.Store.YES);
            Field artistField = new TextField("artist", scanner.nextLine(), Field.Store.YES);
            Field songField = new TextField("song", scanner.nextLine(), Field.Store.YES);
            Field textField = new TextField("text", lyrics, Field.Store.YES);
            Field contentField = new TextField("file_content", content, Field.Store.YES);

            doc.add(idField);
            doc.add(artistField);
            doc.add(songField);
            doc.add(textField);
            doc.add(contentField);
            scanner.close();

            System.out.println(doc.getField("id").toString());

            writer.addDocument(doc);

        }

        writer.close();
        dir.close();
    }
}
