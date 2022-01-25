package com.zjw.mvvm_demo.repository;

import androidx.lifecycle.MutableLiveData;

import com.zjw.mvvm_demo.BaseApplication;
import com.zjw.mvvm_demo.db.bean.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public final MutableLiveData<String> failure = new MutableLiveData<>();

    private static volatile UserRepository mInstance;

    public static UserRepository getInstance() {
        if (mInstance == null) {
            synchronized (UserRepository.class) {
                if (mInstance == null) {
                    mInstance = new UserRepository();
                }
            }
        }
        return mInstance;
    }

    public MutableLiveData<User> getUser() {
        Flowable<List<User>> listFlowable = BaseApplication.getDatabase().userDao().getAll();
        CustomDisposable.addDisposable(listFlowable, users -> {
            if (users.size() > 0) {
                for (User user : users) {
                    if (user.getUid() == 1) {
                        userMutableLiveData.postValue(user);
                        break;
                    }
                }
            } else {
                failure.postValue("你还没有注册过吧，去注册吧~");
            }
        });

        return userMutableLiveData;
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    public void updateUser(User user) {
        Completable update = BaseApplication.getDatabase().userDao().update(user);
        CustomDisposable.addDisposable(update, () -> {
            failure.postValue("200");
        });
    }

    /**
     * 保存用户数据
     */
    public void saveUser(User user) {
        Completable deleteAll = BaseApplication.getDatabase().userDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            //保存到数据库
            Completable insertAll = BaseApplication.getDatabase().userDao().insert(user);
            //RxJava处理Room数据存储
            CustomDisposable.addDisposable(insertAll, () -> failure.postValue("200"));
        });
    }
}
