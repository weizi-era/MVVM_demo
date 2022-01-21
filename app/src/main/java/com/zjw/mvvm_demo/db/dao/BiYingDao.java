package com.zjw.mvvm_demo.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zjw.mvvm_demo.db.bean.BiYing;

import java.util.List;

@Dao
public interface BiYingDao {

    @Query("SELECT * FROM biying")
    List<BiYing> getAll();


    @Query("SELECT * FROM biying WHERE uid LIKE :uid LIMIT 1")
    BiYing queryById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(BiYing... biYings);

    @Delete
    void delete(BiYing biYing);
}
