package com.sty.ne.jetpack.livedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LoginTestActivity extends AppCompatActivity {
    private static final String TAG = LoginTestActivity.class.getSimpleName();
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);

        tvText = findViewById(R.id.tv_text);

        //一打开LoginActivity马上关闭，如果是其他的库，应该会崩溃
        //为什么LiveData不会崩溃
        //因为LiveData与Lifecycle进行了关联，就意味着LiveData也具备生命周期的感应，也就意味着它知道生命周期的执行情况
        // 如果finish了，LiveData就不会再去（感应 观察 监听）
        finish();

        //后设置监听也能监听到
        //感应 观察 监听
        StudentManager.getInstance().getLiveDataStr().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvText.setText(s);
                Log.d(TAG, "Login 观察/监听到改变");
            }
        });
    }
}