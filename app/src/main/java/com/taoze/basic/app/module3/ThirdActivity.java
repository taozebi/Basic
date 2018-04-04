package com.taoze.basic.app.module3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.DemoApplication;
import com.taoze.basic.common.base.CommonActivity;
import com.taoze.basic.common.view.TitleBar;

/**
 * Created by Taoze on 2018/4/2.
 */

public class ThirdActivity extends Activity {

    private TextView title;
    private Button backBtn;
    private Button submitBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        title = (TextView) findViewById(R.id.titleText);
        backBtn = (Button)findViewById(R.id.titleBackBtn);
        submitBtn = (Button)findViewById(R.id.titleSubmitBtn);

        title.setText("ThirdActivity");
        submitBtn.setText("setResult");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DemoApplication.TAG, "ThirdActivity back button clicked");
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DemoApplication.TAG, "ThirdActivity setResult button clicked");
                Intent result = new Intent();
                result.putExtra("data", "ThirdActivity set result data: this is ThirdActivity's data 1131313113313");
                setResult(0, result);
                ThirdActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("module3")) {
            T.showShort(this, intent.getStringExtra("module3"));
        }
    }
}
