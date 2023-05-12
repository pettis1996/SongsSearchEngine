# Songs Search Engine for Information Retrieval

> Author: Pettis Paraskevas-Christos [3136]
<br>

> Author: Vafeias Dimitrios [3150]

# 1. Introduction

The Songs Search Engine Application is made as part of the Information Retrieval subject at the University of Ioannina, by the authors mentioned above. The application's corpus and index contains thousands of the top tracks from Spotify including the Artist Name, Song Name, and their Lyrics. Use the application to search songs from the index according to the fields mentioned above, and get their lyrics! The application is still in development and so you may find some bugs in your way. In the following guide, you will be guided to the steps to install and use the search engine to its full potential.

# 2. Installation

**Requirements**: Java JDK 17 or 20

**Step 1**

Open the project through the pom.xml file. (IntelliJ IDE recommended)

**Step 2**

Setup the correct JDK and build paths, before building the project.

**Step 3**

Start the application by executing RunApplication.java.

# 3. Getting Started

**Step 1**

Click 'OK' on the Welcome Message. An input dialog should appear asking for the root path directory.
Copy and paste your root path directory of the project to the text field and click 'OK'.

**Example Input**:

`C:\Users\user\Documents\SongsSearchEngine`

***IMPORTANT: PATH MUST ONLY INCLUDE ENGLISH LETTERS, NUMBERS, AND COMMON SPECIAL CHARACTERS***

***Note: If the path is right the Spotify icon should appear on the application, otherwise click on Settings from the menu bar and then click on Set Path Directory to re-enter the root path directory.***

**Step 2**

Once the main application window is opened, the following fields are in display:

| **Field**           | **Description**                                                                                                                                   |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Search a query**  | Input keywords for searching here for searching inside the index.<br>Example queries: justing NOT bieber, ice cube, abba, love AND hate, amer*ca. |
| **Search in field** | Input a field name ( artist, song, lyrics ) to get search results from searching in the specific field of the index.                              |
| **Search**          | Click on the button after you input a query, and/or a field for searching inside the index using the input query, and/or field.                   |
| **Add a filter**    | Input a field name ( artist, song, lyrics ) to add a sorting filter using that field.                                                             |
| **Add filter**      | Apply the field filter and sort the results.                                                                                                      |
| **History**         | Click to open the history tab, and view all the search queries with a counter for popularity included. To refresh, Hide and re-open the tab.      |
| **Clear History**   | Click to clear all search history. To refresh, Hide and re-open the history tab.                                                                  |
