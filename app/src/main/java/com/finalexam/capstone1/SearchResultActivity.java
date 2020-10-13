package com.finalexam.capstone1;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends BaseActivity {

    private TextView tv_date, tv_dep, tv_dep_kr, tv_arr, tv_arr_kr, tv_noResult;
    private Button btn_save;
    private ImageButton btn_home, btn_profile;
    private ListView lv_search;
    private ArrayList<FlightResult> list;
    private String id;
    private ProgressBar progressBar; //로딩
    private LinearLayout progress_layout;
    //항공권
    private String arr, dep, date;
    private int adlt, chld;
    private FlightResult[] flightResults;
    private ArrayList<Integer> price = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

//        Intent intent = getIntent();
        PreferenceManager pref = new PreferenceManager(this);
        arr = pref.getValue("ARRIVAL", null);
        dep = pref.getValue("DEPARTURE", null);
        date = pref.getValue("DATE", null);
        adlt = pref.getValue("ADULT", 0);
        chld = pref.getValue("CHILD", 0);

//        Log.d("resultof", arr + dep + date + adlt + chld);

        tv_date = (TextView)findViewById(R.id.tv_fsearch_date);
        tv_date.setText(date);
        tv_dep = (TextView)findViewById(R.id.tv_fsearch_dep);
        tv_dep.setText(dep);
        tv_dep_kr = (TextView)findViewById(R.id.tv_fsearch_dep_kr);
        for (Airport a : SearchActivity.getList()) {
            if (a.getName_en().equals(dep)) tv_dep_kr.setText(a.getCity());
        }
        tv_arr = (TextView)findViewById(R.id.tv_fsearch_arr);
        tv_arr.setText(arr);
        tv_arr_kr = (TextView)findViewById(R.id.tv_fsearch_arr_kr);
        for (Airport a : SearchActivity.getList()) {
            if (a.getName_en().equals(arr)) tv_arr_kr.setText(a.getCity());
        }

        progressBar = findViewById(R.id.progress_bar);

        lv_search = (ListView) findViewById(R.id.lv_search);
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Flight> list_detail = new ArrayList<>();
                FlightResult lv_item = list.get(position);
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultActivity.this);

                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                LayoutInflater inflater = getLayoutInflater();
                View d_view = inflater.inflate(R.layout.dialog_result_detail,null);
                builder.setView(d_view);
                final ListView lv_detail = (ListView)view.findViewById(R.id.lv_detail);
                final AlertDialog dialog = builder.create();
                for(int i =0; i<lv_item.getDepCodeSize(); i++){
                    list_detail.add(new Flight(lv_item.getCarrierCode(i), lv_item.getDep_code(i), lv_item.getDep_time(i), lv_item.getArr_code(i), lv_item.getArr_time(i)));
                }
                ResultDetailListViewAdapter rd_adapter = new ResultDetailListViewAdapter(list_detail);
                lv_detail.setAdapter(rd_adapter);
                dialog.show();*/

            }
        });


        tv_noResult = (TextView)findViewById(R.id.tv_noResult);
        //test_list();
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
        progressON();


// 출발, 도착, 날짜, 인원 수 데이터 넘겨줌
        btn_save = (Button)findViewById(R.id.b_result);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                PreferenceManager pref = new PreferenceManager(SearchResultActivity.this);
                id = pref.getValue("id", null);

                if(id!=null) {
                    Intent intent = new Intent(view.getContext(), SetAlarmDetailActivity.class);
//                    intent.putExtra("DEPARTURE", dep);
//                    intent.putExtra("ARRIVAL", arr);
//                    intent.putExtra("DATE", date);
//                    intent.putExtra("ADULT", adlt);
//                    intent.putExtra("CHILD", chld);
                    startActivity(intent);
                }
                else{
                    //Toast.makeText(getApplicationContext(), "먼저 로그인해주세요", Toast.LENGTH_SHORT).show();

                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(SearchResultActivity.this);
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            startActivity(intent);


                        }
                    });
                    alert.setMessage("먼저 로그인해주세요");
                    alert.show();

                    // 로그인 화면 연결 필요할지? Toast X Dialog 로 바꾸기->dialog로 로그인 필요 표시 후 메인으로 넘어감
                }
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, FlightResult[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(FlightResult[] flightResults) {
            list = new ArrayList<FlightResult>();
            if(flightResults!=null){
                for(int i=0; i<flightResults.length; i++){
                    int size = flightResults[i].getDepCodeSize();
                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    String formattedStringPrice = myFormatter.format(flightResults[i].getPrice());

                    list.add(new FlightResult(flightResults[i].getDep_code(), flightResults[i].getDep_time(),flightResults[i].getArr_code(),
                            flightResults[i].getArr_time(),flightResults[i].getCarrierCode(), flightResults[i].getTotalTime(), flightResults[i].getPrice()));
                    price.add(flightResults[i].getPrice());
                }
                JsonArray jsonArray = new JsonArray();
                for (int i =0; i<price.size(); i++){
                    jsonArray.add(price.get(i));
                }
                //Log.d("json", jsonArray.toString());
                SearchListViewAdapter adapter = new SearchListViewAdapter(list);
                lv_search.setAdapter(adapter);
                lv_search.setVisibility(View.VISIBLE);

                PreferenceManager pref = new PreferenceManager(SearchResultActivity.this);
                pref.put("PRICEJSON", jsonArray.toString());
            }

            progressOFF();
        }

        @Override
        protected FlightResult[] doInBackground(Void... voids) {
            try {
                Amadeus amadeus = Amadeus
                        .builder("kFN2xdf3AsPrita2tUv5HWUeXcvM6fdL", "q92QKuEUEuIFbzDd")
                        .build();

//                Log.d("resultof", arr + dep + date + adlt + chld);
                // Flight Choice Prediction
// Note that the example calls 2 APIs: Flight Offers Search & Flight Choice Prediction
                FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                        Params.with("originLocationCode", dep)
                                .and("destinationLocationCode", arr)
                                .and("departureDate", date)
                                //.and("returnDate", "2020-11-09")
                                .and("adults", adlt).and("children", chld).and("currencyCode", "KRW"));

                // Using a JSonObject
                JsonObject result = flightOffers[1].getResponse().getResult();
                JsonArray result2 = (JsonArray) result.get("data"); //항공권 정보
                flightResults = new FlightResult[result2.size()];
                for(int i=0; i<result2.size(); i++){

                    JsonObject result3 = (JsonObject)result2.get(i); //oneway(?), price
                    JsonObject price = (JsonObject)result3.get("price");
                    double price_total1 = Double.parseDouble(String.valueOf(price.get("total")).substring(1,String.valueOf(price.get("total")).length()-1));
                    int price_total = Integer.parseInt(String.valueOf(Math.round(price_total1)));
                    JsonArray result4 = (JsonArray)result3.get("itineraries");
                    JsonObject result5 = (JsonObject)result4.get(0); //왕복일때 size 재야함(for문)

                    String totalTime = String.valueOf(result5.get("duration")).substring(3,String.valueOf(result5.get("duration")).length()-1);//총비행시간

                    JsonArray result6 = (JsonArray)result5.get("segments"); //경유일 때 구분 for문 필요
                    flightResults[i]= new FlightResult(result6.size());

                    flightResults[i].setPrice(price_total);
                    flightResults[i].setTotalTime(totalTime);

                    for(int j=0; j<result6.size(); j++){
                        JsonObject result7 = (JsonObject)result6.get(j);
                        JsonObject departure = (JsonObject)result7.get("departure");
                        String departure_code = String.valueOf(departure.get("iataCode")).substring(1,4);
                        flightResults[i].setDep_code(departure_code, j);
                        String departure_time = String.valueOf(departure.get("at")).substring(12,17);
                        flightResults[i].setDep_time(departure_time, j);

                        JsonObject arrival = (JsonObject)result7.get("arrival");
                        String arrival_code = String.valueOf(arrival.get("iataCode")).substring(1,4);
                        flightResults[i].setArr_code(arrival_code, j);
                        String arrival_time = String.valueOf(arrival.get("at")).substring(12,17);
                        flightResults[i].setArr_time(arrival_time, j);

                        String carrierCode = String.valueOf(result7.get("carrierCode")).substring(1,3);
                        flightResults[i].setCarrierCode(carrierCode, j);
                    }





                    //list.add(new Flight(carrierCode, departure_time, arrival_time, Double.toString(price_total)));
                }

                Log.d("resultof", String.valueOf(result));
                Log.d("flight_length", Integer.toString(flightResults.length));
            } catch (ResponseException e){
                e.printStackTrace();
            }
            return flightResults;
        }
    }

    /*void test_list() {
        // 완성필요~~~~~
        list = new ArrayList<Flight>();
        list.add(new Flight("Air Seoul", "06:00", "07:00", "21,403"));
        list.add(new Flight("Asiana", "06:05", "07:35", "30,000"));
        list.add(new Flight("Air Jeju", "12:15", "13:35", "75,000"));
        list.add(new Flight("Jin Air", "13:45", "15:00", "28,900"));
        list.add(new Flight("Korean Air", "15:00", "16:10", "30,000"));
        list.add(new Flight("Air Busan", "17:45", "18:55", "22,500"));
        list.add(new Flight("Air Busan", "18:10", "19:20", "30,520"));
        list.add(new Flight("tway", "18:10", "19:20", "23,500"));
        list.add(new Flight("Air Busan", "18:50", "20:00", "24,000"));
        list.add(new Flight("Air Busan", "18:55", "20:05", "91,000"));
        list.add(new Flight("tway", "19:00", "20:10", "21,500"));
        list.add(new Flight("Korean Air", "20:30", "21:40", "30,000"));
        list.add(new Flight("Asiana", "21:20", "22:30", "30,500"));
    }*/

}
