package com.hfad.myconstraintlayout.data;

/**
 * Created by evgeny on 06.03.18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.hfad.myconstraintlayout.database.RadioBaseHelper;
import com.hfad.myconstraintlayout.database.RadioCursorWrapper;
import com.hfad.myconstraintlayout.database.RadioDbShema.RadioTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RadioLab {
    private static RadioLab sRadioLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RadioLab get(Context context) {
        if (sRadioLab == null) {
            sRadioLab = new RadioLab(context);
        }
        return sRadioLab;
    }

    private RadioLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RadioBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(Radio radio) {
        ContentValues values = new ContentValues();
        values.put(RadioTable.Cols.UUID, radio.getId().toString());
        values.put(RadioTable.Cols.NAME, radio.getName());
        values.put(RadioTable.Cols.DATE, radio.getDate().getTime());
        values.put(RadioTable.Cols.ADDRES, radio.getAddres());
        values.put(RadioTable.Cols.PLAYED, radio.isPlayed() ? 1 : 0);

        return values;
    }

    public void addRadio(Radio r) {
        ContentValues values = getContentValues(r);

        mDatabase.insert(RadioTable.NAME, null, values);
    }

    public void deleteRadio(Radio r) {

        mDatabase.delete(RadioTable.NAME, RadioTable.Cols.UUID + " = ?", new String[]{ String .valueOf(r.getId()) });
    }



    public void updateRadio(Radio radio) {
        String uuidString = radio.getId().toString();
        ContentValues values = getContentValues(radio);

        mDatabase.update(RadioTable.NAME, values,
                RadioTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private RadioCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RadioTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new RadioCursorWrapper(cursor);
    }


    public List<Radio> getRadios() {
        List<Radio> crimes = new ArrayList<>();

        RadioCursorWrapper cursor = queryCrimes(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            crimes.add(cursor.getRadio());
            cursor.moveToNext();
        }
        cursor.close();

        return crimes;
    }

    public Radio getRadio(UUID id) {
        RadioCursorWrapper cursor = queryCrimes(
                RadioTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRadio();
        } finally {
            cursor.close();
        }
    }
}





//import android.content.Context;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class RadioLab {
//    private static RadioLab sRadioLab;
//
//    private ArrayList<Radio> mRadios;
//
//    public static RadioLab get(Context context) {
//        if (sRadioLab == null) {
//            sRadioLab = new RadioLab(context);
//        }
//        return sRadioLab;
//    }
//
//    private RadioLab(Context context) {
//        mRadios = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Radio radio = new Radio();
//            radio.setName("Radio #" + i);
//            radio.setPlayed(i % 2 == 0);
//            mRadios.add(radio);
//        }
//    }
//
//    public List<Radio> getRadios() {
//        return mRadios;
//    }
//
//    public Radio getCrime(UUID id) {
//        for (Radio radio : mRadios) {
//            if (radio.getId().equals(id)) {
//                return radio;
//            }
//        }
//        return null;
//    }
//}
