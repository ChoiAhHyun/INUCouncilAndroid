package com.sauce.inunion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018-08-28.
 */

public class NoticeWriteImageAdapter extends RecyclerView.Adapter<NoticeWriteImageAdapter.ViewHolder> {
    private Context mCtx;

    public List<NoticeWriteImageItem> items= null;
    public NoticeWriteImageAdapter(Context context, List<NoticeWriteImageItem> items) {
        this.mCtx=context;
        this.items=items;
    }

    @Override
    public NoticeWriteImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_write_image,null);
        return new NoticeWriteImageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoticeWriteImageAdapter.ViewHolder holder, int position) {
        final NoticeWriteImageItem item=items.get(position);

        holder.imageView.setImageURI(item.getImage());

        final int positionF = position;
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(positionF);
                notifyDataSetChanged();
                Log.i("remove","success");
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.notice_write_image_item);
            icon = (ImageView) itemView.findViewById(R.id.notice_write_image_icon);

        }

    }
}