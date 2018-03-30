package com.taoze.basic.app.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.taoze.basic.R;
import com.taoze.basic.app.bean.ChatInfo;
import com.taoze.basic.common.base.BaseCommonAdapter;

import java.util.List;

/**
 * ModuleAdapter
 * Created by Taoze on 2018/3/30.
 */

public class ModuleAdapter extends BaseCommonAdapter<ChatInfo>{

    public ModuleAdapter(Context context, List<ChatInfo> datas, int resourceId) {
        super(context, datas, resourceId);
    }

    @Override
    protected void bindView(int position, ViewHolder viewHolder) {
        ImageView picture = viewHolder.getView(R.id.mChatPictureIv);
        TextView nickName = viewHolder.getView(R.id.mChatNicknameTv);
        TextView lastMsg = viewHolder.getView(R.id.mChatLastMsgTv);
        TextView lastTime = viewHolder.getView(R.id.mChatLastTimeTv);
        ChatInfo info = mDatas.get(position);
        picture.setImageResource(info.picture);
        nickName.setText(info.nickname);
        lastMsg.setText(info.lastMsg);
        lastTime.setText(info.lastTime);
    }
}
