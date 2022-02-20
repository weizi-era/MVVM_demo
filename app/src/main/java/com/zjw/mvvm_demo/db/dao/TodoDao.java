package com.zjw.mvvm_demo.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.db.bean.Todo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM todo")
    Flowable<List<Todo>> getAll();

    @Update
    Completable update(Todo todo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Todo Todo);

    @Query("SELECT * FROM todo WHERE uid=:uid")
    Flowable<Todo> findById(int uid);

    @Delete
    Completable delete(Todo todo);

    @Query("DELETE FROM todo")
    Completable deleteAll();

    @Query("DELETE FROM todo WHERE uid=:uid")
    Completable deleteById(int uid);
}
