package com.example.gaminglife;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.ServiceTestCase;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

public class SetTask extends AppCompatActivity implements View.OnClickListener,DatePicker.OnDateChangedListener  {

    private EditText editNameOfTask;

    private TextView dateOfView;

    private EditText editContextOfTask;

    private int numberOfSubtask;

    private String nameOfTask;

    private int oldStage;

    private Date dateOfTask;

    Goal goal;

    Spinner spinner;

    private int year, month, day, hour, minute;

    private StringBuffer date;

    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_task_activity);
        Intent intent=getIntent();
        final int id=intent.getIntExtra("extra_set_task_id",0);
        goal=DataSupport.find(Goal.class,id);
        iniparameter(goal);
        oldStage=goal.getStage();
        initDateTime();
        dateOfView.setOnClickListener(this);
        date=new StringBuffer();
        //设置下拉式选项
        spinner=(Spinner)findViewById(R.id.number_set_subTask);
        spinner.setSelection(goal.getStage()-1,true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str=(String)spinner.getSelectedItem();
                Toast.makeText(SetTask.this,str,Toast.LENGTH_SHORT).show();
                numberOfSubtask=Integer.parseInt(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button save=(Button) findViewById(R.id.save_alter_data);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (editNameOfTask.getText() == null ||
                        editNameOfTask.getText().toString().equals("")) {
                    try {
                        throw new NullNameException("输入名称不能为空");
                    } catch (NullNameException e) {
                        Toast.makeText(SetTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else if(dateOfView.getText()==null||dateOfView.getText().toString().equals("")){
                    try{
                        throw new NullDataException("不能输入空日期");
                    }catch (NullDataException e){
                        Toast.makeText(SetTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else if(editContextOfTask.getText()==null||editContextOfTask.getText().toString().equals("")){
                    try{
                        throw new NullContextException("输入内容不能为空");
                    }catch (NullContextException e){
                        Toast.makeText(SetTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(SetTask.this);
                    dialog.setTitle("Data will be save.");
                    dialog.setMessage("You will set subTask,do you want to continue?");
                    dialog.setCancelable(false);
                    //注意：以下内容组件ID待修改
                    dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String dataOfdate=dateOfView.getText().toString();
                            nameOfTask=editNameOfTask.getText().toString();
                            SimpleDateFormat dateOfFormat= new SimpleDateFormat("yyyy年MM月dd日");
                            try{
                                dateOfTask=dateOfFormat.parse(dataOfdate);
                            }catch (Exception e){
                                Toast.makeText(SetTask.this,"格式不正确",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            goal.setName(nameOfTask);
                            goal.setDate(dateOfTask);
                            goal.setStage(numberOfSubtask);

                            int a=goal.update(id);
                            if(a==1){
                                Toast.makeText(SetTask.this,"success",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SetTask.this,"defeat",Toast.LENGTH_SHORT).show();
                            }
                            Intent intent=new Intent(SetTask.this,SetSubTask.class);
                            intent.putExtra("extra_set_number_subTask",goal.getId());
                            intent.putExtra("extra_set_old_stage_subTask",oldStage);
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

    private void iniparameter(Goal goal){
        try {
            editContextOfTask=(EditText)findViewById(R.id.set_task_context);
            editContextOfTask.setText(goal.getContext());
            editNameOfTask=(EditText)findViewById(R.id.edit_set_task_name);
            editNameOfTask.setText(goal.getName());
            dateOfView = (TextView) findViewById(R.id.text_view_set_date);
            SimpleDateFormat dateOfFormat = new SimpleDateFormat("yyyy年MM月dd日");
            String oldDate = dateOfFormat.format(goal.getDate());
            dateOfView.setText(oldDate);
            numberOfSubtask = goal.getStage();
        }catch (Exception e){
            Log.d("SetTask",e.getMessage());
        }
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
            case R.id.text_view_set_date:
                initDateTime();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
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
                datePicker.init(year, month-1, day, this);
        }
    }
}
