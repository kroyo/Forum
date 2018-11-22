package com.kroyoshi.forum.utils;

import android.content.Context;
import android.os.Message;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.kroyoshi.forum.entity.mArticle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-10-13.
 * LeanCloud 子类初始化
 */
public class AV {

    public static AVUser getCurUser() {
        return AVUser.getCurrentUser();
    }

    public  static void registerSubclass(){
        AVObject.registerSubclass(mArticle.class);
    }

    //验证数据的提交是否成功
    public  static void showError(String messageStr, Context mContext) {
        try{
            JSONObject message=new JSONObject(messageStr);
            T.show(mContext,message.getString("error"));
        }catch (JSONException e1){
            e1.printStackTrace();
        }
    }
}
