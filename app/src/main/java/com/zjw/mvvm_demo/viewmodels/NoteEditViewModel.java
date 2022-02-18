package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.repository.NotebookRepository;

public class NoteEditViewModel extends BaseViewModel {

    public LiveData<Notebook> notebook;

    private final NotebookRepository repository = new NotebookRepository();

//    public NoteEditViewModel(NotebookRepository repository) {
//        this.repository = repository;
//    }

    public void addNotebook(Notebook notebook) {
        repository.addNotebook(notebook);
        failed = repository.failed;
    }


    /**
     * 根据Id搜索笔记
     */
    public void queryById(int uid) {
        failed = repository.failed;
        notebook = repository.getNotebookById(uid);
    }

    /**
     * 更新笔记
     */
    public void updateNotebook(Notebook notebook) {
        failed = repository.failed;
        repository.updateNotebook(notebook);
    }

    /**
     * 删除笔记
     */
    public void deleteNotebook(Notebook notebook) {
        repository.deleteNotebook(notebook);
        failed = repository.failed;
    }
}
