package com.pradeep.app;

import android.util.Patterns;

public class UserDetails {
    String mFirstName;
    String mLastName;
    String mEmailId;
    public UserDetails(String mFirstName,String mLastName,String mEmailId) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mEmailId = mEmailId;
    }
    public String getFirstName() {
        return mFirstName;
    }


    public String getLastName() {
        return mLastName;
    }


    public String getEmailId() {
        return mEmailId;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmailId()).matches();
    }

    public boolean firstNameCheck() {
        return getFirstName().length() < 3;
    }
    public boolean lastNameCheck() {
        return getLastName().length() < 3;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mEmailId='" + mEmailId + '\'' +
                '}';
    }
}

