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
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;

import static com.kakao.usermgmt.StringSet.id;
import static com.kakao.usermgmt.StringSet.profile;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;
    static final String TAG = "LoginActivity";
    Button btn_toSignup, btn_login;
    private EditText et_login_id, et_login_password;
    CheckBox chAuto;
    String CurState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.d(TAG, "로그인 액티비티 실행됨");

        Intent intent = getIntent();
        CurState = intent.getStringExtra("CurState");   // FromHome or FromAlarm

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);


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
                intent.putExtra("CurState", CurState);
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


                                // CurState 에 따라서 홈화면 or 검색결과화면 연결
                                switch (CurState){
                                    case "FromHome":
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("password", password);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "FromAlarm":
                                        intent = new Intent(LoginActivity.this, SearchResultActivity.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("password", password);
//                                /*intent.putExtra("id", id);
//                                intent.putExtra("e_mail", e_mail);
//                                intent.putExtra("date_of_birth", date_of_birth);
//                                 */
//                                startActivity(intent);
//                                finish();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    class SessionCallback implements ISessionCallback {
        String tag = "KAKAO_API";
        @Override
        public void onSessionOpened() {
            requestMe();
            Intent intent = new Intent(LoginActivity.this, MypageActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : "+exception.getMessage());
        }

        public void requestMe(){
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e(tag, "세션이 닫혀 있음: "+errorResult);
                }

                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e(tag, "사용자 정보 요청 실패: " + errorResult);
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Log.i(tag, "사용자 아이디: "+result.getId());

                    UserAccount kakaoAccount = result.getKakaoAccount();
                    if(kakaoAccount != null){
                        //email
                        String email = kakaoAccount.getEmail();

                        if(email != null){
                            Log.i("KAKAO_API", "emil: " + email);
                        }
                    } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE){
                        //동의 요청 후 이메일 획득 가능
                        //서비스 이용 시나리오 상에서 반드시 필요한 경우에만
                    } else{
                        //이메일 획득 불가
                    }

                    //profile
                    Profile profile = kakaoAccount.getProfile();

                    if(profile != null){
                        Log.d(tag, "nickname: " + profile.getNickname());
                        Log.d(tag, "profile image: " + profile.getProfileImageUrl());
                        Log.d(tag, "thumbnail image: "+profile.getThumbnailImageUrl());
                        PreferenceManager pref = new PreferenceManager(LoginActivity.this);
                        pref.put("id", profile.getNickname());

                    }else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE){
                        //동의 요청 후 프로필 획득
                    }else{
                        //프로필 획득 불가
                    }
                }
            });
        }

    }
}