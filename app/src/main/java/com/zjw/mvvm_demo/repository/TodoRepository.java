package com.zjw.mvvm_demo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.db.bean.Todo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TodoRepository {
    private static final String TAG = TodoRepository.class.getSimpleName();

    public MutableLiveData<Todo> todoLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<Todo>> todosMutableLiveData = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    public final List<Todo> emptyList = new ArrayList<>();

    public void addTodo(Todo todo) {
        Completable insert = BaseApplication.getDatabase().todoDao().insert(todo);

        CustomDisposable.addDisposable(insert, () -> Log.d(TAG, "saveTodo: 待办数据保存成功"));
    }

    public MutableLiveData<List<Todo>> getTodos() {
        Flowable<List<Todo>> all = BaseApplication.getDatabase().todoDao().getAll();
        CustomDisposable.addDisposable(all, todos -> {
            if (todos.size() > 0) {
                todosMutableLiveData.postValue(todos);
            } else {
                todosMutableLiveData.postValue(emptyList);
                failed.postValue("暂无数据");
            }
        });

        return todosMutableLiveData;
    }

    /**
     * 根据id获取待办
     * @param uid id
     */
    public MutableLiveData<Todo> getTodoById(int uid) {
        Flowable<Todo> flowable = BaseApplication.getDatabase().todoDao().findById(uid);
        CustomDisposable.addDisposable(flowable, todo -> {
            if (todo != null) {
                todoLiveData.postValue(todo);
            } else {
                failed.postValue("未查询到待办");
            }
        });
        return todoLiveData;
    }

    /**
     * 更新待办
     *
     * @param todo
     */
    public void updateTodo(Todo todo) {
        Completable update = BaseApplication.getDatabase().todoDao().update(todo);
        CustomDisposable.addDisposable(update, () -> {
            Log.d(TAG, "updateTodo: " + "更新成功");
            failed.postValue("200");
        });
    }

    /**
     * 删除待办
     */
    public void deleteTodo(Todo todo) {
        Completable delete = BaseApplication.getDatabase().todoDao().delete(todo);
        CustomDisposable.addDisposable(delete, () -> {
            Log.d(TAG, "deleteTodo: " + "删除成功");
            failed.postValue("200");
        });
    }

    /**
     * 删除所有待办
     */
    public void deleteAll() {
        Completable delete = BaseApplication.getDatabase().todoDao().deleteAll();
        CustomDisposable.addDisposable(delete, () -> {
            Log.d(TAG, "deleteTodo: " + "删除全部成功");
            failed.postValue("200");
        });
    }


    /**
     * 根据uid删除待办
     */
    public void deleteById(int uid) {
        Completable delete = BaseApplication.getDatabase().todoDao().deleteById(uid);
        CustomDisposable.addDisposable(delete, () -> {
            Log.d(TAG, "deleteTodo: " + "删除部成功");
            failed.postValue("200");
        });
    }
}
