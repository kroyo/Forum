package com.kroyoshi.forum.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.kroyoshi.forum.BaseActivity;
import com.kroyoshi.forum.MainActivity;
import com.kroyoshi.forum.R;
import com.kroyoshi.forum.utils.AV;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoLayoutActivity extends BaseActivity {

    @Bind(R.id.profileIv)
    CircleImageView circleImageView;

    @Bind(R.id.btn_exit)
    MaterialRippleLayout materialRippleLayout;

    private TextView nameTv;

    String[] items = {"拍照", "本地"};
    private static final int PHOTO_NULL = 0;
    private static final int PHOTO_REQUEST_CAREMA = 1;//拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;//本地
    private static final int PHOTO_REQUEST_CUT = 3;//剪切完之后进行照片的上传
    private static final String PHOTO_FILE_NAME = "image/*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infolayout);

        ButterKnife.bind(this);
        setToolBar("个人信息");

        nameTv = (TextView) findViewById(R.id.nameTv);

        //获取用户的信息
        acceptUser();

        materialRippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //设置弹窗
                new AlertDialog.Builder(InfoLayoutActivity.this)
                        .setMessage("是否退出登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AVUser user=AVUser.getCurrentUser();
                                //退出
                                user.logOut();//清空缓存对象

                                //退出后，直接跳转到MainActivity页面
                                Intent intent=new Intent(InfoLayoutActivity.this,MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
            }
        });
    }

    @OnClick(R.id.profileLayout)
    void layout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //拍照
                if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(), "temp.jpg")));
                    startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
                }
                //本地e
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(PHOTO_FILE_NAME);
                    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                }
            }
        });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_NULL) return;
        //拍照后进行照片的剪切
        if (requestCode == PHOTO_REQUEST_CAREMA) {
            File file=new File( Environment.getExternalStorageDirectory(),"temp.jpg");
            crop(Uri.fromFile(file));
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            crop(data.getData());
        }
        if (requestCode == PHOTO_REQUEST_CUT) {
            final Bitmap phone = data.getParcelableExtra("data");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            phone.compress(Bitmap.CompressFormat.JPEG, 100, out);
            AVFile file = new AVFile("hand.jpg", out.toByteArray());
            file.saveInBackground();
            AVUser user = AV.getCurUser();
            user.put("avatar", file);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //上传成功，设置头像
                        circleImageView.setImageBitmap(phone);
                    }
                }
            });
        }
    }
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void acceptUser() {
        AVUser user = AV.getCurUser();
        //呢称显示
        if (user.getString("nickname") != null) {
            nameTv.setText(user.getString("nickname"));
        } else {
            nameTv.setText(user.getString("username"));
        }
        //头像显示
        AVFile file = user.getAVFile("avatar");
        if (file != null) {
            Glide.with(this)
                    .load(file.getThumbnailUrl(false, 80, 80))
                    .into(circleImageView);
        }
    }
}
