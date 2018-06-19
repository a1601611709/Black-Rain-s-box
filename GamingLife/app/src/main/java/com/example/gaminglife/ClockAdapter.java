package com.example.gaminglife;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 偏执 on 2018/5/21.
 */
//列表适配器。，暂时不用理解
public class ClockAdapter extends ArrayAdapter<Incident> {
    private int resourceId;
    public ClockAdapter(Context context, int textViewResourceId, List<Incident> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Incident incident=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.ClockName=(TextView)view.findViewById(R.id.clock_name);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.ClockName.setText(incident.toString());
        return view;
    }

    class ViewHolder{
        TextView ClockName;
    }
}
