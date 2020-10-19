package com.finalexam.capstone1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultDetailListViewAdapter extends BaseAdapter {


    private ArrayList<Flight> list = new ArrayList<>();

    public ResultDetailListViewAdapter(ArrayList list) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_result_detail_listitem , viewGroup, false);
        }

        TextView tv_air = (TextView) view.findViewById(R.id.tv_rd_carriercode);
        TextView tv_dep = (TextView) view.findViewById(R.id.tv_rd_dep);
        TextView tv_dep_time = (TextView) view.findViewById(R.id.tv_rd_dep_time);
        TextView tv_arr = (TextView) view.findViewById(R.id.tv_rd_arr);
        TextView tv_arr_time = (TextView) view.findViewById(R.id.tv_rd_arr_time);

        Flight lv_item = list.get(i);
        tv_air.setText(lv_item.getCarrierCode());
        tv_dep.setText(lv_item.getDep_code());
        tv_dep_time.setText(lv_item.getDep_time());
        tv_arr.setText(lv_item.getArr_code());
        tv_arr_time.setText(lv_item.getArr_time());

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

class Flight {
    String carrierCode;
    String dep_code;
    String dep_time;
    String arr_code;
    String arr_time;

    public String getCarrierCode() {
        return carrierCode;
    }

    public String getDep_code() {
        return dep_code;
    }

    public String getDep_time() {
        return dep_time;
    }

    public String getArr_code() {
        return arr_code;
    }

    public String getArr_time() {
        return arr_time;
    }

    public Flight(String carrierCode, String dep_code, String dep_time, String arr_code, String arr_time){
        this.carrierCode = carrierCode;
        this.dep_code = dep_code;
        this.dep_time = dep_time;
        this.arr_code = arr_code;
        this.arr_time = arr_time;
    }
}
