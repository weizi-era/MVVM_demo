package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;


import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.repository.TodoRepository;

import java.util.List;

public class TodoViewModel extends BaseViewModel {

    public LiveData<List<Todo>> todos;

    public void getTodos() {
        TodoRepository repository = new TodoRepository();
        todos = repository.getTodos();
        failed = repository.failed;
    }
}
