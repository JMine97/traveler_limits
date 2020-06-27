package com.finalexam.capstone1.flight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.finalexam.capstone1.MainActivity;
import com.finalexam.capstone1.MypageActivity;
import com.finalexam.capstone1.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends Activity {

    private TextView tv_date, tv_dep, tv_dep_kr, tv_arr, tv_arr_kr;
    private Button btn_save;
    private ImageButton btn_home, btn_profile;

    private ListView lv_search;
    private ArrayList<Flight> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_search2);

        Intent intent = getIntent();
        final String arr = intent.getStringExtra("ARRIVAL");
        final String dep = intent.getStringExtra("DEPARTURE");
        final String date = intent.getStringExtra("DATE");
        final int adlt = intent.getIntExtra("ADULT", 0);
        final int chld = intent.getIntExtra("CHILD", 0);

        tv_date = (TextView)findViewById(R.id.tv_fsearch_date);
        tv_date.setText(date);
        tv_dep = (TextView)findViewById(R.id.tv_fsearch_dep);
        tv_dep.setText(dep);
        tv_dep_kr = (TextView)findViewById(R.id.tv_fsearch_dep_kr);
        for (Airport a : SearchActivity.getList()) {
            if (a.getName_en().equals(dep)) tv_dep_kr.setText(a.getCity());
        }
        tv_arr = (TextView)findViewById(R.id.tv_fsearch_arr);
        tv_arr.setText(arr);
        tv_arr_kr = (TextView)findViewById(R.id.tv_fsearch_arr_kr);
        for (Airport a : SearchActivity.getList()) {
            if (a.getName_en().equals(arr)) tv_arr_kr.setText(a.getCity());
        }

        lv_search = (ListView) findViewById(R.id.lv_search);
        test_list();
        SearchListViewAdapter adapter = new SearchListViewAdapter(list);
        lv_search.setAdapter(adapter);


// 출발, 도착, 날짜, 인원 수 데이터 넘겨줌
        btn_save = (Button)findViewById(R.id.btn_fsavealarm1);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SetAlarmDetailActivity.class);
                intent.putExtra("DEPARTURE", dep);
                intent.putExtra("ARRIVAL", arr);
                intent.putExtra("DATE", date);
                intent.putExtra("ADULT", adlt);
                intent.putExtra("CHILD", chld);
                startActivity(intent);
            }
        });

        btn_home = (ImageButton) findViewById(R.id.btn_fsearch2_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        btn_profile = (ImageButton) findViewById(R.id.btn_fsearch2_profile);
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


    void test_list() {
        list = new ArrayList<Flight>();

        list.add(new Flight("Air Seoul", "06:00", "07:00", "21,403"));
        list.add(new Flight("Asiana", "06:05", "07:35", "30,000"));
        list.add(new Flight("Air Jeju", "12:15", "13:35", "75,000"));
        list.add(new Flight("Jin Air", "13:45", "15:00", "28,900"));
        list.add(new Flight("Korean Air", "15:00", "16:10", "30,000"));
        list.add(new Flight("Air Busan", "17:45", "18:55", "22,500"));
        list.add(new Flight("Air Busan", "18:10", "19:20", "30,520"));
        list.add(new Flight("tway", "18:10", "19:20", "23,500"));
        list.add(new Flight("Air Busan", "18:50", "20:00", "24,000"));
        list.add(new Flight("Air Busan", "18:55", "20:05", "91,000"));
        list.add(new Flight("tway", "19:00", "20:10", "21,500"));
        list.add(new Flight("Korean Air", "20:30", "21:40", "30,000"));
        list.add(new Flight("Asiana", "21:20", "22:30", "30,500"));




    }
}
