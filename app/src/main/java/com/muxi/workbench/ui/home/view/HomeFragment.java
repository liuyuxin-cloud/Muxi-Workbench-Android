package com.muxi.workbench.ui.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muxi.workbench.R;
import com.muxi.workbench.commonUtils.MyRefreshLayout;
import com.muxi.workbench.ui.home.HomeContract;
import com.muxi.workbench.ui.home.HomePresenter;
import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.home.model.FeedRepository;
import com.muxi.workbench.ui.progress.view.progressDetail.ProgressDetailActivity;

public class HomeFragment extends Fragment implements HomeContract.View {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FeedRepository mFeedRepository;
    private HomeContract.Presenter mPresenter;
    private FeedAdapter mAdapter;
    private ViewStub viewStub;
    private MyRefreshLayout mSwipeRefreshLayout;
    private Button mRetry;
    private FeedAdapter.ItemListener listener = new FeedAdapter.ItemListener() {
        @Override
        public void onNameClick() {
            //跳转到个人界面
            Toast.makeText(getContext(), "应该跳转到个人界面", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFileClick(int sid, String username, String avatar, String title) {
            Intent intent = ProgressDetailActivity.newIntent(getActivity(), sid, username, avatar,
                    false, title, -1);

            startActivity(intent);
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFeedRepository = new FeedRepository();
        mPresenter = new HomePresenter(mFeedRepository, this);
        Log.e("Fragment left cycle", ":onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Fragment left cycle", ":onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        Log.e("Fragment left cycle", ":onResume");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = root.findViewById(R.id.home_toolbar);
        recyclerView = root.findViewById(R.id.home_rcv);

        viewStub = root.findViewById(R.id.home_view_stub);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this::refreshData);

        mSwipeRefreshLayout.setOnClickListener(view -> {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mSwipeRefreshLayout.setOnLoadMoreListener(() -> {
            mPresenter.loadMore();
            mSwipeRefreshLayout.setLoading(false);
        });

        initToolbar();
        initRv();
        return root;
    }


    private void initRv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void initAdapter(FeedBean mBean) {
        Log.e("TAG", "Home initAdapter");
        mAdapter = new FeedAdapter(mBean, mPresenter, listener);
        recyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.home_toolbar_item);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_scan_code:
                    //todo: scan code
                    break;
                case R.id.home_add:
                    //todo: add progress
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addItem(FeedBean nextPage) {
        mAdapter.addData(nextPage);
    }

    @Override
    public void showAllData(FeedBean feedBean) {
        mAdapter.replaceData(feedBean);
    }

    @Override
    public void setEmpty() {
        Log.e("TAG", "HomeFragment setEmpty");
        View view;
        try {
            view = viewStub.inflate();
            mRetry = view.findViewById(R.id.item_false_retry);
        } catch (Exception e) {

            viewStub.setVisibility(View.VISIBLE);
        } finally {
            if (mRetry != null) {
                mRetry.setOnClickListener(view1 -> {
                    mPresenter.loadAllData(true);
                    Toast.makeText(getContext(), "i'm trying!", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }


    @Override
    public void setLoadingIndicator(boolean loadingIndicator, boolean isSucceed) {

        mSwipeRefreshLayout.setRefreshing(loadingIndicator);

        if (!isSucceed) {
            Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void refreshData() {
        viewStub.setVisibility(View.GONE);
        mPresenter.loadAllData(true);
    }

    @Override
    public void showLoadMoreSign(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getContext(), "加载成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
        }
    }


}
