package com.example.gaminglife;

import android.support.v7.app.AppCompatActivity;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 偏执 on 2018/5/28.
 */

public class User extends DataSupport{

    private int coins;
    private String name;
    private int id;
    private List<Picture> pictures;
    public User(){

    }

    public User(int coin,String name){
        coins=coin;
        this.name=name;
        pictures=new ArrayList<>();
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<Picture> getPictures() {

        return pictures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public List<Picture> getPictureData(){
        return DataSupport.where("user_id = ?",
                String.valueOf(id)).find(Picture.class);
    }
    /*
    static private User singLetonInsance=null;

    private User(){

    }

    static public User getSingLetonInsance(){
        if(singLetonInsance==null){
            singLetonInsance=new User();
        }
        return singLetonInsance;
    }
    */

    public int getCoins(){
        return coins;
    }

    public void setCoins(int coins){

      this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
