package com.kroyoshi.forum.loginF;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.EasyEditSpan;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.kroyoshi.forum.BaseActivity;
import com.kroyoshi.forum.R;
import com.kroyoshi.forum.utils.AV;
import com.kroyoshi.forum.utils.BaseUtils;
import com.kroyoshi.forum.utils.T;
import com.kroyoshi.forum.view.LodingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    //加载控件
    @Bind(R.id.editPhone)
    EditText editPhone;
    @Bind(R.id.reditPassword)
    EditText editPassword;

    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        setToolBar("用户注册");

        BaseUtils.requestFocus(editPhone);
    }

    @OnClick(R.id.nextBtn)
    void nextBtn(){

        String phone = editPhone.getText().toString().trim();
        //验证手机号是否正确
        if(!BaseUtils.isMobileNO(phone)){
            BaseUtils.requestFocus(editPhone);
            return ;
        }

        String password = editPassword.getText().toString();
        //验证密码输入框是否为空
        if(password.equals("")){
            BaseUtils.requestFocus(editPassword);
            return;
        }

        if(!BaseUtils.isNetworkAvailable(this)){
            T.show(this,"请检查你的网络……");
            return;
        }

        lodingDialog = new LodingDialog();

        lodingDialog.show(this);

        AVUser user = new AVUser();

        user.setUsername(phone);

        user.setPassword(password);

        user.put("mobilePhoneNumber", phone);

        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                lodingDialog.dismiss();
                if (e == null) {
                    startActivity(new Intent(mContext, CaptchaActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    finish();
                } else {
                    AV.showError(e.getMessage(),mContext);
                }
            }
        });
    }
}
