import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class RunApplication {

    public static void main(String[] args) throws IOException, ParseException {
        /*
         *   TODO: make search history sort elements by number of time searched
         *    TODO: highlight query words in results
         *     TODO: previous and next page for results (10 per page) - ** DONE ** NEED TO PRESS SEARCH
         * */
        HashMap<String, Integer> searchHistory = new HashMap<>();

        JFrame appFrame = new JFrame("Songs Search Engine - Information Retrieval");
        JPanel appPanel = new JPanel();

        JOptionPane.showMessageDialog(appFrame,
                """
                        Welcome!\s
                        Search for songs by artist, song title, or lyrics\s
                        and find information about the song including the full lyrics.""",
                    "Welcome Message", JOptionPane.INFORMATION_MESSAGE);

        final String[] pathDirectory = {JOptionPane.showInputDialog(appFrame,
                "Input root folder directory path: ",
                "Input Root Directory",
                JOptionPane.WARNING_MESSAGE)};

        final String[] indexDirectory = {pathDirectory[0] + "\\index"};
        final String[] imageDirectory = {pathDirectory[0] + "\\src\\image\\icon.png"};
        final String[] songFilesDirectory = {pathDirectory[0] + "\\song_files"};

        ImageIcon imgicon = new ImageIcon(imageDirectory[0]);
        appFrame.setIconImage(imgicon.getImage());

        appPanel.setLayout(null); // for absolute component positions
        appPanel.setBackground(new Color(0x121212));

        // Variables
        JLabel querySearchLabel = new JLabel("Search a query:");
        JTextField querySearchField = new JTextField();

        JLabel fieldSearchLabel = new JLabel("Search in field:");
        JTextField fieldSearchField = new JTextField();

        JButton searchButton = new JButton("Search");

        final Searcher[] searcher = {new Searcher(indexDirectory[0])};

        JLabel addFilterLabel = new JLabel("Add a filter:");

        JTextField addFilterField = new JTextField();

        JButton addFilterButton = new JButton("Add Filter");

        DefaultListModel<String> results = new DefaultListModel<>();

        DefaultListModel<String> resultsDisplayed = new DefaultListModel<>();

        Vector<String> historyItems = new Vector<>();

        JButton showHistoryButton = new JButton("History");

        JList searchHistoryLinks = new JList(historyItems);

        JList resultsList = new JList(resultsDisplayed);

        final int[] resultsShown = {1};

        JButton clearHistoryButton = new JButton("Clear History");

        JButton nextHistoryPage = new JButton(">");
        JButton previousHistoryPage = new JButton("<");

        JButton previousPage = new JButton("< Previous 10");
        JButton nextPage = new JButton("Next 10 >");
        // Variables End

        // Menu
        JMenuBar menuBar = new JMenuBar();

        JMenu indexMenu = new JMenu("Index");
        JMenuItem createIndexMenuItem = new JMenuItem("Create Index");

        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem setDirectoryPathMenuItem = new JMenuItem("Set Directory Path");

        JMenu aboutMenu = new JMenu("About Us");
        JMenuItem aboutUsMenuItem = new JMenuItem("Team Info");
        JMenuItem aboutProjectMenuItem = new JMenuItem("Project Readme");

        createIndexMenuItem.addActionListener((e) -> {
            Indexer indexer = new Indexer(indexDirectory[0],
                    songFilesDirectory[0]);
            try {
                indexer.createIndex();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        setDirectoryPathMenuItem.addActionListener(e -> {
            pathDirectory[0] = JOptionPane.showInputDialog(appFrame, "Input root folder directory path: ", "Input Root Directory", JOptionPane.WARNING_MESSAGE);
            indexDirectory[0] = pathDirectory[0] + "\\index";
            imageDirectory[0] = pathDirectory[0] + "\\src\\image\\icon.png";
            songFilesDirectory[0] = pathDirectory[0] + "\\song_files";
        });

        aboutUsMenuItem.addActionListener((e) ->
                JOptionPane.showMessageDialog(appFrame,
                        "TEAM MEMBERS\n\n" +
                                "[AM:3136]\nPETTIS PARASKEVAS-CHRISTOS\n\n\n" +
                                "[AM:3150]\nVAFEIAS DIMITRIOS\n\n\n",
                        "About Us",
                        JOptionPane.PLAIN_MESSAGE));

        menuBar.add(indexMenu);
        menuBar.add(settingsMenu);
        menuBar.add(aboutMenu);

        aboutMenu.add(aboutUsMenuItem);
        aboutMenu.add(aboutProjectMenuItem);

        settingsMenu.add(setDirectoryPathMenuItem);

        indexMenu.add(createIndexMenuItem);
        // Menu End

        // GUI Header
        querySearchLabel.setBounds(40, 0, 400, 30);
        querySearchLabel.setOpaque(false);
        querySearchLabel.setForeground(Color.white);

        querySearchField.setBounds(40, 30, 400, 30);

        fieldSearchLabel.setBounds(470, 0, 200, 30);
        fieldSearchLabel.setOpaque(false);
        fieldSearchLabel.setForeground(Color.white);

        fieldSearchField.setBounds(470, 30, 200, 30);

        searchButton.setBounds(690, 30, 100, 30);
        searchButton.addActionListener(e -> {
            List<Document> resultDocument = new ArrayList<>();
            try {
                results.clear();
                resultsDisplayed.clear();
                searcher[0] = new Searcher(indexDirectory[0]);
                resultDocument = searcher[0].search(querySearchField.getText(), fieldSearchField.getText());
            } catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }

            if(searchHistory.containsKey(querySearchField.getText()))
            {
                searchHistory.replace(querySearchField.getText(), searchHistory.get(querySearchField.getText()) + 1);
            } else {
                searchHistory.put(querySearchField.getText(), 1);
            }

            for(Map.Entry<String, Integer> entry : searchHistory.entrySet()) {
                if(historyItems.size() != 0 && !historyItems.contains(entry.getKey() + " : " + (entry.getValue()))){
                    historyItems.remove(entry.getKey() + " : " + (entry.getValue()-1));
                    historyItems.add(entry.getKey() + " : " + entry.getValue());
                } else if(historyItems.size() == 0){
                    historyItems.add(entry.getKey() + " : " + entry.getValue());
                }
            }

            for (Document doc : resultDocument){
                results.add(0, "ID: " + doc.getField("id").stringValue()
                        + "| Artist: " + doc.getField("artist").stringValue()
                        + "| Song: " + doc.getField("song").stringValue()
                        + "| Lyrics: " + doc.getField("text").stringValue());
            }

            for (int i = resultsShown[0]; i < resultsShown[0] + 10; i++){
                resultsDisplayed.add(0, "ID: " + resultDocument.get(i).getField("id").stringValue()
                        + "| Artist: " + resultDocument.get(i).getField("artist").stringValue()
                        + "| Song: " + resultDocument.get(i).getField("song").stringValue()
                        + "| Lyrics: " + resultDocument.get(i).getField("text").stringValue());
            }

            resultsList.clearSelection();
            resultsList.setVisible(true);
        });

        addFilterLabel.setBounds(40, 70, 400, 30);
        addFilterLabel.setOpaque(false);
        addFilterLabel.setForeground(Color.white);

        addFilterField.setBounds(40, 100, 400, 30);
        addFilterButton.setBounds(470, 100, 200, 30);
        addFilterButton.addActionListener((e) -> {
            // add filter
            for(String entry : searchHistory.keySet()){
                System.out.println(entry);
            }
        });

        showHistoryButton.setBounds(690, 100, 100, 30);
        showHistoryButton.addActionListener((e) -> {
            if(!searchHistoryLinks.isVisible()){
                showHistoryButton.setText("Hide");
                resultsList.setBounds(40, 220, 750, 270);
                searchHistoryLinks.setVisible(true);
                nextHistoryPage.setVisible(true);
                previousHistoryPage.setVisible(true);
                clearHistoryButton.setVisible(true);
            } else if(searchHistoryLinks.isVisible()){
                showHistoryButton.setText("History");
                resultsList.setBounds(40, 140, 750, 350);
                searchHistoryLinks.setVisible(false);
                nextHistoryPage.setVisible(false);
                previousHistoryPage.setVisible(false);
                clearHistoryButton.setVisible(false);
            }
        });

        searchHistoryLinks.setBounds(90, 140, 650, 30);
        searchHistoryLinks.setVisible(false);
        searchHistoryLinks.setVisibleRowCount(-1);
        searchHistoryLinks.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        searchHistoryLinks.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        searchHistoryLinks.setFixedCellHeight(30);
        searchHistoryLinks.setFixedCellWidth(50);
        searchHistoryLinks.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JList l = (JList)e.getSource();
                ListModel m = l.getModel();
                int index = l.locationToIndex(e.getPoint());
                if( index>-1 ) {
                    l.setToolTipText(m.getElementAt(index).toString());
                }
            }
        });

        clearHistoryButton.setBounds(350, 180, 150, 30);
        clearHistoryButton.setVisible(false);
        clearHistoryButton.addActionListener((e) -> {
            searchHistory.clear();
            historyItems.clear();
        });

        previousHistoryPage.setBounds(40, 140, 50, 30);
        previousHistoryPage.setVisible(false);
        previousHistoryPage.addActionListener((e) -> {

        });

        nextHistoryPage.setBounds(740, 140, 50, 30);
        nextHistoryPage.setVisible(false);
        nextHistoryPage.addActionListener((e) -> {

        });
        // GUI Header End

        // Body
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        resultsList.setLayoutOrientation(JList.VERTICAL);
        resultsList.setVisibleRowCount(-1);
        resultsList.setBounds(40, 140, 750, 350);
        resultsList.setFixedCellHeight(25);
        resultsList.addListSelectionListener(event -> {
            JList source = (JList)event.getSource();
            String selected = source.getSelectedValue().toString();
            if (!event.getValueIsAdjusting() && event.getSource() != null){
                JTextArea textArea = new JTextArea(selected, 25, 70);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                JOptionPane.showMessageDialog(null, scrollPane, "Song Information", JOptionPane.PLAIN_MESSAGE);
                source.clearSelection();
            }
        });
        resultsList.setVisible(false);

        previousPage.setBounds(40, 500, 150, 30);
        previousPage.addActionListener((e) -> {
            resultsDisplayed.clear();
            if (resultsShown[0] > 10){
                resultsShown[0] -= 10;
            }
        });
        nextPage.setBounds(640, 500, 150, 30);
        nextPage.addActionListener((e) -> {
            resultsDisplayed.clear();
            if (resultsShown[0] < results.size()){
                resultsShown[0] += 10;
            }
        });
        // Body end

        // adding components to panel
        appPanel.add(querySearchLabel);
        appPanel.add(querySearchField);
        appPanel.add(fieldSearchLabel);
        appPanel.add(fieldSearchField);
        appPanel.add(searchButton);
        appPanel.add(addFilterLabel);
        appPanel.add(addFilterField);
        appPanel.add(addFilterButton);
        appPanel.add(searchHistoryLinks);
        appPanel.add(showHistoryButton);
        appPanel.add(clearHistoryButton);
        appPanel.add(nextHistoryPage);
        appPanel.add(previousHistoryPage);
        appPanel.add(resultsList);
        appPanel.add(nextPage);
        appPanel.add(previousPage);

        // main app frame settings
        appFrame.setJMenuBar(menuBar);
        appFrame.setSize(850, 620);
        appFrame.setResizable(false);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setVisible(true);
        appFrame.setLayout(new BorderLayout());

        // add panel to the frame
        appFrame.getContentPane().add(appPanel);

    }

}
