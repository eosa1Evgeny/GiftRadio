package com.hfad.myconstraintlayout.data;

/**
 * Created by evgeny on 06.03.18.
 */

import java.util.Date;
import java.util.UUID;

public class Radio {

    private UUID mId;
    private String mName;
    private Date mDate;
    private String mAddres;
    private boolean mPlayed;



    public Radio() {
        mId = UUID.randomUUID();
        mDate = new Date();

    }

    public Radio(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getAddres() {
        return mAddres;
    }

    public void setAddres(String addres) {
        mAddres = addres;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isPlayed() {
        return mPlayed;
    }

    public void setPlayed(boolean played) {
        mPlayed = played;
    }
}
