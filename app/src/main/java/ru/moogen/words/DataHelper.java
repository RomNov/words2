package ru.moogen.words;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "words.db";
    public static final int DATABASE_VERSION = 20;

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

    public ArrayList<Word> getWordsFromDB(SQLiteDatabase db){
        ArrayList<Word> result = new ArrayList<>();
        Cursor cursor = db.query(TABLE_WORDS_NAME, null, null, null
        , null, null, COLUMN_ID);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String additionalName = null;
            if (!cursor.isNull(cursor.getColumnIndex(COLUMN_ADDITIONAL_NAME))){
                additionalName = cursor.getString(cursor.getColumnIndex(COLUMN_ADDITIONAL_NAME));
            }
            String etim = null;
            if (!cursor.isNull(cursor.getColumnIndex(COLUMN_ETIM))){
                etim = cursor.getString(cursor.getColumnIndex(COLUMN_ETIM));
            }
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String example = null;
            if (!cursor.isNull(cursor.getColumnIndex(COLUMN_EXAMPLE))){
                example = cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLE));
            }
            int favor = cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE));
            boolean favorite = favor == 1;

            Word word = new Word(id, date, name, additionalName, etim, description, example, favorite);
            result.add(word);
            }
            Word lastWord = new Word(50000, "01.01.2025", null, null
                    , null,"Новое слово будет ждать вас здесь завтра"
                    , null, false);
        result.add(lastWord);
        return result;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE TABLE ---------------------------------------------------------------------------
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
        // END CREATE TABLE -----------------------------------------------------------------------

        // LOAD FROM FILE -------------------------------------------------------------------------
        ArrayList<String> strings = FileLoader.loadFromFile(mContext.getResources()
                .openRawResource(R.raw.words), "utf-8");
        ArrayList<Word> words = FileLoader.getSortedWordList(strings, ";");
        // END LOAD FROM FILE ---------------------------------------------------------------------

        // CREATE SQL QUERRY ----------------------------------------------------------------------
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

            // INSERT DATA TO DATABASE ------------------------------------------------------------
        for (int i = 0; i < words.size(); i++) {
            Word currentWord = words.get(i);
            sqlInsert.append("(").append(currentWord.getId()).append(", '")
                    .append(currentWord.getDate()).append("', '")
                    .append(currentWord.getName()).append("', ");

            String additionalName = currentWord.getAdditionalName();
            if (additionalName.equals(NULL)){
                sqlInsert.append(additionalName).append(", ");
            }else {
                sqlInsert.append("'").append(additionalName).append("', ");
            }

            String etim = currentWord.getEtim();
            if (etim.equals(NULL)){
                sqlInsert.append(etim).append(", ");
            }else {
                sqlInsert.append("'").append(etim).append("', ");
            }

            sqlInsert.append("'").append(currentWord.getDescription()).append("', ");

            String example = currentWord.getExample();
            if (example.equals(NULL)){
                sqlInsert.append(example).append(", ");
            }else {
                sqlInsert.append("'").append(example).append("', ");
            }

            sqlInsert.append("0)");
            String endOffString = ",";
            if (i == words.size() - 1){
                endOffString = ";";
            }

            sqlInsert.append(endOffString);
        }
            // END INSERT DATA TO DATABASE --------------------------------------------------------

        db.execSQL(sqlInsert.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS_NAME);
        onCreate(db); // TODO СДЕЛАТЬ ПЕРЕХОД ИЗБРАННОГО В НОВУЮ БАЗУ

    }
}
