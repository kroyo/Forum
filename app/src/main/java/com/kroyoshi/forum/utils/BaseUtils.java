package com.kroyoshi.forum.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-10-14.
 * 各种注意事件
 */
public class BaseUtils {

    //弹出软键盘
    public static void popSoftkeyboard(Context ctx,View view,boolean wantPop) {
        InputMethodManager imm= (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(wantPop){
            view.requestFocus();
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }else {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    /**
     * EditText绑定焦点，并弹出键盘
     *
     * @param editText
     */
    public static  void requestFocus(EditText editText){

        editText.requestFocus();
        editText.setFocusableInTouchMode(true);
        InputMethodManager imm= (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
    }

    /**
     * 判断手机号码是否正确
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){

        Pattern p=Pattern.compile("^(1)\\d{10}$");

        Matcher m=p.matcher(mobiles);

        return m.matches();
    }

    //判断是否有网络
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //判断editText文本是否为空
    public static boolean editTextIsEmpty(EditText text) {
        return text.getText().toString().length() == 0;
    }

    /**
     * 防止多次点击事件
     */
        public static long mLastClickTime;

    public static boolean isFastClick(){

        //当前时间
        long currentTime=System.currentTimeMillis();

        //两次点击的时间差
        long time=currentTime-mLastClickTime;
        if(0<time && time <500){
            return true;
        }
        return false;
    }

}
