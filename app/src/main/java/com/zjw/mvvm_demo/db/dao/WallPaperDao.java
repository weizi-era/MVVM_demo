package com.zjw.mvvm_demo.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.zjw.mvvm_demo.db.bean.WallPaper;

import java.util.List;

@Dao
public interface WallPaperDao {

    @Query("SELECT * FROM wallpaper")
    List<WallPaper> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WallPaper> wallPapers);

    @Query("DELETE FROM wallpaper")
    void deleteAll();
}
