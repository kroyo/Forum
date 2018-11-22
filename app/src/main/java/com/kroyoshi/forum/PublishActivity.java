package com.kroyoshi.forum;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.kroyoshi.forum.utils.AV;
import com.kroyoshi.forum.utils.BaseUtils;
import com.kroyoshi.forum.utils.T;
import com.kroyoshi.forum.view.LodingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 文章发表 编写界面
 */

public class PublishActivity extends BaseActivity {

    @Bind(R.id.editTitle)
    EditText editTiltle;
    @Bind(R.id.editContent)
    EditText editContent;

    private  Context mContext;

    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ButterKnife.bind(this);
        mContext=this;

        setToolBar("撰写文章");

        BaseUtils.requestFocus(editTiltle);

    }

    //将撰写的文章添加到服务器中
    private void addArticle() {

        String title=editTiltle.getText().toString().trim();
        String content=editContent.getText().toString();

        if(title.equals("")){
            BaseUtils.requestFocus(editTiltle);
            return;
        }
        if(content.equals("")){
            BaseUtils.requestFocus(editContent);
            return;
        }

        //检测网络是否出错
        if (!BaseUtils.isNetworkAvailable(this)) {
            return;
        }

        lodingDialog = new LodingDialog();
        lodingDialog.show(this);


        AVObject addArticle=new AVObject("mArticle");
        addArticle.put("title",title);
        addArticle.put("content",content);
        addArticle.put("user", AV.getCurUser());

        addArticle.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    lodingDialog.dismiss();
                    T.show(mContext,"文章发表成功……");
                    finish();
                }else{
                    T.show(mContext,"文章发表失败，请检查网络……");
                    return;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id==R.id.home){

        }

        //发表按钮点击事件
        if(id==R.id.action_publish){
            addArticle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
