package com.kroyoshi.forum.loginF;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.kroyoshi.forum.BaseActivity;
import com.kroyoshi.forum.R;
import com.kroyoshi.forum.utils.BaseUtils;
import com.kroyoshi.forum.utils.Common;
import com.kroyoshi.forum.utils.T;
import com.kroyoshi.forum.view.LodingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{

    //加载控件
    @Bind(R.id.editName)
    EditText editName;
    @Bind(R.id.editPassword)
    EditText editPassword;

    private Context  mContext;

    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;

        ButterKnife.bind(this);

        setToolBar("用户登录");
    }

    //登录按钮点击事件
    @OnClick(R.id.loginBtn)
    void  loginButton() {

        //验证手机号是否正确
        String phone = editName.getText().toString().trim();
        if (!BaseUtils.isMobileNO(phone)) {
            BaseUtils.requestFocus(editName);
            T.show(mContext, "手机号输入有误!");
            return;
        }

        String password = editPassword.getText().toString();
        if (password.equals("")) {
            BaseUtils.requestFocus(editPassword);
            return;
        }

        if (!BaseUtils.isNetworkAvailable(this)) {
            return;
        }

        lodingDialog = new LodingDialog();
        lodingDialog.show(this);

        AVUser.logInInBackground(phone, password, new LogInCallback() {
            public void done(AVUser user, AVException e) {
                lodingDialog.dismiss();
                if (user != null) {
                    T.show(mContext, "登录成功!");
                    finish();
                } else {
                    // 登录失败
                    T.show(mContext, Common.ormErrorCode(e.getCode()));
                }
            }
        });
    }

    //注册按钮相应事件
    @OnClick(R.id.registerTv)
    void  registerBtn()
     {
         startActivity(new Intent(mContext,RegisterActivity.class));
         overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
         finish();
     }
}
