package com.example.amit.uniconnexample;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

/**
 * Created by amit on 30/10/16.
 */

public class App extends Application {
    Settings settings;
    Boolean flag,vib,logincheck;
    SharedPreferences myPrefs;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        myPrefs=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
        flag=myPrefs.getBoolean("isChecked1",true);
        vib=myPrefs.getBoolean("isChecked2",true);
        logincheck=myPrefs.getBoolean("isLoggedin",false);
        Foreground.init(this);
        Timber.plant(new Timber.DebugTree());
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setVib(Boolean vib) {
        this.vib = vib;
    }

    public Boolean getFlag(){
        //flag=settings.isEnabledswitch();
        return (myPrefs.getBoolean("isChecked1",true));
    }

    public Boolean getVib() {
        return (myPrefs.getBoolean("isChecked2",true));
    }

    public Boolean getLogincheck() {
        return myPrefs.getBoolean("isLoggedin",false);
    }
}
