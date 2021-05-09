package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class MyOkhttp {

    public static ServerResult uploadImage(String imagePath){
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            File file = new File(imagePath);
            RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", imagePath, image)
                    .build();
            Request request = new Request.Builder()
                    .url("http://39.96.52.220:80/upload")
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            ServerResult serverResult = new ServerResult();
            serverResult.PictureId = jsonObject.optString("filename");
            serverResult.ret       = jsonObject.optString("ret");
            return serverResult;
        }catch (Exception e){

        }
        return null;
    }


    public static Bitmap getBitmap(String PathName){
        try {
            OkHttpClient client = new OkHttpClient();

            //获取请求对象
            Request request = new Request.Builder().url("http://39.96.52.220:80/download?fileName="+PathName).build();

            //获取响应体

            ResponseBody body = client.newCall(request).execute().body();

            //获取流
            InputStream in = body.byteStream();
            //转化为bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        }catch (Exception e){

        }
        return null;
    }

}


