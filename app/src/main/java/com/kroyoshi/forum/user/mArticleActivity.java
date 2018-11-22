package com.kroyoshi.forum.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kroyoshi.forum.BaseActivity;
import com.kroyoshi.forum.R;

public class MArticleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        setToolBar("我的分享");
    }
}
