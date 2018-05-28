package ru.moogen.words;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private DataFragment mDataFragment;
    private ArrayList<Word> words;
    private String appPackageName;



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

        appPackageName = getPackageName();

    }


    public void changeFragment(int newPos){
        if (newPos < 0 || newPos >= words.size()){
            return;
        }
        pager.setCurrentItem(newPos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId){
            case R.id.menu_el_random:
                randomPosition();
                return true;
            case R.id.menu_el_share:
                int currentItem = pager.getCurrentItem();
                Word word = words.get(currentItem);
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder subject = new StringBuilder();
                if (currentItem != words.size() - 1) {
                    stringBuilder.append(word.getName()).append(" - ").append(word.getDescriptionSend())
                            .append("\nНовое слово каждый день в приложении \"Слово дня\" \nСкачать по ссылке: https://play.google.com/store/apps/details?id=")
                            .append(appPackageName);
                    subject.append("новое слово - ").append(word.getName());
                } else {
                    stringBuilder.append("Новое слово каждый день в приложении \"Слово дня\" \nСкачать по ссылке: https://play.google.com/store/apps/details?id=")
                            .append(appPackageName);
                    subject.append("Приложение - слово дня");
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
                intent.setType("text/plain");
                startActivity(intent);
                return true;
            case R.id.menu_el_search:
                return true;
        }
        return false;
    }

    public void randomPosition(){
        int max = words.size() - 1;
        if (max <= 0){
            return;
        }
        int currentPosition = pager.getCurrentItem();
        int randomPosition = (int)(Math.random()*max);
        System.out.println(randomPosition);
        if (randomPosition == currentPosition){
            randomPosition();
            return;
        }
        pager.setCurrentItem(randomPosition);
    }






}
