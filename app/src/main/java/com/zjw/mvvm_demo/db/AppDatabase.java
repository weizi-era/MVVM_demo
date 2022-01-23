package com.zjw.mvvm_demo.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zjw.mvvm_demo.db.bean.BiYing;
import com.zjw.mvvm_demo.db.bean.WallPaper;
import com.zjw.mvvm_demo.db.dao.BiYingDao;
import com.zjw.mvvm_demo.db.dao.WallPaperDao;

@Database(entities = {BiYing.class, WallPaper.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mvvm_demo";
    private static volatile AppDatabase mInstance;

    public abstract BiYingDao imageDao();

    public abstract WallPaperDao wallPaperDao();

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            "mvvm_demo")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }

        return mInstance;
    }

    /**
     * 版本升级迁移
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `wallpaper`" +
                    "(uid INTEGER NOT NULL, " + "image TEXT, " +
                    "PRIMARY KEY(`uid`))");
        }
    };
}
