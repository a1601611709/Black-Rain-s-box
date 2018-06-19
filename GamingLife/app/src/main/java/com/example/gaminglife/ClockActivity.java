package com.example.gaminglife;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.Button;
import android.widget.ListView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class ClockActivity extends AppCompatActivity {

    List<Incident> incidents;

    ListView listView;

    SQLiteDatabase db;

    FloatingActionButton fab;//悬浮按钮

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clock_menu,menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.item_clock_menu,menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        ActivityCollector.addActivity(this);
        loading();
        //为listView所有item注册
        this.registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //打开对应列表中的数据
                Incident incident=incidents.get(position);
                Intent intent2=new Intent(ClockActivity.this,ClockContent.class);
                intent2.putExtra("extra_data_clock",incident.getdetailContent());
                startActivity(intent2);

            }
        });


        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                switch (view.getId()) {
                    case R.id.fab:
                        PopupMenu popupMenu = new PopupMenu(ClockActivity.this, view);
                        getMenuInflater().inflate(R.menu.pop_item, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.action_add:
                                        Intent intent=new Intent(ClockActivity.this,AddClock.class);
                                        startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        loading();
    }

    @Override
    //菜单列表
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_clock:
                Intent incident=new Intent(ClockActivity.this,AddClock.class);
                startActivity(incident);
                break;
            default:
        }
        return true;
    }

    public boolean onContextItemSelected(MenuItem item){
        final AdapterView.AdapterContextMenuInfo menuInfo=
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.delete:
                /*添加一个对话框来确定是否确定当前操作*/
                AlertDialog.Builder dialog=new AlertDialog.Builder(ClockActivity.this);
                dialog.setMessage("Are you sure to remove the clock.");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pos=(int)listView.getAdapter().getItemId(menuInfo.position);
                        Incident incident=incidents.get(pos);
                        incident.delete();
                        Intent intent2=new Intent(ClockActivity.this,ClockAlarmBroadcastReceiver.class);
                        intent2.putExtra("display_clock_context",incident.toString());
                        intent2.putExtra("display_clock_detail_context",incident.getdetailContent());
                        intent2.putExtra("clock_ID",incident.getId());
                        intent2.setAction("CLOCK_ALARM");
                        PendingIntent clockAlarm=PendingIntent.getBroadcast(
                                ClockActivity.this,incident.getId(),intent2,0);
                        AlarmManager clockAM=(AlarmManager)getSystemService(ALARM_SERVICE);
                        clockAM.cancel(clockAlarm);
                        Intent intent=new Intent(ClockActivity.this,ClockActivity.class);
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
                int pos1=(int)listView.getAdapter().getItemId(menuInfo.position);
                Incident incident1=incidents.get(pos1);
                Intent intent=new Intent(ClockActivity.this,SetClock.class);
                intent.putExtra("extra_clock_id",incident1.getId());
                startActivity(intent);
                break;
            case R.id.achieve:
                int pos2=(int)listView.getAdapter().getItemId(menuInfo.position);
                Incident incident2=incidents.get(pos2);
                incident2.setFinish(true);
                incident2.save();
                Intent intent3=new Intent(ClockActivity.this,ClockAlarmBroadcastReceiver.class);
                intent3.putExtra("display_clock_context",incident2.toString());
                intent3.putExtra("display_clock_detail_context",incident2.getdetailContent());
                intent3.putExtra("clock_ID",incident2.getId());
                intent3.setAction("CLOCK_ALARM");
                PendingIntent clockAlarm=PendingIntent.getBroadcast(
                        ClockActivity.this,incident2.getId(),intent3,0);
                AlarmManager clockAM=(AlarmManager)getSystemService(ALARM_SERVICE);
                clockAM.cancel(clockAlarm);
                loading();
        }
        return true;
    }

    public void loading(){
        Connector.getDatabase();
        //显示全部任务
        //删除所有数据
        //DataSupport.deleteAll(Incident.class);
        //incidents= DataSupport.findAll(Incident.class);

        //显示未完成任务
        incidents=DataSupport.where("finish = ?","0").find(Incident.class);
        //显示列表并监听点击列表中的数据，然后做出对应的Intent
        ClockAdapter adapter=new ClockAdapter(ClockActivity.this,R.layout.clock_item,incidents);
        listView=(ListView)findViewById(R.id.clock_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

