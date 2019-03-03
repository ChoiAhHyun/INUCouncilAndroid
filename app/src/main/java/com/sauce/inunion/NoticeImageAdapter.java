package com.sauce.inunion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class NoticeImageAdapter extends RecyclerView.Adapter<NoticeImageAdapter.ViewHolder> {
    private Context mCtx;

    public ArrayList<NoticeImageItem> items = new ArrayList<>();
    public NoticeImageAdapter(Context context) {
        this.mCtx=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_detail_image_recyclerview,null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NoticeImageItem item=items.get(position);
//        Log.w("파일",position +", " + item.getImage());
        Glide.with(mCtx)
                .load("http://117.16.231.66:7001/imgload/" + item.getImage())
                .into(holder.imageView);
//        Picasso.get().load(item.getImage()).into(holder.imageView);
//        holder.imageView.setImageBitmap(item.getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(NoticeImageItem item){
        items.add(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.notice_detail_image_item);

        }

    }
//    public void addImage(Bitmap bitmap){
//        items.add(new NoticeImageItem(bitmap));
//        notifyItemInserted(items.size()-1);
//    }
}