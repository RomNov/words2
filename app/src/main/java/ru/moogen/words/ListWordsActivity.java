package ru.moogen.words;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ListWordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        int type = 0;

        Intent data = getIntent();
        if (data != null) {
            type = data.getIntExtra("type", 0);
        }

        DataHelper dataHelper = new DataHelper(this);
        SQLiteDatabase sqLiteDatabase = dataHelper.getWritableDatabase();
        ArrayList<Word> wordList = dataHelper.getWordsFromDB(sqLiteDatabase);

        if (type == 2) {
            ArrayList<Word> newList = new ArrayList<>();
            for (int i = 0; i < wordList.size(); i++) {
                if (wordList.get(i).isFavourite()) {
                    newList.add(wordList.get(i));
                }
            }
            wordList = newList;
        }

        if (wordList.size() > 0) {
            if (type != 2) {
                wordList.remove(wordList.size() - 1);
            }
            ListView listView = findViewById(R.id.list_view);
            ListViewSearchAdapter listViewSearchAdapter = new ListViewSearchAdapter(this, wordList);
            listView.setAdapter(listViewSearchAdapter);
        }


    }

}
