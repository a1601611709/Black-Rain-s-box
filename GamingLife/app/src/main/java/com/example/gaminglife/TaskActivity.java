package com.example.gaminglife;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private List<Goal> goals;

    private ListView lv;

    SQLiteDatabase db;

    FloatingActionButton fab;//悬浮按钮

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.item_clock_menu,menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        try{
            loading();
            this.registerForContextMenu(lv);
        }catch(Exception e){
            Log.d("TaskActivity.this",e.toString());
        }

        //显示列表并创建点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goal goal=goals.get(position);
                goal.setStageGoal(goal.getSubTaskData());
                Intent intent=new Intent(TaskActivity.this,TaskContent.class);
                intent.putExtra("extra_data_task",goal.toParticularString());
                startActivity(intent);
            }
        });


        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                switch (view.getId()) {
                    case R.id.fab:
                        PopupMenu popupMenu = new PopupMenu(TaskActivity.this, view);
                        getMenuInflater().inflate(R.menu.pop_item, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.action_add:
                                        Intent addTask=new Intent(TaskActivity.this,AddTask.class);
                                        startActivity(addTask);
                                        break;
                                }

                                return false;
                            }
                        });
                        popupMenu.show();
                        Log.e("****--->", "float");
                        break;
                }
            }
        });
        //悬浮按钮

    }

    public boolean onContextItemSelected(MenuItem item){
        final AdapterView.AdapterContextMenuInfo menuInfo=
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.delete:
                /*添加一个对话框来确定是否确定当前操作*/
                AlertDialog.Builder dialog=new AlertDialog.Builder(TaskActivity.this);
                dialog.setMessage("Are you sure to remove the clock?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pos=(int)lv.getAdapter().getItemId(menuInfo.position);
                        Goal goal=goals.get(pos);
                        DataSupport.delete(Goal.class,goal.getId());
                        Intent intent=new Intent(TaskActivity.this,TaskActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.set:
                int pos1=(int)lv.getAdapter().getItemId(menuInfo.position);
                Goal goal1=goals.get(pos1);
                Intent intent=new Intent(TaskActivity.this,SetTask.class);
                intent.putExtra("extra_set_task_id",goal1.getId());
                startActivity(intent);
                break;
            case R.id.achieve:
                int pos2=(int)lv.getAdapter().getItemId(menuInfo.position);
                Goal goal2=goals.get(pos2);
                goal2.setAchieve(true);
                goal2.save();
                User user= DataSupport.find(User.class,1);
                if(user==null){
                    user=new User(1,"newName");

                }
                int coins=user.getCoins()+1;
                user.setCoins(coins);
                user.update(1);

                loading();
        }
        return true;
    }


    @Override
    //菜单列表
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_task:
                Intent addTask=new Intent(TaskActivity.this,AddTask.class);
                startActivity(addTask);
                break;
            case R.id.remove_task:
                break;
            default:
        }
        return true;
    }
    public void loading(){
        Connector.getDatabase();
//        DataSupport.deleteAll(StageGoal.class);
//        DataSupport.deleteAll(Goal.class);
        //显示全部任务
        goals= DataSupport.findAll(Goal.class);

        //显示未完成任务
        /*goals= DataSupport.where("achieve = ?","0").find(Incident.class);*/
        //显示列表并监听点击列表中的数据，然后做出对应的Intent
        TaskAdapter adapter=new TaskAdapter(TaskActivity.this,R.layout.text_view,goals);
        lv=(ListView) findViewById(R.id.list_view);
        lv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading();
    }
}
