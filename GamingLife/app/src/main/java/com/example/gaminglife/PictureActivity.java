package com.example.gaminglife;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Intent intent=getIntent();
        int id=intent.getIntExtra("pitcureId_1",0);
        if(id==0){
            id=intent.getIntExtra("pitcureId_2",0);
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
        ImageView imageView=(ImageView)findViewById(R.id.image123);
        imageView.setBackgroundResource(id);
    }
}
