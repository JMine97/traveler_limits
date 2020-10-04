package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static com.finalexam.capstone1.SearchActivity.TAG;

public class SetAlarmDetailActivity extends Activity {

    private Button btn_save;
    ImageButton btn_home, btn_profile;
    private EditText ed_price_limit, ed_airline;
    private Float price_limit;
    private String airline;
    private String id, password, st_email, st_birth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_alarm1);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        ed_price_limit=findViewById(R.id.price_limit);
        ed_airline=findViewById(R.id.airline);

//        Intent intent = getIntent();



        btn_save = (Button)findViewById(R.id.btn_fsavealarm2);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PriceDistributionActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                price_limit = Float.valueOf(String.valueOf(ed_price_limit.getText()));
                airline = String.valueOf(ed_airline.getText());
                PreferenceManager pref = new PreferenceManager(SetAlarmDetailActivity.this);
                pref.put("PRICELIMIT", price_limit);

                Log.d(TAG, "POST response code pricelimit at setalarmdetailactivity" + price_limit);

                if(price_limit==null) {
                    Toast.makeText(SetAlarmDetailActivity.this, "가격이 빈칸 일 수 없습니다", Toast.LENGTH_SHORT).show();
                }
                else {

//                    intent.putExtra("DEPARTURE", dep);
//                    intent.putExtra("ARRIVAL", arr);
//                    intent.putExtra("DATE", date);
//                    intent.putExtra("ADULT", adlt);
//                    intent.putExtra("CHILD", chld);
//                    intent.putExtra("PRICELIMIT", price_limit);
//                    intent.putExtra("AIRLINE", airline);
//
//                    intent.putExtra("id", id);
//                    intent.putExtra("password", password);
//                    intent.putExtra("e_mail", st_email);
//                    intent.putExtra("date_of_birth", st_birth);
                    startActivity(intent);
                }
            }
        });


        btn_home = (ImageButton) findViewById(R.id.btn_falarm_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("id", id);
//                intent.putExtra("password", password);
//                intent.putExtra("e_mail", st_email);
//                intent.putExtra("date_of_birth", st_birth);
                startActivity(intent);
            }
        });
        btn_profile = (ImageButton) findViewById(R.id.btn_falarm_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MypageActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("id", id);
//                intent.putExtra("password", password);
//                intent.putExtra("e_mail", st_email);
//                intent.putExtra("date_of_birth", st_birth);
                startActivity(intent);
            }
        });

    }
}
