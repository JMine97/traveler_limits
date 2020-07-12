package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.finalexam.capstone1.alarms.MypageAlarmsActivity;

import java.util.ArrayList;

public class MypageActivity extends Activity {

    private ArrayList<String> list_menu;
    private BaseAdapter_mypage adapter;
    private ListView mListView;

    private ImageButton btn_home, btn_profile;
    private TextView login, email, birth;
    private String id, st_email, st_birth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_profile);

        mListView = (ListView) findViewById(R.id.list_mypage);
        list_menu = new ArrayList<String>();
        list_menu.add("개인정보"); list_menu.add("알람설정"); list_menu.add("알람목록");
        adapter = new BaseAdapter_mypage(this, list_menu);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), i+"번째 id="+l, Toast.LENGTH_SHORT).show();
                if (i == 2) {
                    Intent intent = new Intent(view.getContext(), MypageAlarmsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });

        btn_home = (ImageButton)findViewById(R.id.btn_mp_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        btn_profile = (ImageButton)findViewById(R.id.btn_mp_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You're looking mypage already", Toast.LENGTH_SHORT).show();
            }
        });

        login=(TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1000);
            }
        });
        email = (TextView) findViewById(R.id.email);
        birth = (TextView) findViewById(R.id.birth);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        st_email = intent.getStringExtra("e_mail");
        st_birth = intent.getStringExtra("date_of_birth");

        if(id!=null){
            login.setText(id+"님 안녕하세요");
            login.setOnClickListener(null);
            email.setText(st_email);
            birth.setText(st_birth);
        }
    }


    class BaseAdapter_mypage extends BaseAdapter {

        private ArrayList<String> list;
        private Context context;

        BaseAdapter_mypage(Context context, ArrayList<String> data) {
            this.context = context;
            this.list = data;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.mypage_profile_listitem, viewGroup, false);
            }

            final String str = list.get(i);
            TextView tv = (TextView) view.findViewById(R.id.tv_mypage_list);
            tv.setText(str);

            return view;
        }
    }
}

