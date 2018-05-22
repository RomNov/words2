package ru.moogen.words;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class DataFragment extends Fragment {

    private ArrayList<Word> mWords;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public ArrayList<Word> getmWords() {
        return mWords;
    }

    public void setmWords(ArrayList<Word> mWords) {
        this.mWords = mWords;
    }
}
