package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.repository.TodoRepository;

public class TodoEditViewModel extends BaseViewModel {
    public LiveData<Todo> todo;

    private final TodoRepository repository = new TodoRepository();

//    public NoteEditViewModel(NotebookRepository repository) {
//        this.repository = repository;
//    }

    public void addTodo(Todo todo) {
        repository.addTodo(todo);
        failed = repository.failed;
    }


    /**
     * 根据Id搜索待办
     */
    public void queryById(int uid) {
        failed = repository.failed;
        todo = repository.getTodoById(uid);
    }

    /**
     * 更新待办
     */
    public void updateTodo(Todo todo) {
        failed = repository.failed;
        repository.updateTodo(todo);
    }

    /**
     * 删除待办
     */
    public void deleteTodo(Todo todo) {
        repository.deleteTodo(todo);
        failed = repository.failed;
    }
}
