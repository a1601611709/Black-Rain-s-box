package com.example.gaminglife;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 偏执 on 2018/6/19.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private List<Picture> mPictureList;

    private ArrayList<Integer> oldPictureId;

    private List<Integer> newPictureId;

    private User user1;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View pictureView;
        ImageView pictureImage;
        Button pictureButton;

        public ViewHolder(View view){
            super(view);
            pictureView=view;
            pictureImage=(ImageView) view.findViewById(R.id.picture_image);
            pictureButton=(Button)view.findViewById(R.id.picture_button);
        }
    }

    public PictureAdapter(List<Picture> pictureList){
        mPictureList=pictureList;
        oldPictureId=new ArrayList<Integer>();
        newPictureId=new ArrayList<Integer>();
        for(int i=0;i<20;i++){
            Integer temp1=R.drawable.npt1+i;
            oldPictureId.add(temp1);
            Integer temp2=R.drawable.pt1+i;
            newPictureId.add(temp2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.pictureView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Picture picture=mPictureList.get(position);
                Intent intent=new Intent(v.getContext(),PictureActivity.class);
                intent.putExtra("pitcureId_2",picture.getImageId());
                v.getContext().startActivity(intent);
            }
        });
        holder.pictureImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Picture picture=mPictureList.get(position);
                Intent intent=new Intent(v.getContext(),PictureActivity.class);
                intent.putExtra("pitcureId_1",picture.getImageId());
                v.getContext().startActivity(intent);
            }
        });
        holder.pictureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final int position=holder.getAdapterPosition();
                user1= DataSupport.find(User.class,1);
                if(user1==null){
                    user1=new User(1,"NewName");
                    user1.setId(1);
                    user1.save();
                }
                user1.setPictures(user1.getPictureData());
                if(user1.getPictures().get(position).isPurchase()){

                }
                else{
                    if(user1.getCoins()==0){
                        Toast.makeText(v.getContext(),"You don't have enough coins",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        final Context context1=v.getContext();
                        AlertDialog.Builder dialog=new AlertDialog.Builder(v.getContext());
                        dialog.setMessage("Are you sure to purchase ?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user1.setCoins(user1.getCoins()-1);
                                Picture picture=mPictureList.get(position);
                                picture.setImageId(newPictureId.get(position));
                                picture.setPurchase(true);
                                picture.update(picture.getId());
                                user1.getPictures().set(position,picture);
                                user1.save();
                                Intent intent=new Intent(context1,ListPictureActivity.class);
                                context1.startActivity(intent);
                            }
                        });
                        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    }

                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture=mPictureList.get(position);
        holder.pictureImage.setImageResource(picture.getImageId());
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }

}
