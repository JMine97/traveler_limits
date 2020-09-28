package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.finalexam.capstone1.MainActivity;
import com.finalexam.capstone1.MypageActivity;
import com.finalexam.capstone1.R;
import com.finalexam.capstone1.MypageAlarmsActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.finalexam.capstone1.SearchActivity.TAG;

public class PriceDistributionActivity extends Activity {

    private Button btn_save;
    ImageButton btn_home, btn_profile;
    String adlt, chld, limit, id;
    private String CurState = "SetAlarm"; //알람 조회 페이지에서 뒤로가기로 이동할 구간을 구분하기 위함

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_alarm2);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        Intent intent = getIntent();
        final String arr = intent.getStringExtra("ARRIVAL");
        final String dep = intent.getStringExtra("DEPARTURE");
        final String date = intent.getStringExtra("DATE");
        final int int_adlt = intent.getIntExtra("ADULT", 0);
        final int int_chld = intent.getIntExtra("CHILD", 0);
        final float float_limit = intent.getFloatExtra("PRICELIMIT", 0);
        final String airline = intent.getStringExtra("AIRLINE");

        btn_save = (Button) findViewById(R.id.btn_fsavealarm3);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MypageAlarmsActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("CurState", CurState);
                startActivity(intent);

                adlt= String.valueOf(int_adlt);
                chld= String.valueOf(int_chld);
                limit=String.valueOf(float_limit);

                PreferenceManager pref = new PreferenceManager(PriceDistributionActivity.this);
                id = pref.getValue("id", null);
                Log.d(TAG, "POST response code aa " + id);

                SaveAlarmActivity task = new SaveAlarmActivity();
                task.execute("http://" + "52.78.216.182" + "/insert_alarm_data.php", dep, arr, date, adlt, chld, airline, limit, id);
            }
        });

        btn_home = (ImageButton) findViewById(R.id.btn_falarm2_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        btn_profile = (ImageButton) findViewById(R.id.btn_falarm2_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MypageActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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
//            dep, arr, date, adlt, chld, airline, limit)

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String dept_city= (String) params[1];
            String arr_city = (String) params[2];
            String dept_date = (String) params[3];
            int adult = Integer.parseInt(params[4]);
            int child = Integer.parseInt(params[5]);
            String airline_info = (String) params[6];
            float price_limit = Float.parseFloat(params[7]);
            String id=(String) params[8];

//            System.out.println("in" + token);

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "dept_city=" + dept_city + "&arr_city=" + arr_city + "&dept_date=" + dept_date
                    + "&adult=" + adult + "&child=" + child + "&airline_info=" + airline_info + "&price_limit=" + price_limit+"&id="+id;

            Log.d(TAG, "POST response code aa " + id);

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
