# JetPack之LiveData

[TOC]

## 一、概念

### 1.1 `LiveData`

`LiveData` 是一种可观察的数据存储器类。与常规的可观察类不同，`LiveData `具有生命周期感知能力，意指它遵循其他应用组件（如 `Activity`、`Fragment` 或 `Service`）的生命周期。这种感知能力可确保` LiveData` 仅更新处于活跃生命周期状态的应用组件观察者。

参考：[https://developer.android.google.cn/topic/libraries/architecture/livedata](https://developer.android.google.cn/topic/libraries/architecture/livedata)

### 1.2 `LiveData`优势

* 简单明确（只有触发和感应：一次触发，多个地方可以感应到触发的内容；先触发改变，后设置监听也可以收到通知）；
* 唯一追逐（观察多少次，便会触发多少次，非常明确，排查问题容易）；
* 具有生命感知能力，不会崩溃，**避免内存泄漏**；

## 二、实操

### 2.1 基础使用

`MainActivity`:

```java
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
```

`StudentManager`：

```java
public class StudentManager {
    private static volatile StudentManager studentManager;
    private MutableLiveData<String> liveDataStr;

    private StudentManager() {
        liveDataStr = new MutableLiveData<>();
    }

    public static StudentManager getInstance() {
        if(studentManager == null) {
            synchronized (StudentManager.class) {
                if(studentManager == null) {
                    studentManager = new StudentManager();
                }
            }
        }
        return studentManager;
    }

    public MutableLiveData<String> getLiveDataStr() {
        return liveDataStr;
    }
}
```

### 2.2 先触发，后监听也可以

`MainActivity`:

```java
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
    }
```

`LoginTestActivity`：

```java
public class LoginTestActivity extends AppCompatActivity {
    private static final String TAG = LoginTestActivity.class.getSimpleName();
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);

        tvText = findViewById(R.id.tv_text);

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
```

### 2.3 防止崩溃，避免内存泄漏

`MainActivity`:

```java
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
    }
```

`LoginTestActivity`：

```java
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

        StudentManager.getInstance().getLiveDataStr().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvText.setText(s);
                Log.d(TAG, "Login 观察/监听到改变");
            }
        });
    }
}
```

### 