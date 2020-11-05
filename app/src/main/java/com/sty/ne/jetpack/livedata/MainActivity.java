package com.sty.ne.jetpack.livedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvText;
    private Button btnLoginTest;
    private Button btnLoginRequest;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //感应 观察 监听
        StudentManager.getInstance().getLiveDataStr().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvText.setText(s);
            }
        });
    }

    private void initView() {
        tvText = findViewById(R.id.tv_text);
        btnLoginTest = findViewById(R.id.btn_login_test);
        btnLoginRequest = findViewById(R.id.btn_login_request);
        mHandler = new Handler();

        btnLoginTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转之前触发一次
                //先触发
                StudentManager.getInstance().getLiveDataStr().setValue("天涯路");
                startActivity(new Intent(MainActivity.this, LoginTestActivity.class));
            }
        });

        btnLoginRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginRequestActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //触发
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StudentManager.getInstance().getLiveDataStr().setValue("88");
            }
        }, 3000);
    }
}