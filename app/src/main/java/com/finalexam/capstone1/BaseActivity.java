package com.finalexam.capstone1;

import androidx.appcompat.app.AppCompatActivity;

import com.finalexam.capstone1.notuse.GlobalApplication;

public class BaseActivity extends AppCompatActivity {

    public void progressON() {
        GlobalApplication.getGlobalApplicationContext().progressOn(this, null);
    }
    
    public void progressOFF(){
        GlobalApplication.getGlobalApplicationContext().progressOFF();
    }
}
