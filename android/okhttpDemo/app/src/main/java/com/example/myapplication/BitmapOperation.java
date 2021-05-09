package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BitmapOperation {



    public static void savePicture(Bitmap bitmap, String Path, String FileName){
        try {
            String subForder = Path;
            File foder = new File(subForder);
            if (!foder.exists()) {
                foder.mkdirs();
            }
            File myCaptureFile = new File(subForder, FileName);
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }catch (Exception e)
        {
            System.out.println("错误:"+e.getMessage());
        }
    }


    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmp;
    }


    /**
     * ***********************************************************************
     *
     * @brief 根据路径返回图片
     * @parameter path 图片路径
     * @version v1.0.0
     * @author 张嘉皓
     * @date 2019/12/27
     * ----------------------------------------------------------------------
     * @修改时间:
     * @修改人+联系方式:
     * @说明(你为什么修改):
     * ***********************************************************************
     */
    public static Bitmap getBitmap2Path(String path)
    {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            return bitmap;
        }catch (FileNotFoundException exception)
        {
            Log.e("File","NotFound!");
        }
        return null;
    }

}
