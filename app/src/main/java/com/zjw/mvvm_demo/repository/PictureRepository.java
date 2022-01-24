package com.zjw.mvvm_demo.repository;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.db.bean.WallPaper;

import java.util.List;

import io.reactivex.Flowable;

public class PictureRepository {

    private final MutableLiveData<List<WallPaper>> wallPaper = new MutableLiveData<>();

    public MutableLiveData<List<WallPaper>> getWallPaper() {
        Flowable<List<WallPaper>> listFlowable = BaseApplication.getDatabase().wallPaperDao().getAll();
        CustomDisposable.addDisposable(listFlowable, wallPaper::postValue);

        return wallPaper;
    }
}
