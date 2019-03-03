package com.sauce.inunion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class MajorRecyclerAdapter extends RecyclerView.Adapter<MajorRecyclerAdapter.MyitemViewHolder> {

    public String name = new String();

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

        holder.major_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("major", "");
                Intent intent = new Intent("choosemajor");
                if(holder.check.getVisibility() == View.INVISIBLE ) {
                    holder.check.setVisibility(View.VISIBLE);
                    intent.putExtra("choose","true");
                    intent.putExtra("majorname",items.get(position).majorname);
                    LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                }
                else {
                    holder.check.setVisibility(View.INVISIBLE);
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
    public Myitem(String majorname) {
        this.majorname = majorname;
    }

    public String getName() {
        return majorname;
    }
}