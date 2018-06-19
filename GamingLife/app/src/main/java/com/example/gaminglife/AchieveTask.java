package com.example.gaminglife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class AchieveTask extends AppCompatActivity {

    private List<Goal> goals;

    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve_task);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        goals = new ArrayList<Goal>();
        loading();
    }

    public void loading() {
        Connector.getDatabase();
//        DataSupport.deleteAll(StageGoal.class);
//        DataSupport.deleteAll(Goal.class);
        //显示全部任务
        //goals= DataSupport.findAll(Goal.class);

        //显示未完成任务
        goals = DataSupport.where("achieve = ?", "1").find(Goal.class);
        //显示列表并监听点击列表中的数据，然后做出对应的Intent
        TaskAdapter adapter = new TaskAdapter(AchieveTask.this, R.layout.text_view, goals);
        lv = (ListView) findViewById(R.id.achieve_task_list_view);
        lv.setAdapter(adapter);
    }
}
