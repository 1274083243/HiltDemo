package com.ike.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ike.myapplication1.bean.Test;

public class MainActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }
    public void test(){
        new Test().fun1(13+"");
    }
}