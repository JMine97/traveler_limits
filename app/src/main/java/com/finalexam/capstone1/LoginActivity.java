package com.finalexam.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";
    Button btn_toSignup, btn_login;
    private EditText et_login_id, et_login_password;
    CheckBox chAuto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.d(TAG, "로그인 액티비티 실행됨");

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        chAuto = findViewById(R.id.chAutoLog);

        et_login_id = findViewById(R.id.et_login_id);
        et_login_password = findViewById(R.id.et_login_password);

        btn_login = findViewById(R.id.btn_login);

        btn_toSignup = (Button) findViewById(R.id.btn_toSignup);
        btn_toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "회원가입 to 로그인");
                Intent intent = new Intent(view.getContext(), SignupActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String id = et_login_id.getText().toString();
                String password = et_login_password.getText().toString();

                Response.Listener<String> resposeListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success){
                                String id = jsonObject.getString("id");
                                String password = jsonObject.getString("password");
                                String e_mail = jsonObject.getString("e_mail");
                                String date_of_birth = jsonObject.getString("date_of_birth");

                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                PreferenceManager pref = new PreferenceManager(LoginActivity.this);
                                pref.put("id", id);
                                pref.put("password", password);
                                pref.put("e_mail", e_mail);
                                pref.put("date_of_birth", date_of_birth);
                                if(chAuto.isChecked()){
                                    pref.put("auto", true);
                                }
                                else{
                                    pref.put("auto", false);
                                }
                                Intent intent = new Intent(LoginActivity.this, MypageActivity.class);
                                /*intent.putExtra("id", id);
                                intent.putExtra("password", password);
                                intent.putExtra("e_mail", e_mail);
                                intent.putExtra("date_of_birth", date_of_birth);
                                 */
                                startActivity(intent);
                                finish();
                            } else{
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(id, password, resposeListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

}
