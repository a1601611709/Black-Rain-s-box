package com.example.gaminglife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ClockContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_content);

        //显创建一个TextView
        TextView textView=(TextView)findViewById(R.id.clock_content_text);

        //接受从上一个活动接收来的数据并显示
        Intent intent2=getIntent();
        String data2 =intent2.getStringExtra("extra_data_clock");
        if(data2!=null){
            textView.setText(data2);
        }
        else{
            data2=intent2.getStringExtra("notification_clock_data");
            textView.setText(data2);
        }
    }
}
