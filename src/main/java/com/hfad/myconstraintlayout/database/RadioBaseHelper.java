package com.hfad.myconstraintlayout.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hfad.myconstraintlayout.database.RadioDbShema.RadioTable;

/**
 * Created by evgeny on 08.03.18.
 */

public class RadioBaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "RadioBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "radioBase.db";

    public RadioBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + RadioTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                RadioTable.Cols.UUID + ", " +
                RadioTable.Cols.NAME + ", " +
                RadioTable.Cols.DATE + ", " +
                RadioTable.Cols.ADDRES + ", " +
                RadioTable.Cols.PLAYED +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
