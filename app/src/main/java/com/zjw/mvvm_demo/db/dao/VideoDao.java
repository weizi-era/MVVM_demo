package com.zjw.mvvm_demo.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zjw.mvvm_demo.db.bean.Video;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM video")
    Flowable<List<Video>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Video> videos);

    @Query("DELETE FROM video")
    Completable deleteAll();
}
