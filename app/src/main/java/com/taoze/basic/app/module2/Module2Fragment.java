package com.taoze.basic.app.module2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.taoze.basic.R;
import com.taoze.basic.common.base.BaseFragment;
import com.taoze.basic.common.widget.ActionSheetDialog;
import com.taoze.basic.common.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.taoze.basic.common.widget.ActionSheetDialog.SheetItemColor;
import com.taoze.basic.common.widget.AlertDialog;

import butterknife.OnClick;

/**
 * Two
 * Created by Taoze on 2018/3/26.
 */
public class Module2Fragment extends BaseFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module2,null);
    }

    @Override
    protected void onInitData() {
        setTitleText("Module 2");
        setSubmitBtnVisibility(false);
    }
    @OnClick({R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5})
    public void onClick(View v){
        switch (v.getId()){
        case R.id.btn1:
        new ActionSheetDialog(getActivity())
                .builder()
                .setTitle("清空消息列表后，聊天记录依然保留，确定要清空消息列表？")
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("清空消息列表", SheetItemColor.Red
                        , new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {
                                Toast.makeText(getActivity(),"clear message list",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
        break;

        case R.id.btn2:
        new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("发送给好友",
                        SheetItemColor.Blue,
                        new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

                            }
                        })
                .addSheetItem("转载到空间相册",
                        SheetItemColor.Blue,
                        new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

                            }
                        })
                .addSheetItem("上传到群相册",
                        SheetItemColor.Blue,
                        new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

                            }
                        })
                .addSheetItem("保存到手机",
                        SheetItemColor.Blue,
                        new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

                            }
                        }).show();
        break;
        case R.id.btn3:
        new ActionSheetDialog(getActivity())
                .builder()
                .setTitle("好友列表")
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("删除好友", SheetItemColor.Red
                        , new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

                            }
                        })
                .addSheetItem("增加好友", SheetItemColor.Blue
                        , new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

                            }
                        })
                .addSheetItem("备注", SheetItemColor.Blue
                        , new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {

                            }
                        }).show();
        break;
        //setNegativeButton相对setPositiveButton颜色要浅一些
        case R.id.btn4:
        new AlertDialog(getActivity())
                .builder()
                .setTitle("退出当前帐号")
                .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                .setPositiveButton("确认退出", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                }).show();
        break;
        case R.id.btn5:
        new AlertDialog(getActivity())
                .builder()
                .setTitle("错误信息")
                .setMsg("你的手机sd卡出现问题，建议删除不需要的文件，否则收不到图片和视频等打文件")
                .setPositiveButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                }).show();
        break;
    }
    }
}
