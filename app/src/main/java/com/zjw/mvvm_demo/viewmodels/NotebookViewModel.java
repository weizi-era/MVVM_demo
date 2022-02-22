package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.repository.NotebookRepository;

import java.util.List;

public class NotebookViewModel extends BaseViewModel {

    public LiveData<List<Notebook>> notebooks;
    NotebookRepository repository = new NotebookRepository();


    public void getNotebooks() {
        notebooks = repository.getNotebooks();
        failed = repository.failed;
    }

    /**
     * 删除笔记
     */
    public void deleteNotebook(Notebook... notebook) {
        repository.deleteNotebook(notebook);
        failed = repository.failed;
    }

    /**
     * 搜索笔记
     * @param input 输入内容
     */
    public void searchNotebook(String input) {
        notebooks = repository.searchNotebook(input);
        failed = repository.failed;
    }

}
