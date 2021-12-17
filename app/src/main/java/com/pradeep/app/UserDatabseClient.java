package com.pradeep.app;

import android.arch.persistence.room.Room;
import android.content.Context;

public class UserDatabseClient {
    private Context mContext;
    private static UserDatabseClient mInstance;
    private AppLocalDatabase appDatabase;

    private UserDatabseClient(Context mContext) {
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext, AppLocalDatabase.class, "localUserList").build();
    }

    public static synchronized UserDatabseClient getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new UserDatabseClient(mContext);
        }
        return mInstance;
    }

    public AppLocalDatabase getAppDatabase() {
        return appDatabase;
    }
}