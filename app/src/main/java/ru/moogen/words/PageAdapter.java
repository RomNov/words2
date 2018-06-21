package ru.moogen.words;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {

    private ArrayList<Word> wordsList;


    public PageAdapter(FragmentManager fm, ArrayList<Word> wordList) {
        super(fm);
        this.wordsList = wordList;
    }

    @Override
    public Fragment getItem(int position) {
        return (PageFragment.newInstance(wordsList.get(position), wordsList.size(), position));
    }

    @Override
    public int getCount() {
        return wordsList.size();
    }
}
