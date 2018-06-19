package com.example.gaminglife;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TaskContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_content);
        //从上一个活动中接收数据并显示
        TextView textView=(TextView)findViewById(R.id.goal_content_text);
        Intent intent=getIntent();
        String data=intent.getStringExtra("extra_data_task");
        textView.setText(data);
    }
}
