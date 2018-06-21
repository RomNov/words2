package ru.moogen.words;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PageFragment extends Fragment {

    private Word word;
    private int size;
    private int position;
    private TextView nameTextView;
    private TextView additionalNameTextView;
    private TextView etimTextView;
    private TextView descriptionTextView;
    private TextView exampleTextView;
    private View lineDelimiter;

    private FloatingActionButton fab;
    private ImageButton imageButtonLeft;
    private ImageButton imageButtonRight;
    private TextView upDateTextView;
    private Button googleButton;

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public static PageFragment newInstance(Word word, int size, int position) {
        PageFragment fragment = new PageFragment();
        fragment.setWord(word);
        fragment.setSize(size);
        fragment.setPosition(position);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View result = inflater.inflate(R.layout.page_fragment, container, false);

        nameTextView = result.findViewById(R.id.text_view_name);
        nameTextView.setText(word.getName());

        if (!(word.getAdditionalName() == null)) {
            additionalNameTextView = result.findViewById(R.id.text_view_additional_name);
            additionalNameTextView.setText(word.getAdditionalName());
            additionalNameTextView.setVisibility(View.VISIBLE);
        }

        if (!(word.getEtim() == null)) {
            etimTextView = result.findViewById(R.id.text_view_etim);
            etimTextView.setText(word.getEtim());
            etimTextView.setVisibility(View.VISIBLE);
        }

        descriptionTextView = result.findViewById(R.id.text_view_description);
        descriptionTextView.setText(Html.fromHtml(word.getDescription()));

        if (!(word.getExample() == null)) {
            exampleTextView = result.findViewById(R.id.text_view_example);
            exampleTextView.setText(word.getExample());
            exampleTextView.setVisibility(View.VISIBLE);
            lineDelimiter = result.findViewById(R.id.divider_pager);
            lineDelimiter.setVisibility(View.VISIBLE);
        }

        fab = result.findViewById(R.id.fab);
        if (word.isFavourite()) {
            fab.setImageResource(R.mipmap.ic_fab_yellow_star);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean currentValue = word.isFavourite();
                word.setFavourite(!currentValue);
                if (currentValue) {
                    fab.setImageResource(R.mipmap.ic_fab_gray_star);
                } else {
                    fab.setImageResource(R.mipmap.ic_fab_yellow_star);
                }
                final int favor = currentValue ? 0 : 1;
                final int id = word.getId();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataHelper dataHelper = new DataHelper(getActivity());
                        SQLiteDatabase sqLiteDatabase = dataHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DataHelper.COLUMN_FAVOURITE, favor);
                        sqLiteDatabase.update(DataHelper.TABLE_WORDS_NAME, contentValues
                                , DataHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                    }
                }).start();
            }
        });


        upDateTextView = result.findViewById(R.id.text_view_up_date);
        upDateTextView.setText(Word.getShortDateFormat().format(word.getDateClassDate()));

        imageButtonLeft = result.findViewById(R.id.button_up_left);
        imageButtonRight = result.findViewById(R.id.button_up_right);
        googleButton = result.findViewById(R.id.button_google);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("https://www.google.ru/search?q=" + word.getSearchName());
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
            }
        });

        imageButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) PageFragment.this.getActivity()).changeFragment(position + 1);
            }
        });
        imageButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) PageFragment.this.getActivity()).changeFragment(position - 1);
            }
        });

        if (position == size - 1) {
            fab.setVisibility(View.GONE);
            imageButtonRight.setVisibility(View.GONE);
            googleButton.setVisibility(View.GONE);
            descriptionTextView.setTextSize(24);
            upDateTextView.setText("будущий день");
        } else if (position == 0) {
            imageButtonLeft.setVisibility(View.GONE);
        }

        return result;
    }

    public PageFragment() {
    }
}
