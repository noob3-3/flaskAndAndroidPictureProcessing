package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    public ImageView ResultImg;
    public TextView  Result;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        UIOperation.SetFullScreen(this);
        initView();


    }

    private void initView() {
        ResultImg = findViewById(R.id.result_pic);
        Result    = findViewById(R.id.result);

        ResultImg.setImageBitmap(ServerResult.serverResult.bitmap);
        Result.setText(ServerResult.serverResult.ret);
    }
}
