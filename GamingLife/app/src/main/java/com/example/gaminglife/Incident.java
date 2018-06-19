package com.example.gaminglife;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by 偏执 on 2018/5/21.
 */

public class Incident extends DataSupport{

    private int id;

    private String name;

    private Date date;

    private String content;

    private boolean finish;

    private long alarmTime;

    public Incident(){
    }

    public Incident(String name,Date date,String content,long alarmTime){
        this.name=name;
        this.date=date;
        this.content=content;
        finish=false;
        this.alarmTime=alarmTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        // TODO: implement
        return name;
    }


    public void setName(String newName) {
        // TODO: implement
        name=newName;
    }


    public Date getDate() {
        // TODO: implement
        return date;
    }


    public void setDate(Date newDate) {
        // TODO: implement
        date=newDate;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getAlarmTime() {

        return alarmTime;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getdetailContent(){
        String detailContent=name+"\n"+this.content+"\n";
        return detailContent;
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String newContent){
        content=newContent;
    }

    public String toString(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        String time=format.format(this.date);
        return this.name+"-"+time;
    }

}
