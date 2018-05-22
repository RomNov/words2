package ru.moogen.words;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Word.setDateFormat(getResources());

        DataHelper dataHelper = new DataHelper(this);
        SQLiteDatabase sqLiteDatabase = dataHelper.getWritableDatabase();
        ArrayList<Word> words = dataHelper.getWordsFromDB(sqLiteDatabase);

        for (int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i));
        }



    }



}
