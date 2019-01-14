package com.sauce.inunion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarScheduleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    class MyItemViewHolder extends RecyclerView.ViewHolder{

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public ArrayList<Myscheduleitem> items;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_schedule_item, parent, false);
        return new MyItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
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
                Intent intent = new Intent(view.getContext(), ScheduleContentActivity.class);
                intent.putExtra("scheduleId", myscheduleitem.scheduleId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
       CalendarScheduleRecyclerAdapter(ArrayList<Myscheduleitem> items){
        this.items = items;
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
