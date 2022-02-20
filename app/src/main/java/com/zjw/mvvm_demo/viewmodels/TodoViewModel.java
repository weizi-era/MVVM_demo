package com.zjw.mvvm_demo.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;


import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.repository.CustomDisposable;
import com.zjw.mvvm_demo.repository.TodoRepository;

import java.util.List;

import io.reactivex.Completable;

public class TodoViewModel extends BaseViewModel {

    public LiveData<List<Todo>> todos;
    TodoRepository repository = new TodoRepository();

    public void getTodos() {
        todos = repository.getTodos();
        failed = repository.failed;
    }


    public void updateTodo(Todo todo) {
        repository.updateTodo(todo);
        failed = repository.failed;
    }

    /**
     * 删除待办
     */
    public void deleteTodo(Todo todo) {
        repository.deleteTodo(todo);
        failed = repository.failed;
    }

    public void deleteAll() {
        repository.deleteAll();
        failed = repository.failed;
    }

    /**
     * 根据uid删除待办
     */
    public void deleteById(int uid) {
       repository.deleteById(uid);
       failed = repository.failed;
    }
}
