package com.example.gaminglife;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SetSubTask extends AppCompatActivity  implements View.OnClickListener,DatePicker.OnDateChangedListener {

    Goal goal;

    private Date dateOfTask;

    private int year, month, day;

    private StringBuffer date;

    int[] subTaskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_sub_task_activity);
        subTaskID=new int[10];
        Intent intent=getIntent();
        int oldStage=intent.getIntExtra("extra_set_old_stage_subTask",0);
        final int id=intent.getIntExtra("extra_set_number_subTask",0);
        goal=DataSupport.find(Goal.class,id);
        goal.setStageGoal(goal.getSubTaskData());
        LinearLayout linear=(LinearLayout)
                findViewById(R.id.set_linearlay_subTask);
        initDateTime();
        date=new StringBuffer();
        if(oldStage==goal.getStage()){
            for(int i=0;i<goal.getStage();i++){
                StageGoal stageGoal=goal.getStageGoal().get(i);
                subTaskID[i]=stageGoal.getId();
                TextView tvofName=new TextView(this);
                TextView tvOfDeadline=new TextView(this);
                TextView tvOfContext=new TextView(this);
                EditText editContextText=new EditText(this);
                String nameTitle="Please input name of SubTask"+(i+1);
                String contextTitle="Please input context of SubTask"+(i+1);
                tvofName.setText(nameTitle);
                linear.addView(tvofName);
                EditText editName=new EditText(this);
                editName.setText(stageGoal.getName());
                editName.setId(R.id.edit_name_subtask_1+i);
                linear.addView(editName);
                if(i==goal.getStage()){
                    String deadlineTitle="The deadline of SubTask"+(i+1)+"needn't set";
                    tvOfDeadline.setText(deadlineTitle);
                    linear.addView(tvOfDeadline);
                    TextView textView=new TextView(this);
                    textView.setId(R.id.text_deadline_subtask_1+i);
                    SimpleDateFormat dateOfFormat= new SimpleDateFormat("yyyy年MM月dd日");
                    textView.setText(dateOfFormat.format(stageGoal.getDate()));
                    linear.addView(textView);
                }
                else{
                    String deadlineTitle="Please input deadline of SubTask"+(i+1);
                    tvOfDeadline.setText(deadlineTitle);
                    linear.addView(tvOfDeadline);
                    TextView textView=new TextView(this);
                    textView.setId(R.id.text_deadline_subtask_1+i);
                    SimpleDateFormat dateOfFormat= new SimpleDateFormat("yyyy年MM月dd日");
                    textView.setText(dateOfFormat.format(stageGoal.getDate()));
                    textView.setOnClickListener(this);
                    linear.addView(textView);
                }
                tvOfContext.setText(contextTitle);
                linear.addView(tvOfContext);
                editContextText.setId(R.id.edit_context_subtask_1+i);
                editContextText.setText(stageGoal.getContext());
                linear.addView(editContextText);
            }
        }
        else{
            for(int i=0;i<goal.getStage();i++){
                TextView tvOfName=new TextView(this);
                TextView tvOfDeadline=new TextView(this);
                TextView tvOfContext=new TextView(this);
                EditText editContextText=new EditText(this);
                String nameTitle="Please input name of SubTask"+(i+1);
                String contextTitle="Please input context of SubTask"+(i+1);
                tvOfName.setText(nameTitle);
                linear.addView(tvOfName);
                EditText editName=new EditText(this);
                editName.setHint("name of SubTask"+(i+1));
                editName.setId(R.id.edit_name_subtask_1+i);
                linear.addView(editName);
                if(i==goal.getStage()-1){
                    String deadlineTitle="The deadline of SubTask"+(i+1)+"needn't set";
                    tvOfDeadline.setText(deadlineTitle);
                    linear.addView(tvOfDeadline);
                    TextView textView=new TextView(this);
                    textView.setId(R.id.text_deadline_subtask_1+i);
                    SimpleDateFormat dateOfFormat= new SimpleDateFormat("yyyy年MM月dd日");
                    textView.setText(dateOfFormat.format(goal.getDate()));
                    linear.addView(textView);
                }else{
                    String deadlineTitle="Please input deadline of SubTask"+(i+1);
                    tvOfDeadline.setText(deadlineTitle);
                    linear.addView(tvOfDeadline);
                    TextView textView=new TextView(this);
                    textView.setId(R.id.text_deadline_subtask_1+i);
                    textView.setText("click to choice date");
                    textView.setOnClickListener(this);
                    linear.addView(textView);
                }
                tvOfContext.setText(contextTitle);
                linear.addView(tvOfContext);
                editContextText.setId(R.id.edit_context_subtask_1+i);
                linear.addView(editContextText);
            }
        }

        Button saveOfSubTask=new Button(this);
        saveOfSubTask.setText("save");
        saveOfSubTask.setId(R.id.save_subtask_data);
        linear.addView(saveOfSubTask);
        saveOfSubTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    for(int i=0;i<goal.getStage();i++){
                        EditText editTextOfName=(EditText)findViewById(R.id.edit_name_subtask_1+i);
                        TextView textViewOfDate=(TextView)findViewById(R.id.text_deadline_subtask_1+i);
                        EditText editOfContext=(EditText)findViewById(R.id.edit_context_subtask_1+i);
                        checkData(editTextOfName,textViewOfDate,editOfContext,i);
                    }
                    AlertDialog.Builder dialog=new AlertDialog.Builder(SetSubTask.this);
                    dialog.setMessage("Are you sure to save ?");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goal.setStageGoal(goal.getSubTaskData());
                            for(int i=0;i<goal.getStage();i++){
                                EditText editTextOfname=(EditText)findViewById(R.id.edit_name_subtask_1+i);
                                String nameOfSubTask=editTextOfname.getText().toString();
                                TextView textViewOfDate=(TextView)findViewById(R.id.text_deadline_subtask_1+i);
                                String dataOfDate=textViewOfDate.getText().toString();
                                SimpleDateFormat dateOfFormat= new SimpleDateFormat("yyyy年MM月dd日");
                                try{
                                    dateOfTask=dateOfFormat.parse(dataOfDate);
                                }catch (Exception e){
                                    Toast.makeText(SetSubTask.this,"格式不正确",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                StageGoal stageGoal=new StageGoal();
                                stageGoal.setName(nameOfSubTask);
                                stageGoal.setDate(dateOfTask);
                                stageGoal.setGoal_id(id);
                                try{
                                    stageGoal.update(subTaskID[i]);
                                }catch (Exception e){
                                    Log.d("SetSubTask",e.getMessage());
                                }
                            }

                            int a=goal.update(id);
                            if(a==1){
                                Toast.makeText(SetSubTask.this,"success",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SetSubTask.this,"defeat",Toast.LENGTH_SHORT).show();
                            }
                            Intent intent=new Intent(SetSubTask.this,TaskActivity.class);
                            startActivity(intent);

                        }
                    });
                    dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }catch (NullNameException e){
                    Toast.makeText(SetSubTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (NullDataException e){
                    Toast.makeText(SetSubTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (NullContextException e){
                    Toast.makeText(SetSubTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (IllegalDateException e){
                    Toast.makeText(SetSubTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }

    @Override
    public void onClick(View v) {
        initDateTime();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int IdOfView=v.getId();
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                switch(IdOfView){
                    case R.id.text_deadline_subtask_1:
                        TextView dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_1);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_2:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_2);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_3:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_3);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_4:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_4);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_5:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_5);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_6:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_6);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_7:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_7);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_8:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_8);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_9:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_9);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                    case R.id.text_deadline_subtask_10:
                        dateOfView=(TextView)findViewById(R.id.text_deadline_subtask_10);
                        dateOfView.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                        break;
                }

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

    private void checkData(EditText editTextOfName,TextView textView,EditText editTextOfContext,int n)
            throws NullNameException,NullDataException,NullContextException,IllegalDateException{
        if(editTextOfName.getText()==null||editTextOfName.getText().toString().equals("")){
            throw new NullNameException("SubTask "+(n+1)+"输入名字为空");
        }
        else if(textView.getText()==null||textView.getText().toString().equals("click to choice date")){
            throw new NullDataException("SubTask "+(n+1)+"输入日期为空");
        }
        else if(editTextOfContext.getText()==null||editTextOfContext.getText().toString().equals("")){
            throw new NullContextException("SubTask "+(n+1)+"输入内容为空");
        }
        if(n>0){
            TextView frontDateView=(TextView)findViewById(R.id.text_deadline_subtask_1+n-1);
            SimpleDateFormat dateOfFormat= new SimpleDateFormat("yyyy年MM月dd日");
            Date tempFrontDate=new Date();
            try{
                tempFrontDate=dateOfFormat.parse(frontDateView.getText().toString());
            }catch (Exception e){
                Toast.makeText(SetSubTask.this,"格式不正确",Toast.LENGTH_SHORT).show();
            }
            Date tempNowDate=new Date();
            try{
                tempNowDate=dateOfFormat.parse(textView.getText().toString());
            }catch (Exception e){
                Toast.makeText(SetSubTask.this,"格式不正确",Toast.LENGTH_SHORT).show();
            }
            if(tempFrontDate.getTime()>=tempNowDate.getTime()){
                if(n==goal.getStage()-1){
                    throw new IllegalDateException("SubTask "+(n)+"输入日期不合法");
                }
                else{
                    throw new IllegalDateException("SubTask "+(n+1)+"输入日期不合法");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //goal.delete();
    }

}
