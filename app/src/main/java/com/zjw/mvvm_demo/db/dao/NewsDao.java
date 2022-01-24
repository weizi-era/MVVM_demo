package com.zjw.mvvm_demo.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zjw.mvvm_demo.db.bean.News;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    Flowable<List<News>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<News> news);

    @Query("DELETE FROM news")
    Completable deleteAll();
}
