package com.kroyoshi.forum.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2016-10-18.
 * 文章子类
 */
@AVClassName(mArticle.Article_CLASS)
public class mArticle extends AVObject{

    public static final Creator CREATOR = AVObjectCreator.instance;
    static final String Article_CLASS = "mArticle";

    private static final String TITLE="title";
    private static final String CONTENT="content";

    public  String getTitle(){
        return getString("title");
    }
    public void setTitle(String title){
        put(TITLE,title);
    }

    public  String getContent(){
        return getString("content");
    }
    public void setContent(String content){
        put(CONTENT,content);
    }
}
