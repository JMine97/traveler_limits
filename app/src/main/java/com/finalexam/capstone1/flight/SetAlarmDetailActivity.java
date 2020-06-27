package com.finalexam.capstone1.flight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.finalexam.capstone1.MainActivity;
import com.finalexam.capstone1.MypageActivity;
import com.finalexam.capstone1.R;

public class SetAlarmDetailActivity extends Activity {

    private Button btn_save;
    ImageButton btn_home, btn_profile;
    private EditText ed_price_limit, ed_airline;
    private float price_limit;
    private String airline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_alarm1);

        ed_price_limit=findViewById(R.id.price_limit);
        ed_airline=findViewById(R.id.airline);

        Intent intent = getIntent();
        final String arr = intent.getStringExtra("ARRIVAL");
        final String dep = intent.getStringExtra("DEPARTURE");
        final String date = intent.getStringExtra("DATE");
        final int adlt = intent.getIntExtra("ADULT", 0);
        final int chld = intent.getIntExtra("CHILD", 0);

        btn_save = (Button)findViewById(R.id.btn_fsavealarm2);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PriceDistributionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                price_limit = Float.parseFloat(String.valueOf(ed_price_limit.getText()));
                airline = String.valueOf(ed_airline.getText());

                intent.putExtra("DEPARTURE", dep);
                intent.putExtra("ARRIVAL", arr);
                intent.putExtra("DATE", date);
                intent.putExtra("ADULT", adlt);
                intent.putExtra("CHILD", chld);
                intent.putExtra("PRICELIMIT", price_limit);
                intent.putExtra("AIRLINE", airline);
                startActivity(intent);
            }
        });

        btn_home = (ImageButton) findViewById(R.id.btn_falarm_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        btn_profile = (ImageButton) findViewById(R.id.btn_falarm_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }
}
