package com.finalexam.capstone1;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends Activity {

    static final String TAG = "SingupActivity";

    Button button, btn_toLogin;
    EditText id, password, e_mail, date_of_birth;
    String st_id, st_password, st_name, st_e_mail, st_date_of_birth;
    String token="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        button=findViewById(R.id.btn_signup);

        id=findViewById(R.id.et_signup_id);
        password=findViewById(R.id.et_signup_pwd);
        e_mail=findViewById(R.id.et_signup_email);
        date_of_birth=findViewById(R.id.et_signup_dob);

        //버튼 클릭시발동
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                        st_id=id.getText().toString();
                        st_password=password.getText().toString();
                        st_e_mail=e_mail.getText().toString();
                        st_date_of_birth=date_of_birth.getText().toString(); //날짜형태

                        token = instanceIdResult.getToken();
                        // Do whatever you want with your token now
                        // i.e. store it on SharedPreferences or DB
                        // or directly send it to server

                        Log.d("FCM Log", "FCM 토큰: " + token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
//                System.out.println("outt" + token);

                        // member_info db로 전송
                        RegisterActivity task = new RegisterActivity();
//                task.execute("http://" + IP_ADDRESS + "/insert.php", name,country);
                        task.execute("http://" + "synergyflight.dothome.co.kr" + "/insert_member_info.php", st_id, st_e_mail, st_date_of_birth, st_password, token);

                        Intent intent = new Intent(v.getContext(), MypageActivity.class);
                        intent.putExtra("st_id", st_id);
                        intent.putExtra("st_email", st_e_mail);
                        intent.putExtra("st_date_of_birth", st_date_of_birth);
                        setResult(2000, intent);
                        SignupActivity.this.finish();
                        // TODO : 회원가입 성공 시에만 종료되어야함
                        // TODO : 이후 마이페이지 정보 변화 및 로그아웃 기능 필요
                    }
                });

            }});

        btn_toLogin = (Button) findViewById(R.id.btn_toLogin);
        btn_toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "회원가입 to 로그인");
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });




    } //onCreate

    // member_info db로 토큰 등 전송
    public class RegisterActivity extends AsyncTask<String, Void, String> {

        private static final String TAG = "RegisterActivity";
        //ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String id = (String) params[1];
            String e_mail = (String) params[2];
            String date_of_birth = (String) params[3];
            String password = (String) params[4];
            String token = (String) params[5];

//            System.out.println("in" + token);

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "id=" + id + "&e_mail=" + e_mail + "&date_of_birth=" + date_of_birth
                    + "&password=" + password + "&token=" + token;

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
