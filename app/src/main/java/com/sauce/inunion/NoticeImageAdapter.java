package com.sauce.inunion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NoticeImageAdapter extends RecyclerView.Adapter<NoticeImageAdapter.ViewHolder> {
    private Context mCtx;

    public ArrayList<NoticeImageItem> items = new ArrayList<>();
    public ArrayList<NoticeImageItem> arrayList = new ArrayList<>();
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

        Glide.with(mCtx)
                .load(item.getImage())
                .into(holder.imageView);
//        Picasso.get().load(item.getImage()).into(holder.imageView);
//        holder.imageView.setImageBitmap(item.getImage());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void addItem(NoticeImageItem item){
        items.add(item);
        arrayList.add(item);
        notifyDataSetChanged();
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