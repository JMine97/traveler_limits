package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetAlarmActivity extends Activity {

    private Button btn_save;
    private EditText ed_price_limit;
    private float float_limit;
    private ImageView graph;
    private String id, password, st_email, st_birth, adlt, chld, limit;
    private boolean round;
//    private String CurState = "SetAlarm"; //알람 조회 페이지에서 뒤로가기로 이동할 구간을 구분하기 위함

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_create);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        ed_price_limit = findViewById(R.id.price_limit);
        graph = findViewById(R.id.graph);

        Intent intent = getIntent();
        final String arr = intent.getStringExtra("ARRIVAL");
        final String dep = intent.getStringExtra("DEPARTURE");
        final String date = intent.getStringExtra("DATE");
//        final int int_adlt = intent.getIntExtra("ADULT", 0);
        adlt = String.valueOf(intent.getIntExtra("ADULT", 0));
//        final int int_chld = intent.getIntExtra("CHILD", 0);
        chld = String.valueOf(intent.getIntExtra("CHILD", 0));
        id = intent.getStringExtra("id");
        st_email = intent.getStringExtra("e_mail");
        st_birth = intent.getStringExtra("date_of_birth");
        password = intent.getStringExtra("password");
        round = intent.getBooleanExtra("ROUND", true);



        btn_save = (Button)findViewById(R.id.btn_fsavealarm2);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), PriceDistributionActivity.class);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                float_limit = Float.parseFloat(String.valueOf(ed_price_limit.getText()));
//                airline = String.valueOf(ed_airline.getText());

//                intent.putExtra("DEPARTURE", dep);
//                intent.putExtra("ARRIVAL", arr);
//                intent.putExtra("DATE", date);
//                intent.putExtra("ADULT", adlt);
//                intent.putExtra("CHILD", chld);
//                intent.putExtra("PRICELIMIT", price_limit);
////                intent.putExtra("AIRLINE", airline);
//
//                intent.putExtra("id", id);
//                intent.putExtra("password", password);
//                intent.putExtra("e_mail", st_email);
//                intent.putExtra("date_of_birth", st_birth);
//                startActivity(intent);

                Intent intent = new Intent(view.getContext(), MyAlarmsActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("CurState", CurState);
                startActivity(intent);

//                adlt= String.valueOf(int_adlt);
//                chld= String.valueOf(int_chld);
                limit = String.valueOf(ed_price_limit.getText());

                SetAlarmActivity.SaveAlarmActivity task = new SetAlarmActivity.SaveAlarmActivity();
                task.execute("http://" + "synergyflight.dothome.co.kr" + "/insert_alarm_data.php", dep, arr, date, adlt, chld, limit);
            }
        });



    }

    // member_info db로 토큰 등 전송
    public class SaveAlarmActivity extends AsyncTask<String, Void, String> {

        private static final String TAG = "SaveAlarmActivity";
        //ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];
//            dep, arr, date, adlt, chld, limit)

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // TODO : 저장 시 왕복여부 포함하기
            String dept_city= (String) params[1];
            String arr_city = (String) params[2];
            String dept_date = (String) params[3];
            int adult = Integer.parseInt(params[4]);
            int child = Integer.parseInt(params[5]);
//            String airline_info = (String) params[6];
            float price_limit = Float.parseFloat(params[6]);

//            System.out.println("in" + token);

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "dept_city=" + dept_city + "&arr_city=" + arr_city + "&dept_date=" + dept_date
                    + "&adult=" + adult + "&child=" + child + "&price_limit=" + price_limit;

//            System.out.println("in" + price_limit+ adult);
            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다.

                outputStream.flush();
                outputStream.close();


                // 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {

                    // 에러 발생

                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "RegisterActivity: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

    }
}
