package com.zjw.mvvm_demo.network.base;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(@NotNull Disposable d) {

    }

    @Override
    public void onNext(@NotNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NotNull Throwable e) {
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T t);
    protected abstract void onFailure(Throwable e);
}
