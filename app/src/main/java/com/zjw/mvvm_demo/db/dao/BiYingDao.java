package com.zjw.mvvm_demo.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zjw.mvvm_demo.db.bean.BiYing;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface BiYingDao {

    @Query("SELECT * FROM biying")
    List<BiYing> getAll();

    @Query("SELECT * FROM biying WHERE uid LIKE :uid LIMIT 1")
    Flowable<BiYing> queryById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(BiYing... biYings);

    @Delete
    void delete(BiYing biYing);
}
