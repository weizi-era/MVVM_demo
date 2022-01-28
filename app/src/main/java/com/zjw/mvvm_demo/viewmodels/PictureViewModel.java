package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zjw.mvvm_demo.db.bean.WallPaper;
import com.zjw.mvvm_demo.repository.PictureRepository;

import java.util.List;

public class PictureViewModel extends BaseViewModel {

    public LiveData<List<WallPaper>> wallPaper;

    public void getWallPaper() {
        wallPaper = new PictureRepository().getWallPaper();

    }
}
