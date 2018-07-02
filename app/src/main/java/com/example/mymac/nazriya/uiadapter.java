package com.example.mymac.nazriya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MYMAC on 21-05-2018.
 */

public class uiadapter extends BaseAdapter {
    TextView message_body,message_body2;
    Context context;
    ArrayList arrayList;
    public uiadapter(
            Context context,ArrayList arrayList
    )
    {
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatlayout,null);
        RelativeLayout layout = view.findViewById(R.id.mainlayout);
        RelativeLayout layout2 = view.findViewById(R.id.mainlayout2);
        message_body2=  view.findViewById(R.id.message_body2);
       message_body= view.findViewById(R.id.message_body);
       Data d = (Data) arrayList.get(position);
       if (d.who==0){
           layout2.setBackgroundResource(R.drawable.bubb);
       }else{
           layout.setBackgroundResource(R.drawable.bubb2);
       }
       message_body.setText(d.message);
        message_body2.setText(d.time);
        return view;
    }
}
