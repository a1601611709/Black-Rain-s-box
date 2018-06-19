package com.example.gaminglife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ListPictureActivity extends AppCompatActivity {

    private List<Picture> pictureList=new ArrayList<>();

    User user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_picture);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
        initPicture();
    }

    private void initPicture(){
        user1=DataSupport.find(User.class,1);
        if(user1==null){
            user1=new User(1,"NewName");
            user1.setId(1);
            user1.save();
        }
        user1.setPictures(user1.getPictureData());
        pictureList= user1.getPictureData();
        if(pictureList.size()==0){
            for(int i=0;i<20;i++){
                Picture picture=new Picture(R.drawable.npt1+i);
                user1.getPictures().add(picture);
                picture.setUser_id(user1.getId());
                picture.save();
            }
            user1.save();
            pictureList= user1.getPictureData();
        }
        TextView textView=(TextView)findViewById(R.id.textView3);
        textView.setTextSize(20);
        textView.setText("\nYour coins : "+Integer.toString(user1.getCoins()));
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        PictureAdapter adapter=new PictureAdapter(pictureList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPicture();
    }

}
