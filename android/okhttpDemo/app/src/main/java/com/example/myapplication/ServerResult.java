package com.example.myapplication;

import android.graphics.Bitmap;

// 存放服务器返回结果
public class ServerResult {
    public String PictureId;  // 图片名称
    public String ret;        // 结果
    public Bitmap bitmap;

    public static ServerResult serverResult = new ServerResult();
}
