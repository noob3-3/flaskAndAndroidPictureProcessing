package com.example.myapplication;

import android.os.Environment;


/**
 *  文件总类
 *
 */
public class MyFile {
    /**
     *  得到本机文件目录
     *
     */
    public static String getPhonePath()
    {
        return Environment.getExternalStorageDirectory().getPath();
    }

}
