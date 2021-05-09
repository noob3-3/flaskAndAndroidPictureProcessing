package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {


    public SurfaceView     surfaceView;
    public ImageView       takePhoto;
    public SurfaceHolder   mHolder;
    public Camera          mCamera;
    private Camera.AutoFocusCallback mAutoFocusCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        UIOperation.SetFullScreen(this);
        initView();

        initOnClickListener();

    }


    private void initView() {
        surfaceView = findViewById(R.id.surface);
        takePhoto   = findViewById(R.id.takephoto);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new SurfaceCallback());

    }


    private void initOnClickListener() {

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.flag = 1;
                mCamera.takePicture(null,null,new jpegCallBack());
            }
        });

    }


    // 打开相机
    public void openCamera(int direction)
    {
        try{
            mCamera = Camera.open(direction);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对
            mCamera.cancelAutoFocus();
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mHolder);

            // 相机竖屏显示
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class jpegCallBack implements Camera.PictureCallback{

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mCamera.cancelAutoFocus();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
            bitmap = BitmapOperation.rotateBitmap(bitmap,90);
            BitmapOperation.savePicture(bitmap,MyFile.getPhonePath()+"/"+"DCIM/","WaitSend.jpg");
            finish();
        }
    }

    private final class SurfaceCallback implements SurfaceHolder.Callback{
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mHolder = holder;
            openCamera(0);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 释放相机资源
            if(mCamera!=null)
            {
                mCamera.release();
                mCamera = null;
            }
        }
    }


}
