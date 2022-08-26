package com.hfad.myconstraintlayout.database;

/**
 * Created by evgeny on 08.03.18.
 */

import android.database.Cursor;
import android.database.CursorWrapper;

import com.hfad.myconstraintlayout.data.Radio;

import java.util.Date;
import java.util.UUID;

import com.hfad.myconstraintlayout.database.RadioDbShema.RadioTable;

public class RadioCursorWrapper extends CursorWrapper {
    public RadioCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Radio getRadio() {
        String uuidString = getString(getColumnIndex(RadioTable.Cols.UUID));
        String name = getString(getColumnIndex(RadioTable.Cols.NAME));
        long date = getLong(getColumnIndex(RadioTable.Cols.DATE));
        String addres = getString(getColumnIndex(RadioTable.Cols.ADDRES));
        int isPlayed = getInt(getColumnIndex(RadioTable.Cols.PLAYED));

        Radio radio = new Radio(UUID.fromString(uuidString));
        radio.setName(name);
        radio.setDate(new Date(date));
        radio.setAddres(addres);
        radio.setPlayed(isPlayed != 0);

        return radio;
    }
}