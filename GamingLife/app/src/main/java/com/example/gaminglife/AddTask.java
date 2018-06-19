package com.example.gaminglife;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AddTask extends AppCompatActivity implements View.OnClickListener,DatePicker.OnDateChangedListener {

    private EditText editNameOfTask;

    private TextView dateOfView;

    private EditText editContextOfTask;

    private int numberOfSubtask;

    private String nameOfTask;

    private Date dateOfTask;

    Spinner spinner;

    private int year, month, day, hour, minute;

    private StringBuffer date, time;

    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_layout);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        dateOfView=(TextView)findViewById(R.id.text_view_date);
        editContextOfTask=(EditText)findViewById(R.id.add_task_context);
        editNameOfTask=(EditText)findViewById(R.id.edit_task_name);
        initDateTime();
        dateOfView.setOnClickListener(this);
        date=new StringBuffer();
        time=new StringBuffer();
        //设置下拉式选项
        spinner=(Spinner)findViewById(R.id.number_subTask);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str=(String)spinner.getSelectedItem();
                numberOfSubtask=Integer.parseInt(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //监听save按钮设置完data去注释
        Button save=(Button) findViewById(R.id.saveData);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String temp=editNameOfTask.getText().toString();
                if (editNameOfTask.getText() == null ||
                        editNameOfTask.getText().toString().equals("")) {
                    try {
                        throw new NullNameException("Please input the name!");
                    } catch (NullNameException e) {
                        Toast.makeText(AddTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else if(dateOfView.getText()==null||dateOfView.getText().equals("")){
                    try{
                        throw new NullDataException("Please set the date!");
                    }catch (NullDataException e){
                        Toast.makeText(AddTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else if(editContextOfTask.getText()==null||editContextOfTask.getText().toString().equals("")){
                    try{
                        throw new NullContextException("Please describe it!");
                    }catch (NullContextException e){
                        Toast.makeText(AddTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(AddTask.this);
                    dialog.setTitle("Data is saved~");
                    dialog.setMessage("Now you'll set subTask, do you want to continue?");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String dataOfDate=dateOfView.getText().toString();
                            String dataOfContext=editContextOfTask.getText().toString();
                            nameOfTask=editNameOfTask.getText().toString();
                            SimpleDateFormat dateOfFormat= new SimpleDateFormat("yyyy年MM月dd日");
                            try{
                                dateOfTask=dateOfFormat.parse(dataOfDate);
                            }catch (Exception e){
                                Toast.makeText(AddTask.this,"Format error!",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            Goal goal=new Goal(nameOfTask,dateOfTask,numberOfSubtask,dataOfContext);
                            boolean a=goal.save();
                            if(a){
                                Toast.makeText(AddTask.this,"success",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AddTask.this,"fail",Toast.LENGTH_SHORT).show();
                            }
                            Intent intent=new Intent(AddTask.this,AddSubTask.class);
                            intent.putExtra("extra_number_subTask",goal.getId());
                            startActivity(intent);
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
        });
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
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_view_date:
                initDateTime();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                dialog.setTitle("Set date");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month-1, day, this);
        }
    }
}
