package com.finalexam.capstone1;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity {

    static final String TAG = "SingupActivity";

    private Button button, btn_toLogin, validateButton;
    private EditText id, password, e_mail, date_of_birth, password2;
    private String st_id, st_password, st_e_mail, st_date_of_birth, c_id;
    private String token="";
    private TextView pwCheck;
    private boolean validate = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        button=findViewById(R.id.btn_signup);
        validateButton = findViewById(R.id.validateButton);
        pwCheck = findViewById(R.id.pwCheck);
        id=findViewById(R.id.et_signup_id);
        password=findViewById(R.id.et_signup_pwd);
        password2=findViewById(R.id.et_signup_pwd2);
        e_mail=findViewById(R.id.et_signup_email);
        date_of_birth=findViewById(R.id.et_signup_dob);


        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.getText().toString().equals(password2.getText().toString())){
                    pwCheck.setText("");
                }else{
                    pwCheck.setText("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = id.getText().toString();
                if(userID.equals("")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                    dlg.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인", null).create();
                    dlg.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                                dlg.setMessage("사용할 수 있는 아이디입니다")
                                        .setPositiveButton("확인",null).create();
                                dlg.show();
                                id.setEnabled(false);
                                validate = true;
                                validateButton.setEnabled(false);
                            }
                            else{
                                AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                                dlg.setMessage("사용할 수 없는 아이디입니다")
                                        .setNegativeButton("확인", null).create();
                                dlg.show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(validateRequest);
            }
        });

        //버튼 클릭시발동
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(validate==true){
                    if(password.getText().toString().equals(password2.getText().toString())){ //비밀번호와 비밀번호 확인이 일치하는지 여부
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

                                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                // TODO : 회원가입 성공 시에만 종료되어야함
                                // TODO : 이후 마이페이지 정보 변화 및 로그아웃 기능 필요
                            }
                        });
                    }else{
                        AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                        dlg.setMessage("비밀번호가 일치하지 않습니다")
                                .setNegativeButton("확인", null).create();
                        dlg.show();
                    }
                }else{
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                    dlg.setMessage("아이디 중복확인이 필요합니다")
                            .setNegativeButton("확인", null).create();
                    dlg.show();
                }

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

    public class ValidateRequest extends StringRequest{ //ID 중복 check
        final static private String URL="http://synergyflight.dothome.co.kr/UserValidate.php";
        private Map<String, String> map;

        public ValidateRequest(String userID, Response.Listener<String>listener){
            super(Method.POST, URL, listener,null);

            map = new HashMap<>();
            map.put("userID", userID);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

}
