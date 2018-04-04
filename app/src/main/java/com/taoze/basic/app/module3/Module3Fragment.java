package com.taoze.basic.app.module3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.DemoApplication;
import com.taoze.basic.common.base.BaseFragment;

import butterknife.OnClick;

import static com.taoze.basic.app.DemoApplication.TAG;

/**
 * Created by Taoze on 2018/3/26.
 */
public class Module3Fragment extends BaseFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module3,null);
    }

    @Override
    protected void onInitData() {
        setTitleText("Module 2");
        setSubmitBtnVisibility(false);
    }

    @OnClick(R.id.startActivity)
    public void onClick(View v){
        Log.d(DemoApplication.TAG,"Module3Fragment startActivity button clicked");
        startActivity();
    }

    private void startActivity(){
        Intent intent = new Intent();
        intent.setClass(getActivity(),ThirdActivity.class);
        intent.putExtra("module3","Module3Fragment data : 33333333");
        startActivityForResult(intent,1001);
//        getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_exit);
    }
    @Override
    public void onActivityResultNestedCompat(int requestCode, int resultCode, Intent data) {
        super.onActivityResultNestedCompat(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == 0){
            if(data != null){
                T.showShort(getActivity(),"Module3Fragment requestCode: "+requestCode+"  ,resultCode: "+resultCode+"  ---> data: "+data.getStringExtra("data"));
                Log.d(TAG,"Module3Fragment requestCode: "+requestCode+"  ,resultCode: "+resultCode+"  ---> data: "+data.getStringExtra("data"));
            }else{
                Toast.makeText(getActivity(),"Module3Fragment onResult data is null",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"Module3Fragment onResult requestCode, resultCode is not match",Toast.LENGTH_SHORT).show();
        }
    }
}
