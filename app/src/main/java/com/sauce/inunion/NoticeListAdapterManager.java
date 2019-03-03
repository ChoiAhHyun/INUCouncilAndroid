package com.sauce.inunion;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import retrofit2.Retrofit;

/*
public class NoticeListAdapterManager extends BaseQuickAdapter<NoticeListItem,BaseViewHolder> {
    Context mContext;
    List<NoticeListItem> listItem;
    List<NoticeListItem> displayListItem;
    public NoticeListAdapterManager(Context context, List<NoticeListItem> itemList) {
        super(R.layout.notice_cardview,itemList);
        this.mContext = context;

    }
    @Override
    protected void convert(BaseViewHolder helper, NoticeListItem item) {

        //helper.setText(R.id.id,item.id);
        helper.setText(R.id.notice_title,item.getTitle());
        helper.setText(R.id.notice_content,item.getContent());
        helper.setText(R.id.notice_time,item.getTime());
        //helper.setText(R.id.notice_image,item.getImage());
    }

}//

public class NoticeListAdapterManager extends RecyclerView.Adapter<NoticeListAdapterManager.ViewHolder> {
    private final Context mCtx;
    private List<NoticeListItem> items= null;
    private ArrayList<NoticeListItem> arrayList;


    public NoticeListAdapterManager(Context context, List<NoticeListItem> items) {
        this.mCtx=context;
        this.items=items;
        arrayList = new ArrayList<NoticeListItem>();
        arrayList.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_cardview,null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NoticeListItem item=items.get(position);

        holder.tv_content.setText(item.getContent());
        holder.tv_title.setText(item.getTitle());
        holder.tv_time.setText(item.getTime());
        //holder.noticeCardviewImageAdapter.setData(item.getImage());
        //holder.noticeCardviewImageAdapter.setRowIndex(position);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arrayList);
        } else {
            for (NoticeListItem item : arrayList) {
                String title = item.getTitle();
                String content = item.getContent();
                if (title.toLowerCase().contains(charText)) {
                    items.add(item);
                }
                if (content.toLowerCase().contains(charText)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_time;
        TextView tv_content;
        CardView cardView;
        private NoticeCardviewImageAdapter noticeCardviewImageAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            tv_title= (TextView) itemView.findViewById(R.id.notice_title);
            tv_time = (TextView) itemView.findViewById(R.id.notice_time);
            tv_content= (TextView) itemView.findViewById(R.id.notice_content);
            cardView=(CardView) itemView.findViewById(R.id.notice_cardview);
            //recyclerView= (RecyclerView) itemView.findViewById(R.id.notice_cardview_image_rv);
            //recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            //noticeCardviewImageAdapter = new NoticeCardviewImageAdapter();
            //recyclerView.setAdapter(noticeCardviewImageAdapter);

        }


    }
}*/

public class NoticeListAdapterManager extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NoticeListItem> items = new ArrayList<>();
    private ArrayList<NoticeListItem> arrayList = new ArrayList<>();
    ArrayList noticeImageItem;
    private FragmentManager fragmentManager;
    private SparseIntArray listPosition = new SparseIntArray();
    private  Context mCtx;
    NoticeInterface service;
    Retrofit retrofit;
    public NoticeListAdapterManager(Context context, FragmentManager fragmentManager) {
        this.mCtx=context;
        this.fragmentManager=fragmentManager;
    }

    public class CellViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_time;
        TextView tv_content;
        CardView cardView;
        LinearLayout linearLayout;
        private RecyclerView mRecyclerView;
        TextView tv_image;
        NoticeCardviewImageAdapter adapter;

        private LinearLayoutManager layoutManager;

        public CellViewHolder(View itemView) {

            super(itemView);
            Context context = itemView.getContext();
            tv_title= (TextView) itemView.findViewById(R.id.notice_title);
            tv_time = (TextView) itemView.findViewById(R.id.notice_time);
            tv_content= (TextView) itemView.findViewById(R.id.notice_content);
            cardView=(CardView) itemView.findViewById(R.id.notice_cardview);
            mRecyclerView = itemView.findViewById(R.id.notice_cardview_image_rv);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.noticeCardViewLayout);

//            mRecyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(mCtx);

            tv_image=(TextView) itemView.findViewById(R.id.notice_cardview_image_item_tv);
//            if(Notice.subList1.size()>4){
//
//                int i=4;
//                do{
//                    Notice.subList1.remove(i);
//                }while (i<Notice.subList1.size());
//            }

            adapter = new NoticeCardviewImageAdapter(mCtx);

            mRecyclerView.setAdapter(adapter);

            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            mRecyclerView.setLayoutManager(layoutManager);

//            adapter.notifyDataSetChanged();

            // this is needed if you are working with CollapsingToolbarLayout, I am adding this here just in case I forget.
            //mRecyclerView.setNestedScrollingEnabled(false);

        }

        /*public void setData(List<NoticeCardviewImageItem> list) {

            adapter.updateList(list);

        }*/

    }

    public void addItem(NoticeListItem item){
        items.add(item);
        arrayList.add(item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        mCtx = viewGroup.getContext();

        switch (viewType) {

            default: {

                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notice_cardview, viewGroup, false);

                return new CellViewHolder(v1);

            }

        }

    }
/*
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_cardview,null);
        return new CellViewHolder(v);
    }
*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

//        switch (viewHolder.getItemViewType()) {
//
//            default: {
                final NoticeListItem item=items.get(position);
                final CellViewHolder cellViewHolder = (CellViewHolder) viewHolder;

                //cellViewHolder.setData(items.get(position));
                cellViewHolder.tv_content.setText(item.getContent());
                cellViewHolder.tv_title.setText(item.getTitle());
                cellViewHolder.tv_time.setText(item.getTime());
                //cellViewHolder.mRecyclerView.set;
                cellViewHolder.tv_image.setText(item.getCount());
                cellViewHolder.adapter.clearItem();
                for (int i = 0; i < item.getNoticeImageList().size(); i++){
                    cellViewHolder.adapter.addItem(new NoticeImageItem(item.getNoticeImageList().get(i)));
                    Log.d("파일",position + ", " + i + ", " + item.getNoticeImageList().get(i));
                }

//                NoticeListItem currentUser = ((NoticeListItem) items.get(position));
//
//                ViewHolder..setText(currentUser.getTitle());

                cellViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",item.getId());

                        NoticeDetailManager noticeDetail= new NoticeDetailManager();
                        noticeDetail.setArguments(bundle);

                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, noticeDetail)
                                .commit();
                    }
                });


//                int lastSeenFirstPosition = listPosition.get(position, 0);
//
//                if (lastSeenFirstPosition >= 0) {
//
//                    cellViewHolder.layoutManager.scrollToPositionWithOffset(lastSeenFirstPosition, 0);
//
//                }
//
//                break;
//
//
//            }
//
//        }

    }


//    @Override
//    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
//
//        final int position = viewHolder.getAdapterPosition();
//
//        CellViewHolder cellViewHolder = (CellViewHolder) viewHolder;
//
//        int firstVisiblePosition = cellViewHolder.layoutManager.findFirstVisibleItemPosition();
//
//        listPosition.put(position, firstVisiblePosition);
//
//        super.onViewRecycled(viewHolder);
//
//    }

    @Override
    public int getItemCount() {

        if (items == null)

            return 0;

        return items.size();

    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arrayList);
        } else {
            for (NoticeListItem item : arrayList) {
                String title = item.getTitle();
                String content = item.getContent();
                if (title.toLowerCase().contains(charText)) {
                    items.add(item);
                }
                if (content.toLowerCase().contains(charText)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}