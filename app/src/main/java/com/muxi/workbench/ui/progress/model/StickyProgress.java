package com.muxi.workbench.ui.progress.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "stickyprogresses")
public final class StickyProgress {

    @PrimaryKey
    @ColumnInfo(name = "sid")
    private final int mSid;

    @ColumnInfo(name = "uid")
    private final int mUid;

    @ColumnInfo(name = "avatar")
    private final String mAvatar;

    @ColumnInfo(name = "username")
    private final String mUsername;

    @ColumnInfo(name = "time")
    private final String mTime;

    @ColumnInfo(name = "title")
    private final String mTitle;

    @ColumnInfo(name = "content")
    private final String mContent;

    @ColumnInfo(name = "ifLike")
    private final boolean mIfLike;

    @ColumnInfo(name = "commentCount")
    private final int mCommentCount;

    @ColumnInfo(name = "likeCount")
    private final int mLikeCount;


    public StickyProgress(int sid, int uid, String avatar, String username, String time, String title, String content, boolean ifLike, int commentCount, int likeCount) {
        this.mSid = sid;
        this.mUid = uid;
        this.mAvatar = avatar;
        this.mUsername = username;
        this.mTime = time;
        this.mTitle = title;
        this.mContent = content;
        this.mIfLike = ifLike;
        this.mCommentCount = commentCount;
        this.mLikeCount = likeCount;
    }

    @Ignore
    public StickyProgress(Progress progress) {
        this(progress.getSid(), progress.getUid(), progress.getAvatar(), progress.getUsername(),
                progress.getTime(), progress.getTitle(), progress.getContent(), progress.getIfLike(),
                progress.getCommentCount(), progress.getLikeCount());
    }

    public int getSid() {
        return mSid;
    }

    public int getUid() {
        return mUid;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getTime() {
        return mTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public boolean getIfLike() {
        return mIfLike;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public int getLikeCount() {
        return mLikeCount;
    }

}
