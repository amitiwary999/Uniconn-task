package com.example.amit.uniconnexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amit.uniconnexample.utils.Utils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by amit on 28/11/16.
 */

public class Settings extends AppCompatActivity {
    private TabLayout tablayoutbottom;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tablayoutbottom=(TabLayout)findViewById(R.id.tabLayoutbottom);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        Utils.setUpToolbarBackButton(Settings.this, toolbar);
        setupTabIconsBottom();
        BottomBar bottomBar=(BottomBar)findViewById(R.id.bottomtab);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId==R.id.tab_setting){

                }
                if(tabId==R.id.tab_account){
                    startActivity(new Intent(Settings.this,Profile.class));
                }
                if(tabId==R.id.tab_notification){
                    startActivity(new Intent(Settings.this,Notification.class));
                }
                if(tabId==R.id.tab_message){
                    startActivity(new Intent(Settings.this,Message.class));
                }
                if(tabId==R.id.tab_home){
                    startActivity(new Intent(Settings.this,Tabs.class));
                }
            }
        });
        // setupTabIcons();
        bindWidgetsWithAnEvent();
    }

    private void setupTabIconsBottom() {
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.home));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.myaccount));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.notifications));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.message));
     //   tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.chati));
        tablayoutbottom.addTab(tablayoutbottom.newTab().setIcon(R.drawable.settings),true);
    }

    private void bindWidgetsWithAnEvent()
    {
        tablayoutbottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
        });

    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0:
                startActivity(new Intent(Settings.this,Tabs.class));
                finish();
                break;
            case 1 :
                startActivity(new Intent(Settings.this,Profile.class));
                finish();
                //replaceFragment(new Profile());
                break;
            case 2 :
                  startActivity(new Intent(Settings.this,Notification.class));
                finish();
                // replaceFragment(new Message());
                break;
            case 3:
                startActivity(new Intent(Settings.this,Message.class));
                finish();
                //replaceFragment(new Notification());
                break;
          /*  case 4:
                startActivity(new Intent(Settings.this,Chat.class));
                finish();
                break;*/
            case 5:
              //  startActivity(new Intent(Notification.this,Settings.class));
                // replaceFragment(new Settings());
                break;
        }
    }
}
