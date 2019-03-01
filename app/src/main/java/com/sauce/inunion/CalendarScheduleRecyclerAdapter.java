package com.sauce.inunion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarScheduleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    class MyItemViewHolder extends RecyclerView.ViewHolder{

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public ArrayList<Myscheduleitem> items;
    private FragmentManager fragmentManager;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_schedule_item, parent, false);
        return new MyItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            final Myscheduleitem myscheduleitem = items.get(i);
        TextView starttime = viewHolder.itemView.findViewById(R.id.time);
        starttime.setText(myscheduleitem.starttime);
        TextView scheduleTitle = viewHolder.itemView.findViewById(R.id.scheduleTitle);
        scheduleTitle.setText(myscheduleitem.scheduleTitle);
        TextView memo = viewHolder.itemView.findViewById(R.id.memo);
        memo.setText(myscheduleitem.memo);

        LinearLayout linearLayout = viewHolder.itemView.findViewById(R.id.layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("scheduleId", myscheduleitem.scheduleId);

                ScheduleContentManager fragment= new ScheduleContentManager();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        Button buttonDelete = viewHolder.itemView.findViewById(R.id.btnDeleteSchedule);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent("click_delete");
                intent2.putExtra("delete_position", i+"");
                Log.d("calendar", i+"");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
       CalendarScheduleRecyclerAdapter(ArrayList<Myscheduleitem> items, Context context, FragmentManager fragmentManager){
        this.items = items;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }
     static class Myscheduleitem {
        public String scheduleTitle;
        public String starttime;
         public String scheduleId;
         public String memo;

        public Myscheduleitem(String scheduleTitle, String starttime, String scheduleId, String memo) {
            this.scheduleTitle = scheduleTitle;
            this.starttime = starttime;
            this.scheduleId = scheduleId;
            this.memo = memo;

        }
    }

}
