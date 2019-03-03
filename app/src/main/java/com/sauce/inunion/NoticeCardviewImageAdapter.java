package com.sauce.inunion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * Created by 123 on 2018-08-23.
 */
/*
public class NoticeCardviewImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Integer> mList;

    private NoticeCardviewImageAdapter.OnItemClickListener mItemClickListener;

    public NoticeCardviewImageAdapter() {
    }

    public void updateList(ArrayList<Integer> list) {

        this.mList = list;
        notifyDataSetChanged();
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.notice_cardview_image_item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.notice_cardview_image_recyclerview, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder) rawHolder;
        holder.imageView.setImageResource(mDataList.get(position).getImage());
        holder.imageView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}

public class NoticeCardviewImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Integer> mList;
    private OnItemClickListener mItemClickListener;

    public NoticeCardviewImageAdapter() {

    }

    public void updateList(ArrayList<Integer> list) {

        this.mList = list;
        notifyDataSetChanged();
    }

    private class CellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView imageView;

        public CellViewHolder(View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.notice_cardview_image_item);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemLongClick(view, getLayoutPosition());
                return true;
            }
            return false;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            default: {
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notice_cardview_image_recyclerview, viewGroup, false);
                return new CellViewHolder(v1);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {

            default: {

                CellViewHolder cellViewHolder = (CellViewHolder) viewHolder;
                cellViewHolder.imageView.setImageResource(mList.get(position));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {

        if (mList == null)
            return 0;

        return mList.size();

    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }
    // for both short and long click
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}

public class NoticeCardviewImageAdapter extends BaseQuickAdapter<NoticeCardviewImageItem,BaseViewHolder> {
    Context mContext;

    public NoticeCardviewImageAdapter(Context context, List<NoticeCardviewImageItem> itemList) {
        super(R.layout.notice_cardview_image_recyclerview,itemList);
        this.mContext = context;

    }
    @Override
    protected void convert(BaseViewHolder helper, NoticeCardviewImageItem item) {

        helper.setImageResource(R.id.notice_cardview_image_item,item.getImage());
    }
}*/

public class NoticeCardviewImageAdapter extends RecyclerView.Adapter<NoticeCardviewImageAdapter.ViewHolder> {
    private Context mCtx;

    private ArrayList<NoticeImageItem> items = new ArrayList<>();
    public NoticeCardviewImageAdapter(Context context) {
        this.mCtx=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_cardview_image_recyclerview,null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NoticeImageItem item=items.get(position);
        Log.w("파일",position +", " + item.getImage());
//        Picasso.get().load(item.getImage()).resize(100, 100)
//                .centerCrop()
//                .into(holder.imageView);
        Glide.with(mCtx)
                .load("http://117.16.231.66:7001/imgload/" + item.getImage())
                .thumbnail(0.1f)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(NoticeImageItem item){
         items.add(item);
    }
    public void clearItem(){
        items.clear();
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.notice_cardview_image_item);

        }

    }

}
