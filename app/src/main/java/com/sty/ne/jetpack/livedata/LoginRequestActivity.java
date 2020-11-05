package com.sty.ne.jetpack.livedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginRequestActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etPwd;
    private Button btnLogin;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_request);

        initView();
        ServerRequest.getInstance().getResponseResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvResult.setText(s);
            }
        });
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        tvResult = findViewById(R.id.tv_result);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnLoginClicked();
            }
        });
    }

    private void onBtnLoginClicked() {
        String name = etName.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        ServerRequest.getInstance().requestLoginAction(name, pwd);
    }
}