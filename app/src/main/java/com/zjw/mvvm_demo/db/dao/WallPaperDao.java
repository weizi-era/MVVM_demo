package com.zjw.mvvm_demo.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zjw.mvvm_demo.db.bean.WallPaper;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface WallPaperDao {

    @Query("SELECT * FROM wallpaper")
    Flowable<List<WallPaper>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<WallPaper> wallPapers);

    @Query("DELETE FROM wallpaper")
    Completable deleteAll();
}
