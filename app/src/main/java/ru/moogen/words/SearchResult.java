package ru.moogen.words;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchResult extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        ArrayList<Word> wordList = MainActivity.searchResult;


        if (wordList.size() > 0){
            ListView listView = findViewById(R.id.list_view);
            ListViewSearchAdapter listViewSearchAdapter = new ListViewSearchAdapter(this, wordList);
            listView.setAdapter(listViewSearchAdapter);
        }



    }



}
