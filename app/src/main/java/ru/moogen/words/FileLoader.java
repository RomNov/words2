package ru.moogen.words;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FileLoader {

    public static ArrayList<String> loadFromFile(InputStream inputStream, String charset){
        ArrayList<String> result = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream, charset);
        scanner.useDelimiter("\n");
        scanner.next();
        while (scanner.hasNext()){
            result.add(scanner.next());
        }
        scanner.close();
        return result;
    }

    public static ArrayList<Word> getSortedWordList(ArrayList<String> strings, String delimiter){
        ArrayList<Word> result = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            String[] row = strings.get(i).split(delimiter);
            String date = row[0];
            String name = row[1];
            String additionalName = row[2];
            String etim = row[3];
            String description = row[4];
            String example = row[5];
            Word word = new Word(i, date,name,  additionalName, etim, description
                    , example, false);
            result.add(word);
        }




        Collections.sort(result);
        return result;
    }
}
