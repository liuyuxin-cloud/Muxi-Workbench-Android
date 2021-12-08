package com.muxi.workbench.ui.home.model;

import android.util.Log;

import com.muxi.workbench.commonUtils.net.NetUtil;
import com.muxi.workbench.ui.login.model.UserWrapper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RemoteDataSource {
    private static final String token = UserWrapper.getInstance().getToken();

    public static void getAllFeedFromRemote(FeedRepository.LoadStatusBeanCallback callback, int limit, int last_id) {

        final Disposable[] mDisposable = new Disposable[1];
        NetUtil.getInstance().getApi().getFeed(token, limit, last_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable[0] = d;
                    }

                    @Override
                    public void onNext(FeedBean feedBean) {
                        Log.e("TAG", "RemoteDataSource onNext");
                        Log.e("TAG", "feedbean" + feedBean.toString());
                        callback.onDataLoaded(feedBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                        Log.e("TAG", "RemoteDataSource onError" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "RemoteDataSource onComplete");
                        callback.onComplete();
                    }
                });
    }



}
