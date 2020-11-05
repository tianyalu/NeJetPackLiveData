package com.sty.ne.jetpack.livedata;

import androidx.lifecycle.MutableLiveData;

/**
 * @Author: tian
 * @UpdateDate: 2020/11/5 10:04 PM
 */
public class ServerRequest {
    private MutableLiveData<String> responseResult;

    private static class LazyHolder {
        private static ServerRequest instance = new ServerRequest();
    }

    private ServerRequest() {
        responseResult = new MutableLiveData<>();
    }

    public static ServerRequest getInstance() {
        return LazyHolder.instance;
    }

    //对外暴露
    public MutableLiveData<String> getResponseResult() {
        return responseResult;
    }

    //模拟请求服务器
    public void requestLoginAction(String name, String pwd) {
        if(ServerData.NAME.equals(name) && ServerData.PWD.equals(pwd)) {
            getResponseResult().setValue("恭喜，登录成功...");
        }else {
            getResponseResult().setValue("登录失败！");
        }
    }
}
