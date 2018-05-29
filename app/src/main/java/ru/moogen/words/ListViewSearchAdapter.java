package ru.moogen.words;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ListViewSearchAdapter extends ArrayAdapter {
    private ArrayList<Word> wordList;
    private Context context;
    private AppCompatActivity appCompatActivity;

    public ListViewSearchAdapter(@NonNull AppCompatActivity context, ArrayList<Word> wordList) {
        super(context, R.layout.list_item);
        this.wordList = wordList;
        this.context = context.getApplicationContext();
        this.appCompatActivity = context;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return wordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return wordList.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Word word = wordList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = view.findViewById(R.id.list_item_text_view);
        textView.setText(word.getName());
        final ImageButton imageButton = view.findViewById(R.id.list_item_image_button);
        if (!word.isFavourite()){
            imageButton.setImageResource(R.mipmap.ic_fab_gray_star);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean currentValue = word.isFavourite();
                word.setFavourite(!currentValue);
                final int id = word.getId();
                if (currentValue){
                    imageButton.setImageResource(R.mipmap.ic_fab_gray_star);
                } else {
                    imageButton.setImageResource(R.mipmap.ic_fab_yellow_star);
                }
                final int favor = currentValue?0:1;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataHelper dataHelper = new DataHelper(context);
                        SQLiteDatabase sqLiteDatabase = dataHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DataHelper.COLUMN_FAVOURITE, favor);
                        sqLiteDatabase.update(DataHelper.TABLE_WORDS_NAME, contentValues
                                , DataHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                    }
                }).start();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("position", word.getId());
                appCompatActivity.setResult(RESULT_OK, intent);
                appCompatActivity.finish();
            }
        });
        return view;
    }
}
