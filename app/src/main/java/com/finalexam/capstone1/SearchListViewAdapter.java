package com.finalexam.capstone1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchListViewAdapter extends BaseAdapter {

    private ArrayList<FlightResult> list = new ArrayList<>();
    private ArrayList<Flight> list_detail = new ArrayList<>();
    private int size;
    private ListView lv_detail;
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
        lv_detail = (ListView) view.findViewById(R.id.lv_detail);

        final FlightResult lv_item = list.get(i);
        tv_air.setText(lv_item.getCarrierCode(0));
        tv_dep.setText(lv_item.getDep_time(0));
        size = lv_item.getDepCodeSize();
        tv_arr.setText(lv_item.getArr_time(size-1));

        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String formattedStringPrice = myFormatter.format(lv_item.getPrice());
        tv_pri.setText(formattedStringPrice + " 원");

        // list view click evnet
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                /*for(int i =0; i<lv_item.getDepCodeSize(); i++){
                    list_detail.add(new Flight(lv_item.getCarrierCode(i), lv_item.getDep_code(i), lv_item.getDep_time(i), lv_item.getArr_code(i), lv_item.getArr_time(i)));
                }
                ResultDetailListViewAdapter adapter_detail = new ResultDetailListViewAdapter(list_detail);
                lv_detail.setAdapter(adapter_detail);
                lv_detail.setVisibility(View.VISIBLE);*/

            }
        });

        return view;
    }
}