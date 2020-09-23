package com.finalexam.capstone1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    static final String TAG = "SearchActivity"; // DEBUG

    private Button btn_dep, btn_arr, btn_date, btn_search, btn_minus1, btn_plus1, btn_minus2, btn_plus2;
    private TextView tv_adlt, tv_chld;
    private ImageButton btn_home, btn_profile;

    private Airport dep, arr;
    final Calendar cal_today = Calendar.getInstance();  // 오늘 날짜
    int     y = cal_today.get(cal_today.YEAR),
            m = cal_today.get(cal_today.MONTH),
            d = cal_today.get(cal_today.DATE);
    int num_adlt = 1, num_chld = 0;             // 인원

    private static List<Airport> list; // 원본 리스트

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        Intent intent = getIntent();

        try {
            String dep = intent.getStringExtra("AIRPORT_kr");
            btn_dep.setText(dep);
        } catch (Exception e) {
            Log.d(TAG, "첫 실행, 인텐트 출발정보 전달 X");
        }

        list = new ArrayList<Airport>();
        settingList();

        btn_dep = (Button) findViewById(R.id.btn_fsearch_dep);
        btn_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AirportDialog oDialog = new AirportDialog(view.getContext(), list);
                oDialog.show();
                oDialog.callFunction(btn_dep);
            }
        });

        btn_arr = (Button) findViewById(R.id.btn_fsearch_arr);
        btn_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AirportDialog oDialog = new AirportDialog(view.getContext(), list);
                oDialog.show();
                oDialog.callFunction(btn_arr);
            }
        });

        btn_date = (Button)findViewById(R.id.btn_fsearch_date);
        if(m<9){
            if(d<10){
                btn_date.setText(String.valueOf(y + "-0" + (m + 1) + "-0" + d));
            }
            else{
                btn_date.setText(String.valueOf(y + "-0" + (m + 1) + "-" + d));
            }
        }else{
            if(d<10){
                btn_date.setText(String.valueOf(y + "-" + (m + 1) + "-0" + d));
            }
            else{
                btn_date.setText(String.valueOf(y + "-" + (m + 1) + "-" + d));
            }
        }
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 달력 -> 미래만 선택 가능
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar c = Calendar.getInstance();
                        c.set(i, i1, i2);
                        if (c.compareTo(cal_today) == -1) { // 과거 날짜 선택 시
                            Toast.makeText(view.getContext(), "불가능한 날짜입니다.", Toast.LENGTH_SHORT).show();
                        } else {    // 오늘 이후 날짜 선택 시
                            y = i;
                            m = i1;
                            d = i2;
                            if(m<9){
                                if(d<10){
                                    btn_date.setText(String.valueOf(y + "-0" + (m + 1) + "-0" + d));
                                } else{
                                    btn_date.setText(String.valueOf(y + "-0" + (m + 1) + "-" + d));
                                }
                            }else{
                                if(d<10){
                                    btn_date.setText(String.valueOf(y + "-" + (m + 1) + "-0" + d));
                                }else{
                                    btn_date.setText(String.valueOf(y + "-" + (m + 1) + "-" + d));
                                }
                            }

                        }
                    }
                }, y, m, d);    // 오늘 날짜로 초기화, 이후 설정된 날짜로 초기화
//                datePickerDialog.setMessage("메시지 작성");
                datePickerDialog.show();
            }
        });

        tv_adlt = (TextView)findViewById(R.id.tv_fsearch_adlt);
        btn_minus1 = (Button)findViewById(R.id.btn_fsearch_minus1);
        btn_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num_adlt > 0) {
                    tv_adlt.setText(String.valueOf(--num_adlt));
                }
            }
        });
        btn_plus1 = (Button)findViewById(R.id.btn_fsearch_plus1);
        btn_plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_adlt.setText(String.valueOf(++num_adlt));
            }
        });

        tv_chld = (TextView) findViewById(R.id.tv_fsearch_chld);
        btn_minus2 = (Button)findViewById(R.id.btn_fsearch_minus2);
        btn_minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num_chld > 0) {
                    tv_chld.setText(String.valueOf(--num_chld));
                }
            }
        });
        btn_plus2 = (Button)findViewById(R.id.btn_fsearch_plus2);
        btn_plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_chld.setText(String.valueOf(++num_chld));
            }
        });

        btn_search = (Button)findViewById(R.id.b_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchResultActivity.class);
                // 출발지, 도착지, 날짜, 인원 전달
                String dep = String.valueOf(btn_dep.getText());
                String arr = String.valueOf(btn_arr.getText());
                intent.putExtra("DEPARTURE", dep);
                intent.putExtra("ARRIVAL", arr);
                if(m<9){
                    if(d<10){
                        intent.putExtra("DATE", y + "-0"+(m + 1) + "-0" + d);
                    }else{
                        intent.putExtra("DATE", y + "-0"+(m + 1) + "-" + d);
                    }
                }else{
                    if(d<10){
                        intent.putExtra("DATE", y + "-"+(m + 1) + "-0" + d);
                    }else{
                        intent.putExtra("DATE", y + "-"+(m + 1) + "-" + d);
                    }
                }

                intent.putExtra("ADULT", num_adlt);
                intent.putExtra("CHILD", num_chld);

//                intent.putExtra("TRAVEL", dep + "/" + arr + "/" + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void settingList() {
        // 대한민국
        list.add(new Airport("ICN", "서울 인천국제공항", "대한민국 서울/인천"));
        list.add(new Airport("GMP", "서울 김포국제공항", "대한민국 서울"));
        list.add(new Airport("PUS", "부산 김해공항", "대한민국 부산"));
        list.add(new Airport("CJU", "제주공항", "대한민국 제주"));

        // 동북아시아
        list.add(new Airport("NRT", "일본 도쿄 나리타 공항", "일본 도쿄"));
        list.add(new Airport("HND", "일본 도쿄 하네다 공항", "일본 도쿄"));
        list.add(new Airport("KIX", "일본 오사카 칸사이 공항"));
        list.add(new Airport("PEK", "중국 베이징 셔우뚜 공항"));
        list.add(new Airport("PVG", "중국 상하이 푸동 공항"));
        list.add(new Airport("HKG", "홍콩 첵랍콕 공항"));
        list.add(new Airport("TPE", "대만 타오위안 공항"));
        list.add(new Airport("ULN", "몽골 울란바토르 칭기즈칸 공항"));

        // 동남아시아
        list.add(new Airport("CGK", "인도네시아 자카르타 수카르노 하타 공항"));
        list.add(new Airport("PNH", "캄보디아 프놈펜 공항"));
        list.add(new Airport("HAN", "베트남 하노이 공항"));
        list.add(new Airport("VTE", "라오스 비엔티안 왓따이 공항"));
        list.add(new Airport("DEL", "인도 델리 인디라 간디 공항"));
        list.add(new Airport("CMB", "스리랑카 콜롬보 반다라나이케 공항"));
        list.add(new Airport("KUL", "말레이시아 쿠알라룸프르 공항"));
        list.add(new Airport("BKK", "태국 방콕 공항"));
        list.add(new Airport("MNL", "필리핀 마닐라 니노이 아키노 공항"));

        // 서남아시아
        list.add(new Airport("AUH", "UAE 아부다비 공항"));
        list.add(new Airport("DXB", "UAE 두바이 공항"));
        list.add(new Airport("DOH", "카타르 도하 공항"));
        list.add(new Airport("IST", "타키 이스탄불 아타튀르크 공항"));

        // 유럽
        list.add(new Airport("LHR", "영국 런던 히드로 공항"));
        list.add(new Airport("LGW", "영국 런던 가크윅 공항"));
        list.add(new Airport("CDG", "프랑스 파리 샤를 드 골 공항"));
        list.add(new Airport("ORY", "프랑스 파리 오를리 공항"));
        list.add(new Airport("FRA", "독일 프랑크푸르트 공항"));
        list.add(new Airport("MAD", "스페인 마드리드 공항"));
        list.add(new Airport("FCO", "이탈리아 로마 레오나르도 다 빈치 공항"));
        list.add(new Airport("DME", "러시아 모스크바 도모데오보 공항"));
        list.add(new Airport("SVO", "러시아 모스크바 셰레메쪠보 공항"));
        list.add(new Airport("TAS", "우즈베키스탄 타슈켄트 공항"));
        list.add(new Airport("HEL", "핀란드 헬싱키 공항"));
        list.add(new Airport("AMS", "네덜란드 암스테르담 스키폴 공항"));

        // 아프리카
        list.add(new Airport("CMN", "모로코 카사블랑카 모하메드 5세 공항"));
        list.add(new Airport("RBA", "모로코 라밧 살레 공항"));
        list.add(new Airport("TUN", "튀니지 튀니스 공항"));
        list.add(new Airport("ADD", "에티오피아 아디스아바바 볼레 공항"));
        list.add(new Airport("CAI", "이집트 카이로 공항"));
        list.add(new Airport("NBO", "케냐 나이로비 공항"));
        list.add(new Airport("TIP", "리비아 트리폴리 공항"));
        list.add(new Airport("KGL", "르완다 키갈리 공항"));
        list.add(new Airport("DAR", "탄자니아 다르에스살람 공항"));
        list.add(new Airport("JNB", "남아프리카공화국 조하네스버스 공항"));
        list.add(new Airport("NSI", "카메룬 아운데 공항"));
        list.add(new Airport("DKR", "세네갈 다카 공항"));

        // 북미
        list.add(new Airport("SEA", "미국 시애틀 공항"));
        list.add(new Airport("SFO", "미국 샌프란시스코 공항"));
        list.add(new Airport("LAX", "미국 로스앤젤레스 공항"));
        list.add(new Airport("LAS", "미국 라스베이거스 공항"));
        list.add(new Airport("ORD", "미국 시카고 오헤어 공항"));
        list.add(new Airport("MDW", "미국 시카고 미드웨이 공항"));
        list.add(new Airport("JFK", "미국 뉴욕 존F 케네디 공항"));
        list.add(new Airport("LGA", "미국 뉴욕 라구아디아 공항"));
        list.add(new Airport("DTT", "미국 디트로이트 공항"));
        list.add(new Airport("ATL", "미국 애틀란타 공항"));
        list.add(new Airport("MIA", "미국 마이애미 공항"));
        list.add(new Airport("YVR", "캐나다 벤쿠버 공항"));
        list.add(new Airport("YYZ", "캐나다 토론토 피어슨 공항"));
        list.add(new Airport("YUL", "캐나다 몬트리올 공항"));
    }

    public static List<Airport> getList() {
        return list;
    }
}