package com.example.gaminglife;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AddClock extends AppCompatActivity implements View.OnClickListener,
        TimePicker.OnTimeChangedListener,DatePicker.OnDateChangedListener {

    private int year, month, day, hour, minute;

    private StringBuffer date, time;

    Date dateOfClock;

    Spinner spinner;
    String alarmTime;
    Long alarmTimeData;

    EditText nameData;
    EditText contextData;

    TextView viewOfDate;
    TextView viewOfTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clock);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        initDateTime();
        dateOfClock = new Date();
        date = new StringBuffer();
        time = new StringBuffer();
        nameData=(EditText)findViewById(R.id.add_name_clock);
        viewOfTime=(TextView)findViewById(R.id.clock_view_time);
        viewOfDate=(TextView)findViewById(R.id.clock_view_date);
        contextData = (EditText) findViewById(R.id.add_context_clock);
        viewOfDate.setOnClickListener(this);
        viewOfTime.setOnClickListener(this);
        spinner=(Spinner)findViewById(R.id.add_clock_time);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarmTime=(String)spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button saveData=(Button)findViewById(R.id.save_clock_data);
        //设置完日期后恢复
        saveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nameData.getText() == null ||
                                nameData.getText().toString().equals("")) {
                            try {
                                throw new NullNameException("输入名称不能为空");
                            } catch (NullNameException e) {
                                Toast.makeText(AddClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        else if(viewOfDate.getText()==null||viewOfDate.getText().toString().equals("")||
                                viewOfTime.getText()==null||viewOfTime.getText().toString().equals("")){
                            try{
                                throw new NullDataException("不能输入空日期");
                            }catch (NullDataException e){
                                Toast.makeText(AddClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(contextData.getText()==null||contextData.getText().toString().equals("")){
                            try{
                                throw new NullContextException("输入内容不能为空");
                            }catch (NullContextException e){
                                Toast.makeText(AddClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            alarmTimeData=getTimeData(alarmTime);
                            String date = viewOfDate.getText().toString();
                            String time = viewOfTime.getText().toString();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
                            try {
                                dateOfClock = format.parse(date + " " + time);
                            } catch (Exception e) {
                                Toast.makeText(AddClock.this, "格式不正确", Toast.LENGTH_SHORT).show();
                            }
                            if(dateOfClock.getTime()-alarmTimeData<System.currentTimeMillis()){
                                try{
                                    throw new IllegalDateException("输入时间不合法，请修改");
                                }catch (IllegalDateException e){
                                    Toast.makeText(AddClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                AlertDialog.Builder dialog = new AlertDialog.Builder(AddClock.this);
                                dialog.setMessage("Are you sure to save ?");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String nameOfClock = nameData.getText().toString();
                                        String contextOfClock = contextData.getText().toString();
                                        Incident incident = new Incident(nameOfClock, dateOfClock, contextOfClock,alarmTimeData);
                                        if(incident.save()){
                                            Toast.makeText(AddClock.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(AddClock.this, "Failure", Toast.LENGTH_SHORT).show();
                                        }

                                        ClockAlarm(incident);
                                        Intent intent = new Intent(AddClock.this, ClockActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dialog.show();
                            }
                        }
                    }
                }
            );
    }

    @Override
    public void onClick(View v) {
        initDateTime();
        switch(v.getId()){
            case R.id.clock_view_date:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        viewOfDate.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month - 1, day,this);
                break;
            case R.id.clock_view_time:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        viewOfTime.setText(time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(this, R.layout.dialog_time, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.time_picker);
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(this);
                dialog2.setTitle("设置时间");
                dialog2.setView(dialogView2);
                dialog2.show();
                break;
        }
    }

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }

    private long getTimeData(String selectTime){
        long data;
        switch (selectTime){
            case "1 hours":
                data=3600000;
                break;
            case "3 hours":
                data=10800000;
                break;
            case "6 hours":
                data=21600000;
                break;
            case "12 hours":
                data=43200000;
                break;
            case "1 days":
                data=86400000;
                break;
            case "2 days":
                data=172800000;
                break;
            default:
                data=0;
                break;
        }
        return data;
    }

    private void ClockAlarm(Incident incident){
        long firstTime= SystemClock.elapsedRealtime();
        long systemTime=System.currentTimeMillis();
        long setTime=incident.getDate().getTime()-alarmTimeData;
        long time=firstTime+setTime-systemTime;
        Intent intent=new Intent(AddClock.this,ClockAlarmBroadcastReceiver.class);
        intent.putExtra("display_clock_context",incident.toString());
        intent.putExtra("display_clock_detail_context",incident.getdetailContent());
        intent.putExtra("clock_ID",incident.getId());
        intent.setAction("CLOCK_ALARM");
        PendingIntent clockAlarm=PendingIntent.getBroadcast(
                AddClock.this,incident.getId(),intent,0);
        AlarmManager clockAM=(AlarmManager)getSystemService(ALARM_SERVICE);
        clockAM.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,clockAlarm);
        //貌似完成创建
    }

}
