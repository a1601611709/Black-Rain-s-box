package com.example.gaminglife;

import org.litepal.crud.DataSupport;

/**
 * Created by 偏执 on 2018/6/19.
 */

public class Picture extends DataSupport{

    private int imageId;

    private int id;

    private int user_id;

    private boolean purchase;

    public Picture(){

    }

    public Picture(int imageId){
        this.imageId=imageId;
        this.purchase=false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {

        return user_id;
    }

    public int getImageId() {

        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }
}