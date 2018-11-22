package com.kroyoshi.forum.user;

import android.os.Bundle;

import com.kroyoshi.forum.BaseActivity;
import com.kroyoshi.forum.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setToolBar("关于时空");
    }
}
