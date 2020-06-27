package com.finalexam.capstone1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.finalexam.capstone1.flight.SearchActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_flight, btn_home, btn_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        btn_flight = (ImageButton) findViewById(R.id.btn_home_f);
        btn_flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flight search Activity로 가는 인텐트 생성
                Intent intent = new Intent(view.getContext(), SearchActivity.class);    // this 오류 해결(anonymous)
                // 액티비티 시작
                startActivity(intent);
            }
        });

        btn_home = (ImageButton) findViewById(R.id.btn_home_h);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You are looking home already", Toast.LENGTH_SHORT).show();
            }
        });

        btn_profile = (ImageButton) findViewById(R.id.btn_home_p);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MypageActivity.class);
                startActivity(intent);
            }
        });
    }

}
