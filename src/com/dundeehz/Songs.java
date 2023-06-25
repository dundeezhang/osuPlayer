package com.dundeehz;

import java.io.*;

public class Songs {
    private final File data;

    public Songs(File data) {
        this.data = data;
    }

    FileFilter filterOSU = f -> f.getName().endsWith("osu");

    public void initSongs() throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(data));
        // get osu songs folder location
        String osuLocation = fileReader.readLine();
        File osuSongsFolder = new File(osuLocation.substring(0, osuLocation.length() - 8) + "songs\\");

        File[] arr = osuSongsFolder.listFiles();
        assert arr != null;
        for (File file : arr) {
            File currentFolder = new File(osuSongsFolder + "\\" + file.getName() + "\\");
            File[] arr2 = currentFolder.listFiles(filterOSU);
            if(arr2 == null) continue;
            try {
                BufferedReader readSongData = new BufferedReader(new FileReader(arr2[0]));
                // read song name, arist, mp3 location
                String lineRead;
                FileWriter fw = new FileWriter(data, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                while ((lineRead = readSongData.readLine()) != null) {
                    if (lineRead.contains("AudioFilename: ")) {
                        pw.print(osuSongsFolder + "\\" + file.getName() + "\\" + lineRead.substring(15) + ";");
                    }
                    if (lineRead.contains("Title:")) {
                        pw.print(lineRead.substring(6) + ";");
                    }
                    if (lineRead.contains("Artist:")) {
                        pw.println(lineRead.substring(7));
                        pw.flush();
                        break;
                    }
                }
                try {
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (IOException ignored) {
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }
}
