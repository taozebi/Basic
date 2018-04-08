package com.taoze.basic.app.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ewide.core.util.T;
import com.taoze.basic.app.module2.MessageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送 广播接收者
 * Created by Taoze on 2018/4/8.
 */

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "JPushReceiver --> onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "JPushReceiver --> onReceive - 收到了自定义消息："+bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            T.showShort(context,bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "JPushReceiver --> onReceive - 收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "JPushReceiver --> onReceive - 用户点击打开了通知");


            Map<String,String> map = new HashMap<String,String>();
            for (String key : bundle.keySet()) {
                if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                    if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                        continue;
                    }
                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it =  json.keys();
                        while (it.hasNext()) {
                            String myKey = it.next().toString();
                            map.put(myKey, json.getString(myKey));
                        }

                    } catch (JSONException e) {
                    }
                }
            }
            Log.d(TAG, "JPushReceiver --> Bundle - "+map.toString());
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, MessageActivity.class);  //自定义打开的界面
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle1 = new Bundle();
            bundle1.putString("taskId",map.get("taskId"));
            i.putExtras(bundle1);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
