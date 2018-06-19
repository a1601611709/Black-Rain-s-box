package com.example.gaminglife;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 偏执 on 2018/5/18.
 */

public class StageGoal extends DataSupport {

    private int id;

    private Goal goal;

    String name;

    Date date;

    private boolean achieve;

    private String context;

    private int goal_id;

    public StageGoal(){

    }

    public StageGoal(String name,Date date,String context) {
        this.name=name;
        this.date=date;
        this.context=context;
        achieve=false;
    }

    public Goal getGoal(){
        return goal;
    }

    public void setGoal(Goal newGoal){
        goal=newGoal;
    }

    public void setGoal_id(int goal_id) {
        this.goal_id = goal_id;
    }

    public int getGoal_id() {

        return goal_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {

        return date;
    }

    public boolean isAchieve() {
        return achieve;
    }

    public void setAchieve(boolean achieve) {
        this.achieve = achieve;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }


    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name=newName;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {

        return context;
    }

    public String toString1(){
        String temp;
        SimpleDateFormat dateOfFormat=
                new SimpleDateFormat("yyyy年MM月dd日");
        String time=dateOfFormat.format(this.date);
        if(this.achieve){
            temp=this.name+"-"+"Has been completed";
        }
        else{
            temp=this.name+"-"+"unfinished"+"-"+time;
        }
        temp+="\n"+this.context;
        return temp;
    }
}
