package com.finalexam.capstone1.alarms;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.finalexam.capstone1.MainActivity;
import com.finalexam.capstone1.MypageActivity;
import com.finalexam.capstone1.R;
import com.finalexam.capstone1.alarms.Alarm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MypageAlarmsActivity extends AppCompatActivity {

    String url = "http://synergyflight.dothome.co.kr/get_alarm_data.php";
    ImageButton btn_home, btn_profile;
    ListView lv_alarm;
    ArrayList<Alarm> list;
    public GetAlarm alrm;
    private List<HashMap<String, String>> alarmList = null;
    private String CurState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_alarm);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        Intent intent = getIntent();
        CurState = intent.getStringExtra("CurState");

        lv_alarm = (ListView) findViewById(R.id.lv_alarm);

        btn_home = (ImageButton) findViewById(R.id.btn_ma_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        btn_profile = (ImageButton) findViewById(R.id.btn_ma_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MypageActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        alrm = new GetAlarm();
        alarmList = new ArrayList<HashMap<String, String>>();

        //알람 목록 받아옴
        try {
            alarmList=alrm.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        test_listview();
        AlarmListAdapter adapter = new AlarmListAdapter(list);
        lv_alarm.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        switch (CurState){
            case "SetAlarm":
                Intent intent = new Intent(MypageAlarmsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case "CheckAlarm":
                intent = new Intent(MypageAlarmsActivity.this, MypageActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    void test_listview() {
        list = new ArrayList<>();


        for(int i = 0; i < alarmList.size(); i++){

            list.add(new Alarm(alarmList.get(i).get("dept_city"), alarmList.get(i).get("arr_city"), alarmList.get(i).get("airline_info"),
                    Integer.parseInt(alarmList.get(i).get("adult")), Integer.parseInt(alarmList.get(i).get("child")), alarmList.get(i).get("dept_date"), alarmList.get(i).get("price_limit")));
            System.out.println(alarmList.get(i).get("airline_info"));
        }

//        list.add(new Alarm("ICN", "CJU", "Any airline", 1, 0, "20.06.30", "10,000"));
//        list.add(new Alarm("ICN", "NRT", "Any airline", 1, 0, "20.06.30", "50,000"));
//        list.add(new Alarm("ICN", "TPE", "Any airline", 1, 0, "20.06.30", "30,000"));
//
//        list.add(new Alarm("GMP", "CJU", "Any airline", 1, 0, "20.06.30", "8,000"));
//        list.add(new Alarm("ICN", "HND", "Any airline", 1, 0, "20.06.30", "50,000"));

    }


    class AlarmListAdapter extends BaseAdapter {

        ArrayList<Alarm> list;

        AlarmListAdapter(ArrayList<Alarm> list) {
            this.list = list;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.mypage_alarm_listitem , viewGroup, false);
            }

            TextView tv_airport = (TextView) view.findViewById(R.id.tv_alarm_airport);
            TextView tv_date = (TextView) view.findViewById(R.id.tv_alarm_date);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_alarm_price);
            TextView tv_detail = (TextView) view.findViewById(R.id.tv_alarm_detail);

            Alarm lv_item = list.get(i);
            tv_airport.setText(lv_item.dept + "  to  " + lv_item.arrv);
            tv_date.setText(lv_item.date);
            tv_price.setText(lv_item.price);
            tv_detail.setText(lv_item.adlt + " Adult, " + lv_item.airl);

            // list view click evnet
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
    }

    class GetAlarm extends AsyncTask<String, Integer, List<HashMap<String, String>>> {


        @Override
        protected List<HashMap<String, String>> doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection();

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String str = jsonHtml.toString();

            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("result");

//            System.out.println(results); //json으로 받아옴
                for (int i = 0; i < results.length(); i++) {
                    JSONObject alarm_info = results.getJSONObject(i);

                    String code_alarm = alarm_info.getString("code_alarm");
                    String dept_city = alarm_info.getString("dept_city");
                    String dept_date = alarm_info.getString("dept_date");
                    String arr_city = alarm_info.getString("arr_city");
                    String arr_date = alarm_info.getString("arr_date");
                    String adult = alarm_info.getString("adult");
                    String child = alarm_info.getString("child");
                    String clss = alarm_info.getString("class");
                    String airline_info = alarm_info.getString("airline_info");
                    String price_limit = alarm_info.getString("price_limit");

                    HashMap<String, String> alarmMap = new HashMap<String, String>();
                    alarmMap.put("code_alarm", code_alarm);
                    alarmMap.put("dept_city", dept_city);
                    alarmMap.put("arr_city", arr_city);
                    alarmMap.put("dept_date", dept_date);
                    alarmMap.put("arr_date", arr_date);
                    alarmMap.put("adult", adult);
                    alarmMap.put("child", child);
                    alarmMap.put("class", clss);
                    alarmMap.put("airline_info", airline_info);
                    alarmMap.put("price_limit", price_limit);

                    alarmList.add(alarmMap);


                }
//                System.out.println(alarmList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return alarmList;
        }

    }

}
