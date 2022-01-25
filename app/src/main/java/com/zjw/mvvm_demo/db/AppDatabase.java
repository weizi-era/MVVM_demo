package com.zjw.mvvm_demo.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zjw.mvvm_demo.db.bean.BiYing;
import com.zjw.mvvm_demo.db.bean.News;
import com.zjw.mvvm_demo.db.bean.User;
import com.zjw.mvvm_demo.db.bean.Video;
import com.zjw.mvvm_demo.db.bean.WallPaper;
import com.zjw.mvvm_demo.db.dao.BiYingDao;
import com.zjw.mvvm_demo.db.dao.NewsDao;
import com.zjw.mvvm_demo.db.dao.UserDao;
import com.zjw.mvvm_demo.db.dao.VideoDao;
import com.zjw.mvvm_demo.db.dao.WallPaperDao;

import org.jetbrains.annotations.NotNull;

@Database(entities = {BiYing.class, WallPaper.class, News.class,
        Video.class, User.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mvvm_demo";
    private static volatile AppDatabase mInstance;

    public abstract BiYingDao imageDao();

    public abstract WallPaperDao wallPaperDao();

    public abstract NewsDao newsDao();

    public abstract VideoDao videoDao();

    public abstract UserDao userDao();

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            "mvvm_demo")
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
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

    /**
     * 版本升级迁移到3 新增新闻表和视频表
     */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            //创建新闻表
            database.execSQL("CREATE TABLE `news` " +
                    "(uid INTEGER NOT NULL, " +
                    "uniquekey TEXT, " +
                    "title TEXT, " +
                    "date TEXT," +
                    "category TEXT," +
                    "author_name TEXT," +
                    "url TEXT," +
                    "thumbnail_pic_s TEXT," +
                    "is_content TEXT," +
                    "PRIMARY KEY(`uid`))");
            //创建视频表
            database.execSQL("CREATE TABLE `video` " +
                    "(uid INTEGER NOT NULL, " +
                    "title TEXT," +
                    "share_url TEXT," +
                    "author TEXT," +
                    "item_cover TEXT," +
                    "hot_words TEXT," +
                    "PRIMARY KEY(`uid`))");
        }
    };

    /**
     * 版本升级迁移到4，新增用户表
     */
    static final Migration MIGRATION_3_4 = new Migration(3 ,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //创建用户表
            database.execSQL("CREATE TABLE `user` " +
                    "(uid INTEGER NOT NULL, " +
                    "account TEXT," +
                    "pwd TEXT," +
                    "nickName TEXT," +
                    "introduction TEXT," +
                    "PRIMARY KEY(`uid`))");
        }
    };

    /**
     * 版本升级迁移到5 在用户表中新增一个avatar字段
     */
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `user` ADD COLUMN avatar TEXT");
        }
    };
}
