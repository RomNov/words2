package ru.moogen.words;

import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private DataFragment mDataFragment;
    private ArrayList<Word> words;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Word.setDateFormat(getResources());

        DataHelper dataHelper = new DataHelper(this);
        SQLiteDatabase sqLiteDatabase = dataHelper.getWritableDatabase();
        words = dataHelper.getWordsFromDB(sqLiteDatabase);

        // Fragment для сохранения списка слов

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        mDataFragment = (DataFragment) fm.findFragmentByTag("data");

        if (mDataFragment == null){
            mDataFragment = new DataFragment();
            fm.beginTransaction().add(mDataFragment, "data").commit();
            mDataFragment.setmWords(words);
        }

        // end Fragment

        pager = findViewById(R.id.view_pager);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), words));
        pager.setCurrentItem(words.size() - 2);



    }


    public void changeFragment(int newPos){
        if (newPos < 0 || newPos >= words.size()){
            return;
        }
        pager.setCurrentItem(newPos);
    }



}
