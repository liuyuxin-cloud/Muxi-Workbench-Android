package com.muxi.workbench.ui.home;


import android.util.Log;

import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.home.model.FeedRepository;

public class HomePresenter implements HomeContract.Presenter {

    private FeedRepository mFeedRepository;
    private HomeContract.View mHomeView;
    private final int limit = 10;
    private int last_id = -1;

    public HomePresenter(FeedRepository feedRepository, HomeContract.View homeView) {
        mFeedRepository = feedRepository;
        mHomeView = homeView;

        mHomeView.setPresenter(this);

    }

    @Override
    public void start() {
        loadAllData(false);
    }

    @Override
    public void loadAllData(boolean isRefresh) {
        mFeedRepository.getAllData(limit,last_id, new FeedRepository.LoadStatusBeanCallback() {
            @Override
            public void onDataLoaded(FeedBean mBean) {
                if (isRefresh)
                    mHomeView.showAllData(mBean);
                else
                    mHomeView.initAdapter(mBean);//isRefresh false 表示第一次请求
            }

            @Override
            public void onDataNotAvailable() {
                mHomeView.setLoadingIndicator(false, false);
                mHomeView.setEmpty();
            }

            @Override
            public void onComplete() {
                mHomeView.setLoadingIndicator(false, true);
            }
        });
    }

    @Override
    public void addItem(FeedBean itemData) {
        mHomeView.addItem(itemData);
    }

    @Override
    public void refresh() {
        mFeedRepository.getAllData(limit, last_id, new FeedRepository.LoadStatusBeanCallback() {
            @Override
            public void onDataLoaded(FeedBean mBean) {

                mHomeView.setLoadingIndicator(false, true);
            }

            @Override
            public void onDataNotAvailable() {

                mHomeView.setLoadingIndicator(false, false);

            }

            @Override
            public void onComplete() {
                Log.e("TAG", "HomePresenter onComplete");
                mHomeView.setLoadingIndicator(false, true);
            }
        });
    }

    @Override
    public void loadMore() {
        mFeedRepository.getAllData(limit, last_id, new FeedRepository.LoadStatusBeanCallback() {
            @Override
            public void onDataLoaded(FeedBean mBean) {
                addItem(mBean);
            }

            @Override
            public void onDataNotAvailable() {
                mHomeView.showLoadMoreSign(false);
            }

            @Override
            public void onComplete() {
                mHomeView.showLoadMoreSign(true);
            }
        });
    }
}
