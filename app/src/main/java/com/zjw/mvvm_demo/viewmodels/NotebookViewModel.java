package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.repository.NotebookRepository;

import java.util.List;

public class NotebookViewModel extends BaseViewModel {

    public LiveData<List<Notebook>> notebooks;



    public void getNotebooks() {
        NotebookRepository repository = new NotebookRepository();
        notebooks = repository.getNotebooks();
        failed = repository.failed;
    }
}
