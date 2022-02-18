package com.zjw.mvvm_demo.viewmodels;


import androidx.lifecycle.LiveData;

import com.zjw.mvvm_demo.bean.RecognitionResponse;
import com.zjw.mvvm_demo.repository.RecognitionRepository;

public class IdenViewModel extends BaseViewModel {

    public LiveData<RecognitionResponse> recognition;

    String accessToken;

    public void getNetworkRecognition(String url) {
        RecognitionRepository recognitionRepository = new RecognitionRepository();
        recognition = recognitionRepository.getNetworkRecognition(accessToken, url);
        failed = recognitionRepository.failure;
    }

    public void getAlbumRecognition(String imageBase64) {
        RecognitionRepository recognitionRepository = new RecognitionRepository();
        recognition = recognitionRepository.getAlbumRecognition(accessToken, imageBase64);
        failed = recognitionRepository.failure;
    }

    public void getToken() {
        RecognitionRepository recognitionRepository = new RecognitionRepository();
        accessToken = recognitionRepository.getAccessToken();
    }
}
