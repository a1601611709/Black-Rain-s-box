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
 * Created by 偏执 on 2018/5/20.
 */
//任务适配器，暂时不用理解
public class TaskAdapter extends ArrayAdapter<Goal> {

    private int resourceId;

    public TaskAdapter(Context context, int textViewResourceId, List<Goal> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //显示目标数据
        Goal goal=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.taskName=(TextView)view.findViewById(R.id.noteName);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();

        }
        TextView goalName=(TextView)view.findViewById(R.id.noteName);
        goalName.setText(goal.toString1());
        return view;
    }
    class ViewHolder{
        TextView taskName;
    }
}
