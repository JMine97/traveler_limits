package com.finalexam.capstone1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchListViewAdapter extends BaseAdapter {

    private ArrayList<Flight> list = new ArrayList<>();

    public SearchListViewAdapter(ArrayList list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.f_search2_listitem , viewGroup, false);
        }

        TextView tv_air = (TextView) view.findViewById(R.id.tv_lv_airline);
        TextView tv_dep = (TextView) view.findViewById(R.id.tv_lv_dep);
        TextView tv_arr = (TextView) view.findViewById(R.id.tv_lv_arr);
        TextView tv_pri = (TextView) view.findViewById(R.id.tv_lv_price);

        Flight lv_item = list.get(i);
        tv_air.setText(lv_item.airline);
        tv_dep.setText(lv_item.dep);
        tv_arr.setText(lv_item.arr);
        tv_pri.setText(lv_item.price + " 원");

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
    String airline;
    String dep, arr;
    String price;

    Flight(String airline, String dep, String arr, String price) {
        this.airline = airline;
        this.dep = dep;
        this.arr = arr;
        this.price = price;
    }
}