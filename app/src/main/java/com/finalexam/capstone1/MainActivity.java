package com.finalexam.capstone1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        Intent intent = getIntent();

        btn_flight = (ImageButton) findViewById(R.id.btn_home_f);
        btn_flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flight search Activity로 가는 인텐트 생성
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);    // this 오류 해결(anonymous)
                // 액티비티 시작
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                /*intent.putExtra("id", id);
                intent.putExtra("password", password);
                intent.putExtra("e_mail", st_email);
                intent.putExtra("date_of_birth", st_birth);*/
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setMessage("정말 종료하시겠습니까?");
        alert.show();
    }
}
