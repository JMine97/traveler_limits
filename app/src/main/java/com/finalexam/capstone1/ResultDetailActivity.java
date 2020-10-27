package com.finalexam.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultDetailActivity extends AppCompatActivity {
    private ListView lv_detail;
    private Button btn_detail_ok;
    private ArrayList<FlightDetail> list;
    private TextView tv_rd_total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result_detail);

        Intent intent = getIntent();
        FlightResult flightResult = (FlightResult) intent.getSerializableExtra("OBJECT");

        getWindow().setWindowAnimations(0);

        lv_detail = findViewById(R.id.lv_detail);
        btn_detail_ok = findViewById(R.id.btn_detail_ok);
        btn_detail_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = new ArrayList<FlightDetail>();
        if (flightResult!=null){
            for(int i =0; i<flightResult.getDepCodeSize(); i++){
                list.add(new FlightDetail(flightResult.getCarrierCode(i), flightResult.getCarrier_kor(i), flightResult.getNumber(i), flightResult.getDep_code(i), flightResult.getDep_time(i), flightResult.getArr_code(i), flightResult.getArr_time(i), flightResult.getDuration(i)));
            }
        }
        ResultDetailListViewAdapter adapter = new ResultDetailListViewAdapter(list);
        lv_detail.setAdapter(adapter);
        lv_detail.setVisibility(View.VISIBLE);

        tv_rd_total = findViewById(R.id.tv_rd_total);
        tv_rd_total.setText("Total Time: "+flightResult.getTotalTime());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
