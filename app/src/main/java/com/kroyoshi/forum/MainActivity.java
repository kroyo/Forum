package com.kroyoshi.forum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kroyoshi.forum.adapter.ArticleAdapter;
import com.kroyoshi.forum.entity.mArticle;
import com.kroyoshi.forum.loginF.LoginActivity;
import com.kroyoshi.forum.user.InfoLayoutActivity;
import com.kroyoshi.forum.utils.AV;
import com.kroyoshi.forum.utils.BaseUtils;
import com.kroyoshi.forum.utils.L;
import com.kroyoshi.forum.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.nav_view)
    NavigationView navView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.ArticleRv)
    RecyclerView articleRv;

    private ArticleAdapter articleAdapter;

    private List<mArticle> articleList;

    private List<AVObject>  aList=new ArrayList<>();;



    //定义最后点击按钮的事件
    private long lastBackTime = 0;

    public static Activity instance;

    private View headerView;
    private TextView nameTv;
    private CircleImageView profileIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        //BaseActivity  中 this 赋值
        instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化
        initUserView();

        //初始化文章数据
        initArticleRv();

        //下来刷新
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadArticleData();
            }
        });

        //主界面悬浮图标按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /****随着list的向下滚动而隐藏，向上滚动而重现****/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AV.getCurUser()!=null){
                    startActivity(new Intent(instance,PublishActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }else{
                    T.show(instance,"您还未登录~");
                    Intent intent=new Intent(instance,LoginActivity.class);
                    startActivity(intent);
                    return;
                }
            }
        });

        //侧滑点击图标加载
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                updateUser();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    //侧滑的初始化
    private void initUserView()
    {
        headerView = navView.getHeaderView(0);
        nameTv = (TextView) headerView.findViewById(R.id.nameTv);
        profileIv= (CircleImageView) headerView.findViewById(R.id.profileIv);

        updateUser();
    }


    private void updateUser() {

        //获取用户信息
        AVUser curUser= AV.getCurUser();

        if(curUser==null){
            headerView.findViewById(R.id.userLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }else{
            if(curUser.getString("nickname")!=null){
                nameTv.setText(curUser.getString("nickname"));
            }else{
                nameTv.setText(curUser.getString("username"));
            }

            //头像显示
            AVFile pic=curUser.getAVFile("avatar");

            if(pic!=null){
                Glide.with(this)
                        .load(pic.getThumbnailUrl(false,80,80))
                        .into(profileIv);
            }

            //点击头像和昵称的相应事件
            headerView.findViewById(R.id.userLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(instance,InfoLayoutActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            });
        }
    }

    private void initArticleRv() {

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.coloryellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadArticleData();
            }
        });

    }

    private void setLoadAdapter() {

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);

        articleRv.setHasFixedSize(true);
        articleRv.setNestedScrollingEnabled(false);
        articleRv.setLayoutManager(linearLayoutManager);

        articleAdapter=new ArticleAdapter(this,aList);

        articleRv.setAdapter(articleAdapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    //加载所有文章数据
    private void loadArticleData(){
        if(!BaseUtils.isNetworkAvailable(this)){
            swipeRefreshLayout.setRefreshing(false);
            T.show(this,"你的网络出小差了……");
            return;
        }

        //AVQuery<mArticle> articleQuery= AVObject.getQuery(mArticle.class);
        AVQuery<AVObject> articleQuery=new AVQuery<>("mArticle");
        articleQuery.orderByDescending("createdAt");
        articleQuery.include("user");
        articleQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if(e==null){
                    aList.addAll(list);
                    setLoadAdapter();
                }else{
                    L.e(e.getCode() + "：" + e.getMessage());
                }
            }
        });
    }



    //侧边栏点击事件
    @Override
    public void onClick(View v) {

        if (BaseUtils.isFastClick()) {
            return;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        Intent intent = null;
        //验证是否登录
        if(AV.getCurUser()==null){
            T.show(this,"您还未登录~");
            intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            return;
        }

        switch (v.getId()) {
            case R.id.mArticleLayout:
                intent = new Intent(this, InfoLayoutActivity.class);
                break;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    //返回按钮点击事件
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long currentBackTime= System.currentTimeMillis();
            if(currentBackTime-lastBackTime>2*1000)
            {
                T.show(this, "再按一次返回键退出");
                lastBackTime = currentBackTime;
            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //标题栏点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
