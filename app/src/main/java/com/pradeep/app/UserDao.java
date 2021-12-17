package com.pradeep.app;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();
    @Insert
    void insert(User task);
    @Delete
    void delete(User task);
    @Query("UPDATE User SET FirstName=:firstName,LastName=:lastName, EmailId=:email WHERE id = :id")
    void update(String firstName,String lastName, String email,int id);
}
