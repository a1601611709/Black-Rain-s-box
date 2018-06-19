package com.example.gaminglife;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private User user1;

    private Button buttonOfMe;
    private Button buttonOfTask;
    private Button buttonOfClock;
    private Button buttonOfReward;
    NavigationView navView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connector.getDatabase();
        /*user1=DataSupport.find(User.class,1);
        if(user1==null){
            user1=new User(10,"NewName");
            user1.setId(1);
            user1.save();
        }*/
        //DataSupport.deleteAll(User.class);

        buttonOfMe = (Button) findViewById(R.id.Me_Button);
        buttonOfTask = (Button) findViewById(R.id.Task_Button);
        buttonOfClock = (Button) findViewById(R.id.Clock_Button);
        buttonOfReward = (Button) findViewById(R.id.Reward_Button);
        buttonOfMe.setOnClickListener(MainActivity.this);
        buttonOfTask.setOnClickListener(MainActivity.this);
        buttonOfClock.setOnClickListener(MainActivity.this);
        buttonOfReward.setOnClickListener(MainActivity.this);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.app.ActionBar actionbar2 = getActionBar();
        if (actionbar2 != null) {
            actionbar2.setDisplayHomeAsUpEnabled(true);
        }

        Button buttonOfMenu = (Button) findViewById(R.id.title_menu);
        buttonOfMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //这个括号请注意
        //滑动菜单

        navView = (NavigationView) findViewById(R.id.menu_view);
        navView.setCheckedItem(R.id.ct_item);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ct_item:
                        Intent ct=new Intent(MainActivity.this,AchieveTask.class);
                        startActivity(ct);
                        break;
                    case R.id.cc_item:
                        Intent cc=new Intent(MainActivity.this,AchieveClock.class);
                        startActivity(cc);
                        break;
                    case R.id.help_item:
                        replaceFragment(new HelpFragment());
                        break;
                    case R.id.au_item:
                        replaceFragment(new UsFragment());
                        break;
                    default:
                }
                return true;
            }
        });
    }
//    //滑动菜单按钮


    public void onClick(View v){
        switch (v.getId()){
            case R.id.Me_Button:
                replaceFragment(new MeFragment());
                changeNavButtonColor(buttonOfMe, buttonOfTask, buttonOfClock, buttonOfReward);
                break;
            case R.id.Task_Button:
                replaceFragment(new TaskFragment());
                changeNavButtonColor(buttonOfTask, buttonOfMe, buttonOfClock, buttonOfReward);
                break;
            case R.id.Clock_Button:
                replaceFragment(new ClockFragment());
                changeNavButtonColor(buttonOfClock, buttonOfTask, buttonOfMe, buttonOfReward);
                break;
            case R.id.Reward_Button:
                replaceFragment(new RewardFragment());
                changeNavButtonColor(buttonOfReward, buttonOfTask, buttonOfClock, buttonOfMe);
                break;
        }
    }
    //底部导航栏点击切换碎片

    private void replaceFragment(Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    private void changeNavButtonColor(Button button1, Button button2, Button button3, Button button4){
        button1.setTextColor(Color.parseColor("#ffaa2a"));
        button2.setTextColor(Color.parseColor("#4a4a4a"));
        button3.setTextColor(Color.parseColor("#4a4a4a"));
        button4.setTextColor(Color.parseColor("#4a4a4a"));
    }
}
