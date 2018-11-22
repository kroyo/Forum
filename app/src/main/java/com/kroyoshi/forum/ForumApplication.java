package com.kroyoshi.forum;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.kroyoshi.forum.utils.AV;

/**
 * Created by Administrator on 2016-10-12.
 * LeanCloud云服务器配置文件
 */
public class ForumApplication extends Application
{
    private static String APPID = "ql5VeojWhWltExe4RNeYRVLt-gzGzoHsz";
    private static String APPKEY = "I9qfbUNor1wjfnL0Bhwf1BIk";

    @Override
    public void onCreate() {
        super.onCreate();

        //注册子类化 AVObject
        AV.registerSubclass();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,APPID,APPKEY);
    }
}
