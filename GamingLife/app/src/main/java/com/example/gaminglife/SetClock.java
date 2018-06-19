package com.example.gaminglife;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class SetClock extends AppCompatActivity implements View.OnClickListener,
        TimePicker.OnTimeChangedListener,DatePicker.OnDateChangedListener {

    EditText editName;

    EditText editContent;

    Date dateOfClock;

    private int year, month, day, hour, minute;

    private StringBuffer date, time;

    Spinner spinner;
    String alarmTime;
    long alarmTimeData;

    TextView viewOfDate;
    TextView viewOfTime;

    Incident oldIncident;

    //检验是否修改成功
    int number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_clock);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        Intent intent=getIntent();
        final int id=intent.getIntExtra("extra_clock_id",0);
        dateOfClock=new Date();
        oldIncident=DataSupport.find(Incident.class,id);
        alarmTimeData=oldIncident.getAlarmTime();
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        String oldDate=format.format(oldIncident.getDate());
        StringTokenizer dateToken=new StringTokenizer(oldDate," ");
        String[] dataOfDate=new String[2];
        int n=0;
        while(dateToken.hasMoreElements()){
            dataOfDate[n]=dateToken.nextToken();
            n++;
        }
        date = new StringBuffer();
        time = new StringBuffer();
        editName=(EditText)findViewById(R.id.set_clock_name);
        editName.setText(oldIncident.getName());
        viewOfTime=(TextView)findViewById(R.id.set_clock_view_time);
        viewOfTime.setText(dataOfDate[1]);
        viewOfDate=(TextView)findViewById(R.id.set_clock_view_date);
        viewOfDate.setText(dataOfDate[0]);
        editContent=(EditText)findViewById(R.id.set_clock_content);
        editContent.setText(oldIncident.getContent());
        initDateTime();
        viewOfDate.setOnClickListener(this);
        viewOfTime.setOnClickListener(this);
        spinner=(Spinner)findViewById(R.id.set_clock_time);
        spinner.setSelection(displayTime(oldIncident.getAlarmTime()),true);
        alarmTime=(String)spinner.getSelectedItem();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarmTime=(String)spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button saveData=(Button)findViewById(R.id.save_clock_set_data);
        saveData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (editName.getText() == null ||
                        editName.getText().toString().equals("")) {
                    try {
                        throw new NullNameException("输入名称不能为空");
                    } catch (NullNameException e) {
                        Toast.makeText(SetClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else if(viewOfDate.getText()==null||viewOfDate.getText().toString().equals("")||
                        viewOfTime.getText()==null||viewOfTime.getText().toString().equals("")){
                    try{
                        throw new NullDataException("不能输入空日期");
                    }catch (NullDataException e){
                        Toast.makeText(SetClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else if(editContent.getText()==null||editContent.getText().toString().equals("")){
                    try{
                        throw new NullContextException("输入内容不能为空");
                    }catch (NullContextException e){
                        Toast.makeText(SetClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String date=viewOfDate.getText().toString();
                    String time=viewOfTime.getText().toString();
                    SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
                    try{
                        dateOfClock=format.parse(date+" "+time);
                    }catch (Exception e){
                        Toast.makeText(SetClock.this,"格式不正确",Toast.LENGTH_SHORT).show();
                    }
                    if(dateOfClock.getTime()-alarmTimeData<System.currentTimeMillis()){
                        try{
                            throw new IllegalDateException("输入时间不合法，请修改");
                        }catch (IllegalDateException e){
                            Toast.makeText(SetClock.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        alarmTimeData=getTimeData(alarmTime);
                        AlertDialog.Builder dialog=new AlertDialog.Builder(SetClock.this);
                        dialog.setMessage("Are you sure to save ?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    String newName=editName.getText().toString();
                                    String newContent=editContent.getText().toString();
                                    oldIncident.setName(newName);
                                    oldIncident.setContent(newContent);
                                    oldIncident.setDate(dateOfClock);
                                    oldIncident.setAlarmTime(alarmTimeData);
                                    number=oldIncident.update(id);
                                    ClockAlarm(oldIncident);
                                    Intent intent=new Intent(SetClock.this,ClockActivity.class);
                                    startActivity(intent);
                                }catch (Exception ne){
                                    Log.d("SetClock",ne.toString()+"\n"+Integer.toString(number));
                                }

                            }
                        });
                        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        initDateTime();
        switch(v.getId()){
            case R.id.set_clock_view_date:
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
            case R.id.set_clock_view_time:
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
        month = calendar.get(Calendar.MONTH) + 1;
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

    private void ClockAlarm(Incident incident){
        long firstTime= SystemClock.elapsedRealtime();
        long systemTime=System.currentTimeMillis();
        long setTime=incident.getDate().getTime()-alarmTimeData;
        long time=firstTime+setTime-systemTime;
        Intent intent=new Intent(SetClock.this,ClockAlarmBroadcastReceiver.class);
        intent.putExtra("display_clock_context",incident.toString());
        intent.putExtra("display_clock_detail_context",incident.getdetailContent());
        intent.putExtra("clock_ID",incident.getId());
        intent.setAction("CLOCK_ALARM");
        PendingIntent clockAlarm=PendingIntent.getBroadcast(
                SetClock.this,incident.getId(),intent,0);
        AlarmManager clockAM=(AlarmManager)getSystemService(ALARM_SERVICE);
        clockAM.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,clockAlarm);
        //貌似完成创建
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

    private int displayTime(long data){
        int temp=0;
        if(data==3600000){
            temp=0;
        }
        else if(data==10800000){
            temp=1;
        }
        else if(data==21600000){
            temp=2;
        }
        else if(data==43200000){
            temp=3;
        }
        else if(data==86400000){
            temp=4;
        }
        else if(data==172800000){
            temp=5;
        }
        return temp;
    }
}
