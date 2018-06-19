package com.example.gaminglife;
import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by 偏执 on 2018/5/18.
 */

public class Goal extends DataSupport{

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    private String name;

    private Date date;

    private boolean achieve;

    private int stage;

    private List<StageGoal> stageGoal;

    private String context;

    public Goal() {

    }
    public Goal(String name,Date date,int stage,String context) {
        this.name = name;
        this.date = date;
        this.stage = stage;
        this.context=context;
        achieve=false;
        stageGoal = new ArrayList<StageGoal>();
    }

    public void setName(String newName) {

        this.name=newName;
    }


    public String getName() {

        return this.name;
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

    public int getStage() {

        return this.stage;
    }


    public void setStage(int newStage) {

        this.stage=newStage;
    }

    public List<StageGoal> getStageGoal(){
        return this.stageGoal;
    }

    public void setStageGoal(List<StageGoal> newStageGoal){
        this.stageGoal=newStageGoal;
    }

    public List<StageGoal> getSubTaskData(){
        return DataSupport.where("goal_id = ?",
                String.valueOf(id)).find(StageGoal.class);
    }

    public Iterator getIterator() {
        return stageGoal.iterator();
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
        return temp;
    }
    public String toParticularString(){
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
        temp=temp+"\n"+"SubGoal"+"\n";
        for(Iterator i=this.stageGoal.iterator();i.hasNext();){
            StageGoal tempOfGoal=(StageGoal) i.next();
            temp=temp+tempOfGoal.toString1()+"\n";
        }
        return temp;
    }

}
