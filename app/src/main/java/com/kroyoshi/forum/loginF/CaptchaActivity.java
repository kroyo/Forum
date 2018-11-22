package com.kroyoshi.forum.loginF;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.okhttp.internal.framed.FrameReader;
import com.kroyoshi.forum.BaseActivity;
import com.kroyoshi.forum.MainActivity;
import com.kroyoshi.forum.R;
import com.kroyoshi.forum.utils.AV;
import com.kroyoshi.forum.utils.BaseUtils;
import com.kroyoshi.forum.utils.T;
import com.kroyoshi.forum.view.LodingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-10-14.
 * 手机验证码验证
 */

public class CaptchaActivity extends BaseActivity {

    @Bind(R.id.editCaptcha)
    EditText editCaptcha;
    @Bind(R.id.captchaButton)
    Button captchaButton;
    @Bind(R.id.nextBtn)
    Button nextBtn;
    @Bind(R.id.phontHint)
    TextView phontHint;

    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captch);
        ButterKnife.bind(this);

        setToolBar("手机号验证");

        BaseUtils.requestFocus(editCaptcha);

        handler.sendEmptyMessageDelayed(1, 1000);

        phontHint.setText("请输入" + AV.getCurUser().getMobilePhoneNumber() + "收到的短信验证码.");

        editCaptcha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String captcha = editCaptcha.getText().toString();
                if (captcha.length() == 6) {
                    nextBtn.setClickable(true);
                    nextBtn.setBackgroundResource(R.drawable.btn_bg);
                } else {
                    nextBtn.setClickable(false);
                    nextBtn.setBackgroundColor(Color.parseColor("#cccccc"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        captchaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.requestMobilePhoneVerifyInBackground(AV.getCurUser().getMobilePhoneNumber(), new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            countdown = 60;
                            handler.sendEmptyMessageDelayed(1, 1000);
                        } else {
                            AV.showError(e.getMessage(), mContext);
                        }
                    }
                });
            }
        });
    }

    //“下一步”按钮点击事件
    @OnClick(R.id.nextBtn)
    void nextBtn(){

        String  captcha=editCaptcha.getText().toString().trim();

        if (!BaseUtils.isNetworkAvailable(this)) {
            T.show(mContext, "请检查您的网络……");
            return;
        }

        lodingDialog = new LodingDialog();
        lodingDialog.show(this);

        AVUser.verifyMobilePhoneInBackground(captcha, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                lodingDialog.dismiss();
                if (e == null) {
                    T.show(mContext, "注册成功!");
                    finish();
                } else {
                    AV.showError(e.getMessage(), mContext);
                }
            }
        });
    }

    //发送验证码按钮倒计时
    private int countdown=60;
    Handler handler=new Handler(){

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                countdown -= 1;
                if (countdown == 0) {
                    captchaButton.setText("获取验证码");
                    captchaButton.setClickable(true);
                    captchaButton.setBackgroundResource(R.drawable.btn_drawable_bg);
                } else {
                    captchaButton.setText("已发送(" + countdown + "s)");
                    captchaButton.setBackgroundColor(Color.parseColor("#cccccc"));
                    captchaButton.setClickable(false);
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
            super.handleMessage(msg);
        }
    };
}
