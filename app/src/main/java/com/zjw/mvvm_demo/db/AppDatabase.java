package com.zjw.mvvm_demo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zjw.mvvm_demo.db.bean.BiYing;
import com.zjw.mvvm_demo.db.bean.WallPaper;
import com.zjw.mvvm_demo.db.dao.BiYingDao;
import com.zjw.mvvm_demo.db.dao.WallPaperDao;

@Database(entities = {BiYing.class, WallPaper.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BiYingDao imageDao();

    public abstract WallPaperDao wallPaperDao();
}
