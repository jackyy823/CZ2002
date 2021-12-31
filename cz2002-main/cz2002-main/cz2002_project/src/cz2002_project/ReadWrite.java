package cz2002_project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadWrite {
    public ArrayList<List<String>> readCsv(String csvName) {
        ArrayList<List<String>> dataArr = new ArrayList<>();
        Path path = Paths.get(csvName);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();

            while (line != null) {
                List<String> list1 = new ArrayList<>();
                String[] tokens = line.split(",");

                Collections.addAll(list1, tokens);
                dataArr.add(list1);

                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataArr;
    }

    public void outputCsv(ArrayList<List<String>> dataArr, String csvName) throws IOException {
        Path path = Paths.get(csvName);
        BufferedWriter br = new BufferedWriter(new FileWriter(String.valueOf(path)));
        StringBuilder sb = new StringBuilder();

        for (List list : dataArr) {
            int size = list.size();
            for (int i=0; i<size; i++) {
                sb.append(list.get(i));
                sb.append(",");
            }
            sb.append("\n");
        }

        br.write(String.valueOf(sb));
        br.close();
    }
}