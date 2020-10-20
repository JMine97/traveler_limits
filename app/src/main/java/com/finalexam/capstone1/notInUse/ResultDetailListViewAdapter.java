package com.finalexam.capstone1.notInUse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.finalexam.capstone1.Flight;
import com.finalexam.capstone1.R;

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

