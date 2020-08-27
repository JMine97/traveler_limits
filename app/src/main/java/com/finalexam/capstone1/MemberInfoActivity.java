package com.finalexam.capstone1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MemberInfoActivity extends AppCompatActivity {
    private TextView info_id, info_birthday, info_email;
    private EditText info_pw;
    private String id, email, birth, password;
    private Button withdraw;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_memberinfo);
        info_id = findViewById(R.id.info_id);
        info_pw = findViewById(R.id.info_pw);
        info_email = findViewById(R.id.info_email);
        info_birthday = findViewById(R.id.info_birthday);
        withdraw = findViewById(R.id.withdraw);

        PreferenceManager pref = new PreferenceManager(this);
        id = pref.getValue("id", null);
        email = pref.getValue("e_mail", null);
        birth = pref.getValue("date_of_birth", null);
        password = pref.getValue("password", null);

        if(id!=null) info_id.setText(id);
        if(email!=null) info_email.setText(email);
        if(birth!=null) info_birthday.setText(birth);

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MemberInfoActivity.this);
                alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DeleteActivity task = new DeleteActivity();
                        task.execute("http://" + "synergyflight.dothome.co.kr" + "/delete_member_info.php", id, password);


                        PreferenceManager pref = new PreferenceManager(MemberInfoActivity.this);
                        pref.clear();
                        Intent intent = new Intent(MemberInfoActivity.this, MypageActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
                alert.setMessage("정말 탈퇴하시겠습니까?");
                alert.show();
            }
        });
    }

    public class DeleteActivity extends AsyncTask<String, Void, String> {

        private static final String TAG = "DeleteActivity";
        //ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String id = (String) params[1];
            String password = (String) params[2];

//            System.out.println("in" + token);

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "id=" + id +"&password=" + password;

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

                Log.d(TAG, "DeleteActivity: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

    }
}
