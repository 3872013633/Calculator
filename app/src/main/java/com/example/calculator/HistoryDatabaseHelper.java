package com.example.calculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK =
            "create table History (" +
                    "id integer primary key autoincrement, " +
                    "time text, " +
                    "expression text, " +
                    "result text)";
    private Context context;

    public HistoryDatabaseHelper(Context context) {
        super(context, "History.db", null ,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
