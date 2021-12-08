package com.muxi.workbench.ui.home.model;

public class FeedRepository {

    public void getAllData(int limit, int last_id, LoadStatusBeanCallback loadStatusBeanCallback) {
        RemoteDataSource.getAllFeedFromRemote(loadStatusBeanCallback, limit, last_id);
    }

    public interface LoadStatusBeanCallback {

        void onDataLoaded(FeedBean mBean);

        void onDataNotAvailable();

        void onComplete();
    }
}
