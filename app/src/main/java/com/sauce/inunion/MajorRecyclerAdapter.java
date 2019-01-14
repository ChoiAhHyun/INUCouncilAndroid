package com.sauce.inunion;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MajorRecyclerAdapter extends RecyclerView.Adapter<MajorRecyclerAdapter.MyitemViewHolder> {

    public String name = new String();
    boolean isChecked = false;

    class MyitemViewHolder extends RecyclerView.ViewHolder {

        TextView major_name;
        ImageView check;

        public MyitemViewHolder(@NonNull final View itemView) {
            super(itemView);
            major_name = itemView.findViewById(R.id.majorname);
            check = itemView.findViewById(R.id.check);

        }

    }

    ArrayList<Myitem> items;
    @NonNull
    @Override
    public MyitemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyitemViewHolder(v);

    }
    @Override
    public void onBindViewHolder(@NonNull final MyitemViewHolder holder, final int position) {
        final Myitem myitem = items.get(position);
        holder.major_name.setText(myitem.getName());

        holder.major_name.setText(items.get(position).majorname);
        if(items.get(position).visible == true){
            holder.check.setVisibility(View.VISIBLE);
        }
        else{
            holder.check.setVisibility(View.INVISIBLE);
        }
        holder.major_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.check.getVisibility() == View.INVISIBLE &&  isChecked == false) {
                    items.get(position).visible = true;
                    isChecked = true;
                    holder.check.setVisibility(View.VISIBLE);
                    Intent intent = new Intent("choosemajor");
                    intent.putExtra("choose","true");
                    intent.putExtra("majorname",items.get(position).majorname);
                    LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                }

                else if(holder.check.getVisibility() == View.INVISIBLE && isChecked == true){

                }
                else {
                    holder.check.setVisibility(View.INVISIBLE);
                    items.get(position).visible = false;
                    isChecked = false;
                    Intent intent = new Intent("choosemajor");
                    intent.putExtra("choose","false");
                    LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                }

            }
        });


    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    MajorRecyclerAdapter(ArrayList<Myitem> items) {
        this.items = items;
    }
}

class Myitem {
    public String majorname;
    boolean visible = false;
    public Myitem(String majorname) {
        this.majorname = majorname;
    }

    public String getName() {
        return majorname;
    }
}