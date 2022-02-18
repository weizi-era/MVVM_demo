package com.zjw.mvvm_demo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.db.bean.Notebook;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class NotebookRepository {

    private static final String TAG = NotebookRepository.class.getSimpleName();

    public MutableLiveData<Notebook> notebookLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<Notebook>> notebooksMutableLiveData = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    public final List<Notebook> emptyList = new ArrayList<>();

    public void addNotebook(Notebook notebook) {
        Completable insert = BaseApplication.getDatabase().notebookDao().insert(notebook);

        CustomDisposable.addDisposable(insert, () -> Log.d(TAG, "saveNotebook: 笔记数据保存成功"));
    }

    public MutableLiveData<List<Notebook>> getNotebooks() {
        Flowable<List<Notebook>> all = BaseApplication.getDatabase().notebookDao().getAll();
        CustomDisposable.addDisposable(all, notebooks -> {
            if (notebooks.size() > 0) {
                notebooksMutableLiveData.postValue(notebooks);
            } else {
                notebooksMutableLiveData.postValue(emptyList);
                failed.postValue("暂无数据");
            }
        });

        return notebooksMutableLiveData;
    }

    /**
     * 根据id获取笔记
     * @param uid id
     */
    public MutableLiveData<Notebook> getNotebookById(int uid) {
        Flowable<Notebook> flowable = BaseApplication.getDatabase().notebookDao().findById(uid);
        CustomDisposable.addDisposable(flowable, notebook -> {
            if (notebook != null) {
                notebookLiveData.postValue(notebook);
            } else {
                failed.postValue("未查询到笔记");
            }
        });
        return notebookLiveData;
    }

    /**
     * 更新笔记
     *
     * @param notebook
     */
    public void updateNotebook(Notebook notebook) {
        Completable update = BaseApplication.getDatabase().notebookDao().update(notebook);
        CustomDisposable.addDisposable(update, () -> {
            Log.d(TAG, "updateNotebook: " + "更新成功");
            failed.postValue("200");
        });
    }

    /**
     * 删除笔记
     */
    public void deleteNotebook(Notebook notebook) {
        Completable delete = BaseApplication.getDatabase().notebookDao().delete(notebook);
        CustomDisposable.addDisposable(delete, () -> {
            Log.d(TAG, "deleteNotebook: " + "删除成功");
            failed.postValue("200");
        });
    }



}
