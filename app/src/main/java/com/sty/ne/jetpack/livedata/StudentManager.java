package com.sty.ne.jetpack.livedata;

import androidx.lifecycle.MutableLiveData;

/**
 * @Author: tian
 * @UpdateDate: 2020/11/5 9:18 PM
 */
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
