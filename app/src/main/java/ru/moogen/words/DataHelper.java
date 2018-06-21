package ru.moogen.words;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "words.db";
    public static final int DATABASE_VERSION = 27;

    public static final String TABLE_WORDS_NAME = "words";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDITIONAL_NAME = "additional_name";
    public static final String COLUMN_ETIM = "etim";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_EXAMPLE = "example";
    public static final String COLUMN_FAVOURITE = "favourite";

    private static final String NULL = "NULL";

    private Context mContext;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public Word getWord(String date, SQLiteDatabase db) {
        Word word = null;
        String[] strArr = new String[]{date};
        String selection = COLUMN_DATE + " = ?";
        Cursor cursor = db.query(TABLE_WORDS_NAME, null, selection, strArr, null, null, null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String date2 = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String additionalName = cursor.getString(cursor.getColumnIndex(COLUMN_ADDITIONAL_NAME));
            String etim = cursor.getString(cursor.getColumnIndex(COLUMN_ETIM));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String example = cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLE));
            word = new Word(id, date2, name, additionalName, etim, description, example, false);
        }
        return word;
    }

    public ArrayList<Word> getWordsFromDB(SQLiteDatabase db) {
        ArrayList<Word> result = new ArrayList<>();
        GregorianCalendar todayCalendar = new GregorianCalendar();
        Cursor cursor = db.query(TABLE_WORDS_NAME, null, null, null
                , null, null, COLUMN_ID);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            GregorianCalendar wordCalendar = new GregorianCalendar();
            Date date1 = Word.getDateFormat().parse(date, new ParsePosition(0));
            wordCalendar.setTime(date1);
            if (wordCalendar.get(Calendar.YEAR) > todayCalendar.get(Calendar.YEAR)) {
                break;
            } else if (wordCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
                if (wordCalendar.get(Calendar.MONTH) > todayCalendar.get(Calendar.MONTH)) {
                    break;
                } else if (wordCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)) {
                    if (wordCalendar.get(Calendar.DAY_OF_MONTH) > todayCalendar.get(Calendar.DAY_OF_MONTH)) {
                        break;
                    }
                }
            }
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String additionalName = null;
            if (!cursor.isNull(cursor.getColumnIndex(COLUMN_ADDITIONAL_NAME))) {
                additionalName = cursor.getString(cursor.getColumnIndex(COLUMN_ADDITIONAL_NAME));
            }
            String etim = null;
            if (!cursor.isNull(cursor.getColumnIndex(COLUMN_ETIM))) {
                etim = cursor.getString(cursor.getColumnIndex(COLUMN_ETIM));
            }
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String example = null;
            if (!cursor.isNull(cursor.getColumnIndex(COLUMN_EXAMPLE)) &&
                    !cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLE)).startsWith("NULL")) {
                example = cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLE));
            }
            int favor = cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE));
            boolean favorite = favor == 1;

            Word word = new Word(id, date, name, additionalName, etim, description, example, favorite);
            result.add(word);
        }
        Word lastWord = new Word(50000, "01.01.2025", null, null
                , null, mContext.getString(R.string.new_tomorrow_word)
                , null, false);
        result.add(lastWord);
        return result;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(TABLE_WORDS_NAME).append(" (")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY NOT NULL, ")
                .append(COLUMN_DATE).append(" TEXT NOT NULL, ")
                .append(COLUMN_NAME).append(" TEXT NOT NULL, ")
                .append(COLUMN_ADDITIONAL_NAME).append(" TEXT, ")
                .append(COLUMN_ETIM).append(" TEXT, ")
                .append(COLUMN_DESCRIPTION).append(" TEXT NOT NULL, ")
                .append(COLUMN_EXAMPLE).append(" TEXT, ")
                .append(COLUMN_FAVOURITE).append(" INTEGER DEFAULT (0) );");
        db.execSQL(sql.toString());

        ArrayList<String> strings = FileLoader.loadFromFile(mContext.getResources()
                .openRawResource(R.raw.words), "utf-8");
        ArrayList<Word> words = FileLoader.getSortedWordList(strings, ";");

        StringBuilder sqlInsert = new StringBuilder();
        sqlInsert.append("INSERT INTO ").append(TABLE_WORDS_NAME).append("(")
                .append(COLUMN_ID).append(", ")
                .append(COLUMN_DATE).append(", ")
                .append(COLUMN_NAME).append(", ")
                .append(COLUMN_ADDITIONAL_NAME).append(", ")
                .append(COLUMN_ETIM).append(", ")
                .append(COLUMN_DESCRIPTION).append(", ")
                .append(COLUMN_EXAMPLE).append(", ")
                .append(COLUMN_FAVOURITE).append(") VALUES");

        for (int i = 0; i < words.size(); i++) {
            Word currentWord = words.get(i);
            sqlInsert.append("(").append(currentWord.getId()).append(", '")
                    .append(currentWord.getDate()).append("', '")
                    .append(currentWord.getName()).append("', ");

            String additionalName = currentWord.getAdditionalName();
            if (additionalName.equals(NULL)) {
                sqlInsert.append(additionalName).append(", ");
            } else {
                sqlInsert.append("'").append(additionalName).append("', ");
            }

            String etim = currentWord.getEtim();
            if (etim.equals(NULL)) {
                sqlInsert.append(etim).append(", ");
            } else {
                sqlInsert.append("'").append(etim).append("', ");
            }

            sqlInsert.append("'").append(currentWord.getDescription()).append("', ");

            String example = currentWord.getExample();
            if (example.equals(NULL)) {
                sqlInsert.append(example).append(", ");
            } else {
                sqlInsert.append("'").append(example).append("', ");
            }

            sqlInsert.append("0)");
            String endOffString = ",";
            if (i == words.size() - 1) {
                endOffString = ";";
            }

            sqlInsert.append(endOffString);
        }

        db.execSQL(sqlInsert.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_WORDS_NAME +
                " WHERE " + COLUMN_FAVOURITE + " = 1;", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            integerArrayList.add(id);
            System.out.println(id);
        }


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS_NAME);
        onCreate(db);

        for (int i = 0; i < integerArrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FAVOURITE, "1");
            db.update(TABLE_WORDS_NAME, values, COLUMN_ID + " = ?", new String[]{integerArrayList.get(i).toString()});
        }


    }
}
