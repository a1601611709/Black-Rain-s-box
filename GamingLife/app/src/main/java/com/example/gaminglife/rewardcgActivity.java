package com.example.gaminglife;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import org.litepal.util.Const;

/**
 * Created by 偏执 on 2018/5/28.
 */

public class rewardcgActivity extends AppCompatActivity{


    SQLiteDatabase db;

    User user1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_cg);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        //User user1= new User();
        loading();
        //setupView();

            Button buttonOfpic1=(Button)findViewById(R.id.button);

            buttonOfpic1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(user1.getCoins()>=1) {
                        user1.setCoins(user1.getCoins()-1);
                        user1.update(1);
                        Intent intent=new Intent(rewardcgActivity.this,pic1Activity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent=new Intent(rewardcgActivity.this,FalseActivity.class);
                        startActivity(intent);
                    }



                }
            });


            Button buttonOfpic2=(Button)findViewById(R.id.button2);

            buttonOfpic2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    if(user1.getCoins()>=2) {
                        int coins=user1.getCoins()-2;
                        user1.setCoins(coins);
                        user1.update(1);
                        Intent intent=new Intent(rewardcgActivity.this,pic2Activity.class);
                        startActivity(intent);

                    }
                    else {
                        Intent intent=new Intent(rewardcgActivity.this,FalseActivity.class);
                        startActivity(intent);
                    }


                }
            });


            Button buttonOfpic3=(Button)findViewById(R.id.button3);

            buttonOfpic3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(user1.getCoins()>=4) {
                        int coins=user1.getCoins()-4;
                        user1.setCoins(coins);
                        user1.update(1);
                        Intent intent=new Intent(rewardcgActivity.this,pic3Activity.class);
                        startActivity(intent);

                    }
                    else {
                        Intent intent=new Intent(rewardcgActivity.this,FalseActivity.class);
                        startActivity(intent);
                    }

                }
            });

        }

    private void loading(){
        user1=DataSupport.find(User.class,1);
        if(user1==null){
            user1=new User(10,"NewName");
            user1.setId(1);
            user1.save();
        }
        TextView textView4=(TextView)findViewById(R.id.textView4);
        textView4.setTextSize(25);
        textView4.setText("Your name : "+user1.getName());

        TextView textView=(TextView)findViewById(R.id.textView3);
        textView.setTextSize(25);
        textView.setText("\n\nYour coins : "+Integer.toString(user1.getCoins()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        loading();
    }
}
