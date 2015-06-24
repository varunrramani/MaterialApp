package com.materialapp.varunramani.materialapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Varun Ramani on 12/27/2014.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private LayoutInflater inflator;
    private Context context;
    private ClickListener clickListener;
    List<Information> data = Collections.emptyList();  //initialised as empty list so it doesn't give null pointer exception

    public Adapter(Context context, List<Information> data) {
        this.context = context;
        inflator = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_row, parent, false);
        Log.d("VR", "onCreateViewHolder Called");
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void delete(int position) {
        Log.d("VR", "inside delete....");
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        Log.d("VR", "onBindViewHolder called Current position is...." + position);
        holder.title.setText(current.strTitle);
        holder.icon.setImageResource(current.iconId);
        /*holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this,"item clicked at position...." + position, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            /*icon.setOnClickListener(this);
            title.setOnClickListener(this);*/
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(this, "item clicked at..."+getPosition(),Toast.LENGTH_LONG).show();
            //delete(getPosition());
            //context.startActivity(new Intent(context, SubActivity.class));
            if(clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
