package com.pradeep.app;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    int id;
    @ColumnInfo(name = "FirstName")
    String mFirstName;
    @ColumnInfo(name = "LastName")
    String mLastName;
    @ColumnInfo(name = "EmailId")
    String mEmailId;

    public String getFirstName() {
        return mFirstName;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

}
