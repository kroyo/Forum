package com.kroyoshi.forum.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.kroyoshi.forum.R;
import com.kroyoshi.forum.entity.mArticle;
import com.kroyoshi.forum.loginF.LoginActivity;
import com.kroyoshi.forum.utils.AV;
import com.kroyoshi.forum.utils.L;
import com.kroyoshi.forum.utils.T;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016-10-18.
 * 主界面显示所有文章的动态加载设配器
 *
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private final List<AVObject> dataList;

    private Activity mContext;

    public ArticleAdapter(Activity context,List<AVObject> dataList){

        mContext=context;
        this.dataList=dataList;

    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {

        final AVObject artList= dataList.get(position);

        artList.fetchInBackground("user", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                AVObject Auser=avObject.getAVObject("user");

                //文章发表人
                String nickname=Auser.getString("nickname");
                if(nickname!=null){
                    holder.nikenameTv.setText("来自“"+nickname+"”的分享：");
                }else{
                    holder.nikenameTv.setText("来自“"+Auser.getString("username")+"”的分享：");
                }

                //头像显示
                AVFile pic=Auser.getAVFile("avatar");
                if(pic!=null){
                    Glide.with(mContext)
                            .load(pic.getThumbnailUrl(false,80,80))
                            .into(holder.profileIv);
                }
            }
        });

        holder.titleTv.setText(artList.getString("title"));

        //重要内容
        holder.contentTv.setText(artList.getString("content"));
        //文字过长，显示部分问题
        holder.contentTv.setOnClickListener(new View.OnClickListener() {
            Boolean flag=true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag=false;
                    holder.contentTv.setEllipsize(null);   //展开
                    holder.contentTv.setSingleLine(flag);
                }else{
                    flag=true;
                    holder.contentTv.setEllipsize(TextUtils.TruncateAt.END); //收缩
                    holder.contentTv.setSingleLine(flag);
                }
            }
        });

        //显示发表时间
          /**Date  装换 成  String**/
        Date date=artList.getDate("updatedAt");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String time=sdf.format(date);
        holder.createTime.setText(time);

        //收藏图标点击事件
        holder.btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AV.getCurUser()==null){
                    T.show(mContext,"您还未登录~");
                    Intent intent=new Intent(mContext,LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }else{
                    holder.btn_collect.setSelected(true);
                    AVObject avObject=new AVObject("Collect");
                    avObject.put("mArticleId",artList);
                    avObject.put("userId", AV.getCurUser());
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e==null){
                                T.show(mContext,"收藏成功");
                            }else{
                                L.e(e.getCode() + "：" + e.getMessage());
                            }
                        }
                    });
                }//end if
            }
        });//end onclick

        //评论图标点击事件
        holder.btn_comment.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.profileIv)
        CircleImageView profileIv;
        @Bind(R.id.nicknameTv)
        TextView nikenameTv;
        @Bind(R.id.titleTv)
        TextView titleTv;
        @Bind(R.id.contentTv)
        TextView contentTv;

        @Bind(R.id.createTime)
        TextView createTime;
        @Bind(R.id.btn_collect)
        TextView btn_collect;
        @Bind(R.id.btn_comment)
        TextView btn_comment;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

    public void updateList(List<AVObject> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        this.notifyDataSetChanged();
    }
}
