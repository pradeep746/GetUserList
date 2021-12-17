package com.pradeep.app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class AppLocalDatabase extends RoomDatabase {
    public abstract UserDao localUserDao();
}
