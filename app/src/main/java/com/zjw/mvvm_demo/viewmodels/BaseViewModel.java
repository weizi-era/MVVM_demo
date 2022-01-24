package com.zjw.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    public LiveData<String> failed;
}
