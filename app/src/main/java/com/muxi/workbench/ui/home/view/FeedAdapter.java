package com.muxi.workbench.ui.home.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxi.workbench.R;
import com.muxi.workbench.ui.home.HomeContract;
import com.muxi.workbench.ui.home.model.FeedBean;
import com.muxi.workbench.ui.progress.model.net.GetAStatusResponse;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailDataSource;
import com.muxi.workbench.ui.progress.model.progressDetail.ProgressDetailRemoteDataSource;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    private List<FeedBean.ListDTO> mDataList;
    private ItemListener mListener;
    private HomeContract.Presenter mPresenter;

    FeedAdapter(FeedBean feedBean, HomeContract.Presenter presenter, ItemListener listener) {
        mPresenter = presenter;
        mDataList = feedBean.getList();
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        return new VH(mLayoutInflater.inflate(R.layout.item_home_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_FOOTER) return;

        FeedBean.ListDTO mData = mDataList.get(position);
        FeedBean.ListDTO.UserDTO mUser = mData.getUser();
        FeedBean.ListDTO.SourceDTO mSource = mData.getSource();
        VH vh = (VH) holder;

        //设置分割线
        if (mData.getShowDivider()) vh.mSplitView.setVisibility(View.VISIBLE);
        else vh.mSplitView.setVisibility(View.GONE);
        vh.mSplitView.setTextDate(mData.getDate());
        vh.mSplitView.setTextSign(mData.getTime());

        vh.mHeadShot.setImageURI(mUser.getAvatarUrl());
        vh.mName.setText(mData.getUser().getName());
        vh.mProjectName.setText(mSource.getName());
        vh.mTime.setText(mData.getTime());
        vh.mStatus.setText(getObjectNameFromId(mData.getAction(),
                mSource.getKind(), mSource.getProjectName()));


        vh.mHeadShot.setOnClickListener(view -> mListener.onNameClick());
        vh.mContent.setOnClickListener(view -> {

            if (mSource.getKind() == 5) {
                ProgressDetailRemoteDataSource.getInstance().getProgressDetail(mSource.getId(), new ProgressDetailDataSource.LoadProgressCallback() {
                    @Override
                    public void onSuccessGet(GetAStatusResponse getAStatusResponse) {
                        mListener.onClickToFeed(mSource.getId(), getAStatusResponse.getUserName(),
                                getAStatusResponse.getAvatar(), mSource.getName());
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(view.getContext(), "失败了",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (mSource.getKind() == 6) {
                mListener.onClickToFeed(mSource.getId(), mUser.getName(),
                        mUser.getAvatarUrl(), mSource.getName());
            }
            Log.e("kind=", String.valueOf(mSource.getKind()));
            if (mSource.getKind() == 3) {

                mListener.onCliCkToFile(mSource.getId(), mSource.getName());
            }
        });
    }

    private String getObjectNameFromId(String action, int kind_id, String projectName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(action);
        switch (kind_id) {
            case 1:
                stringBuilder.append("了团队");
                break;
            case 2:
                stringBuilder.append("了项目");
                break;
            case 3:
                stringBuilder.append("了文档");
                break;
            case 4:
                stringBuilder.append("了文件");
                break;
            case 5://创建
            case 6://评论
                stringBuilder.append("了进度");
                break;
            default:
                break;
        }
//        if (!projectName.contains("noname"))
//            stringBuilder.append(projectName);
        stringBuilder.append(":");
        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //普通的显示状态的item
    private class VH extends RecyclerView.ViewHolder {

        SimpleDraweeView mHeadShot;
        TextView mName, mStatus, mProjectName, mTime;
        SplitView mSplitView;
        ConstraintLayout mContent;

        private VH(@NonNull View itemView) {
            super(itemView);
            mSplitView = itemView.findViewById(R.id.split_bar);
            mHeadShot = itemView.findViewById(R.id.head_shot);
            mContent = itemView.findViewById(R.id.item_content);
            mName = itemView.findViewById(R.id.item_name);
            mStatus = itemView.findViewById(R.id.item_status);
            mProjectName = itemView.findViewById(R.id.item_project_name);
            mTime = itemView.findViewById(R.id.item_time);
        }
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        TextView textView;

        private VHFooter(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.footer_text);
            textView.setOnClickListener(view -> mPresenter.loadMore());
        }
    }


    void replaceData(FeedBean feedBean) {
        mDataList.clear();
        mDataList = feedBean.getList();
        notifyDataSetChanged();
    }

    void addData(FeedBean feedBean) {
        int start = mDataList.size();
        mDataList.addAll(feedBean.getList());
        notifyItemRangeInserted(start, feedBean.getList().size());
    }


    interface ItemListener {

        void onNameClick();

        void onClickToFeed(int sid, String username, String avatar, String title);

        void onCliCkToFile(int objectId, String docName);
    }

}
