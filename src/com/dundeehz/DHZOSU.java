package com.dundeehz;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

public class DHZOSU {
    private JPanel panelArea;
    private JTextField searchBox;
    private JLabel searchLabel;
    private JTextArea logArea;
    private JLabel previousSongsLabel;
    private JCheckBox shuffleCheckBox;
    private JSlider timerSlider;
    private JLabel optionsLabel;
    private JLabel sleepTimerSliderLabel;
    private JCheckBox loopCurrentSongCheckBox;
    private JButton playRandomButton;
    private JLabel currentCollectionLabel;
    private JLabel collectionsLabel;
    private JLabel currentCollectionDisplay;
    private JButton selectCollectionButton;
    private JButton quitButton;
    private JButton selectOsuFolderButton;
    private JProgressBar progressBar1;
    private JButton skipTrackButton;
    private JLabel progressLabel;
    private JLabel additionalOptionsLabel;
    private static File data;
    private static ArrayList<String> songsList;

    public DHZOSU() {

        data = new File("data.txt");
        quitButton.addActionListener(e -> System.exit(0));

        // search for a song to play next
        searchBox.addActionListener(e -> {
            try {
                search(searchBox);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        selectOsuFolderButton.addActionListener(e -> {
            selectFileLocation();
            try {
                Songs songs = new Songs(data);
                songs.initSongs();
                songsList = songs.addToList();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private void selectFileLocation() {
        try {
            String osuPath = Objects.requireNonNull(getInputPath(null)).toString();
            BufferedWriter bufferWrite = null;
            try {

                FileWriter fileWrite = new FileWriter(data, false);
                bufferWrite = new BufferedWriter(fileWrite);
                bufferWrite.write(osuPath + "\n");
                logArea.setText(logArea.getText() + "OSU.EXE SELECTED\n");

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (bufferWrite != null)
                        bufferWrite.close();
                } catch (Exception ex) {
                    System.out.println("Error in closing the BufferedWriter" + ex);
                }
            }
        } catch (Exception e2) {
            logArea.setText(logArea.getText() + "No file was returned\n");
        }
    }

    private void search(JTextField field) throws IOException {
        ArrayList<String> goodList = new ArrayList<>();
        String searched = field.getText();

        if (checkPath()) {
            // just a search where it iterates through everything because there may be multiple results
            for (String s : songsList) {
                if (s.toLowerCase().contains(searched.toLowerCase())) {
                    goodList.add(s);
                }
            }
            logArea.setText("");
            if(goodList.isEmpty()) logArea.setText("No songs were found for \"" + searched + "\"\n");
            for (String s : goodList) {
                System.out.println(s);

                StringTokenizer st = new StringTokenizer(s, ";");
                st.nextToken();
                String songName = st.nextToken();
                String artist = st.nextToken();
                logArea.setText(logArea.getText() + "Song: " + songName + "  | Artist: " + artist + "\n");
            }

        }

    }

    // https://stackoverflow.com/questions/51973636/how-to-return-the-file-path-from-the-windows-file-explorer-using-java
    public static Path getInputPath(String s) {
         /*Send a path (a String path) to open in a specific directory
         or if null default directory */
        JFileChooser jd = s == null ? new JFileChooser() : new JFileChooser(s);

        jd.setDialogTitle("Choose input file");
        int returnVal = jd.showOpenDialog(null);

        /* If user didn't select a file and click ok, return null Path object*/
        if (returnVal != JFileChooser.APPROVE_OPTION) return null;
        return jd.getSelectedFile().toPath();
    }

    /**
     * Main Method
     *
     * @param args Command Line Arguments
     */
    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame("DHZ Osu Player");
        window.setContentPane(new DHZOSU().panelArea);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);

        Songs songs = new Songs(data);
        songsList = songs.addToList();
        //Player player = new Player();
        //player.updateLoc("D:\\osu!\\Songs\\13223 Demetori - Emotional Skyscraper ~ World's End\\Demetori -  Nada Upasana Pundarika - 06 - .mp3");
        //player.start();
    }

    private boolean checkPath() throws IOException {
        BufferedReader test = new BufferedReader(new FileReader(data));
        if (Objects.equals(test.readLine(), "-1")) {
            logArea.setText(logArea.getText() + "PLEASE SELECT OSU.EXE\n");
            return false;
        }
        return true;
    }
}
