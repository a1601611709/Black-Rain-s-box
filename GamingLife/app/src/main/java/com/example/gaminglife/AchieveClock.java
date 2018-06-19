package com.example.gaminglife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.exceptions.DataSupportException;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class AchieveClock extends AppCompatActivity {

    private List<Incident> incidents;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve_clock);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        incidents=new ArrayList<Incident>();
        loading();
    }

    public void loading(){
        Connector.getDatabase();
        //显示全部任务
        //删除所有数据
        //DataSupport.deleteAll(Incident.class);
        //incidents= DataSupport.findAll(Incident.class);

        //显示未完成任务
        try{
            incidents=DataSupport.where("finish = ?","1").find(Incident.class);
            //显示列表并监听点击列表中的数据，然后做出对应的Intent
            ClockAdapter adapter=new ClockAdapter(AchieveClock.this,R.layout.clock_item,incidents);
            listView=(ListView)findViewById(R.id.list_achieve_clock);
            listView.setAdapter(adapter);
        }catch(DataSupportException e){
            Toast.makeText(AchieveClock.this, "No Clock！", Toast.LENGTH_SHORT).show();
        }

    }
}
