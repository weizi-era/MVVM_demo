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
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.db.bean.User;
import com.zjw.mvvm_demo.db.bean.Video;
import com.zjw.mvvm_demo.db.bean.WallPaper;
import com.zjw.mvvm_demo.db.dao.BiYingDao;
import com.zjw.mvvm_demo.db.dao.NewsDao;
import com.zjw.mvvm_demo.db.dao.NotebookDao;
import com.zjw.mvvm_demo.db.dao.TodoDao;
import com.zjw.mvvm_demo.db.dao.UserDao;
import com.zjw.mvvm_demo.db.dao.VideoDao;
import com.zjw.mvvm_demo.db.dao.WallPaperDao;

import org.jetbrains.annotations.NotNull;

@Database(entities = {BiYing.class, WallPaper.class, News.class,
        Video.class, User.class, Notebook.class, Todo.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mvvm_demo";
    private static volatile AppDatabase mInstance;

    public abstract BiYingDao imageDao();

    public abstract WallPaperDao wallPaperDao();

    public abstract NewsDao newsDao();

    public abstract VideoDao videoDao();

    public abstract UserDao userDao();

    public abstract NotebookDao notebookDao();

    public abstract TodoDao todoDao();

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
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
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

    /**
     * 版本升级迁移到6 在数据库中新增一个笔记表
     */
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            //创建笔记表
            database.execSQL("CREATE TABLE `notebook` " +
                    "(uid INTEGER NOT NULL, " +
                    "title TEXT, " +
                    "content TEXT, " +
                    "date TEXT, " +
                    "time TEXT, " +
                    "PRIMARY KEY(`uid`))");
        }
    };

    /**
     * 版本升级迁移到7 在数据库中新增一个待办表
     */
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            //创建待办表
            database.execSQL("CREATE TABLE `todo` " +
                    "(uid INTEGER NOT NULL, " +
                    "title TEXT, " +
                    "remark TEXT, " +
                    "date TEXT, " +
                    "import INTEGER NOT NULL DEFAULT 0, " +
                    "done INTEGER NOT NULL DEFAULT 0, " +
                    "PRIMARY KEY(`uid`))");
        }
    };


}
