package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

public class MainActivity extends AppCompatActivity {


    public RelativeLayout TakePhoto;   // 拍照上传
    public RelativeLayout PhotoAlbum;  // 相册上传

    public static int flag = 0;

    public CircleProgressDialog circleProgressDialog;


    // 显示服务器返回结果
    public Handler ShowResult = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            circleProgressDialog.dismiss();
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UIOperation.SetFullScreen(this);
        // 权限申请
        PermissionUtils.applicationPermissions(this, new PermissionUtils.PermissionListener() {
            @Override
            public void onSuccess(Context context) {
                initView();
                initOnClickListener();
            }

            @Override
            public void onFailed(Context context) {
                if (AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.CAMERA)
                        && AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.STORAGE)) {
                    AndPermission.with(context).runtime().setting().start();
                }
                Toast.makeText(context, context.getString(R.string.permission_camra_storage), Toast.LENGTH_SHORT);

                finish();
            }
        }, Permission.Group.STORAGE, Permission.Group.CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag != 0){
            circleProgressDialog = new CircleProgressDialog(MainActivity.this);
            circleProgressDialog.setText("正在加载中...");
            circleProgressDialog.showDialog();
            // 发送至服务器
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerResult serverResult = MyOkhttp.uploadImage(MyFile.getPhonePath()+"/"+"DCIM/"+"WaitSend.jpg");
                    // 向服务器请求图片结果
                    Bitmap bitmap = MyOkhttp.getBitmap(serverResult.PictureId);
                    ServerResult.serverResult.bitmap = bitmap;
                    ServerResult.serverResult.ret = serverResult.ret;
                    ShowResult.sendMessage(new Message());
                    flag = 0;
                }
            }).start();
        }else {

        }
    }

    private void initView() {
        TakePhoto  = findViewById(R.id.take_photo);
        PhotoAlbum = findViewById(R.id.photo_album);
    }

    private void initOnClickListener() {

        TakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });


        PhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                } else {
                    //已授权，获取照片
                    choosePhoto();
                }
            }
        });

    }


    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                Uri uri = data.getData();
                String filePath = FileUtil.getFilePathByUri(this, uri);
                circleProgressDialog = new CircleProgressDialog(MainActivity.this);
                circleProgressDialog.setText("正在加载中...");
                circleProgressDialog.showDialog();
                // 发送至服务器
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ServerResult serverResult = MyOkhttp.uploadImage(filePath);
                        // 向服务器请求图片结果
                        Bitmap bitmap = MyOkhttp.getBitmap(serverResult.PictureId);
                        ServerResult.serverResult.bitmap = bitmap;
                        ServerResult.serverResult.ret = serverResult.ret;
                        ShowResult.sendMessage(new Message());
                    }
                }).start();
                break;
        }
    }

}