package com.example.gaminglife;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

public class MeActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //隐藏顶部导航栏

        loading();

        setupView();


    }

    public void setupView(){
        mEditText=(EditText)findViewById(R.id.editText);

        mButton=(Button)findViewById(R.id.button4);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(v.getId()==R.id.button4){
                    String tmp=mEditText.getText().toString();
                    User user= DataSupport.find(User.class,1);

                    user.setName(tmp);
                    user.update(1);
                    Toast.makeText(MeActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    loading();
                }
            }
        });
    }

    public void loading(){

        User user1= DataSupport.find(User.class,1);

        if(user1==null){
            user1=new User(1,"newName");
            user1.setId(1);

        }
        //User user1= new User();

        TextView textView5=(TextView)findViewById(R.id.textView5);
        textView5.setTextSize(25);
        textView5.setText("\n\nYour name : "+user1.getName());
        textView5.setTextColor(Color.parseColor("#ffaa2a"));


        TextView textView6=(TextView)findViewById(R.id.textView6);
        textView6.setTextSize(25);
        textView6.setText("\n\n\nYour coins : "+Integer.toString(user1.getCoins()));

    }
}
