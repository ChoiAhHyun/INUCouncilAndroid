package com.sauce.inunion;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
public class ContactListAdapter extends BaseQuickAdapter<ContactListItem,BaseViewHolder>{
    Context mContext;
    public ContactListAdapter(Context context, List<ContactListItem> itemList) {
        super(R.layout.contact_list_item,itemList);
        this.mContext = context;

    }
    @Override
    protected void convert(BaseViewHolder helper, ContactListItem item) {

        //helper.setText(R.id.id,item.id);
        helper.setText(R.id.name_professor,item.getName());

    }

}
*/
public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mCtx;
    private FragmentManager fragmentManager;
    private List<ContactListItem> items= null;
    private ArrayList<ContactListItem> arrayList;

    public static final int SECTION_VIEW = 0;

    public static final int CONTENT_VIEW = 1;

    WeakReference<Context> mContextWeakReference;

    public void setList(List<ContactListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public ContactListAdapter(Context context, List<ContactListItem> items, FragmentManager fragmentManager) {
        this.mCtx=context;
        this.items=items;
        arrayList = new ArrayList<ContactListItem>();
        arrayList.addAll(items);
        this.fragmentManager=fragmentManager;

        this.mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,null);
//        return new ViewHolder(v);

        Context context = mContextWeakReference.get();

        if (viewType == SECTION_VIEW) {

            return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_section, parent, false));

        }

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false), context);
    }


    @Override

    public int getItemViewType(int position) {

        if (items.get(position).isSection) {

            return SECTION_VIEW;

        } else {

            return CONTENT_VIEW;

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ContactListItem item=items.get(position);

//        holder.tv_name.setText(item.getName());

        Context context = mContextWeakReference.get();

        if (context == null) {

            return;

        }

        if (SECTION_VIEW == getItemViewType(position)) {



            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;

            ContactListItem sectionItem = items.get(position);

            sectionHeaderViewHolder.headerTitleTextview.setText(sectionItem.name_professor);

            return;

        }



        ViewHolder ViewHolder = (ViewHolder) holder;

        ContactListItem currentUser = ((ContactListItem) items.get(position));

        ViewHolder.tv_name.setText(currentUser.name_professor);

        ViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id",item.getId());
                ContactDetail contactDetail= new ContactDetail();
                contactDetail.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, contactDetail)
                        .addToBackStack(null)
                        .commit();
            }
        });
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
            for (ContactListItem item : arrayList) {
                String name = item.getName();
                if (name.toLowerCase().contains(charText)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        LinearLayout linearLayout;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);

            tv_name= (TextView) itemView.findViewById(R.id.contact_name);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.contact_list_item);
        }

    }


    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerTitleTextview;


        public SectionHeaderViewHolder(View itemView) {

            super(itemView);

            headerTitleTextview = (TextView) itemView.findViewById(R.id.contact_section);

        }

    }

//    public boolean onPlaceSubheaderBetweenItems(int position) {
//        final ContactListItem contact = items.get(position);
//        final ContactListItem nextContact = items.get(position + 1);
//
//        //The subheader will be placed between two neighboring items if the first movie characters are different.
//        return !contact.getName().substring(0, 1).equals(nextContact.getName().substring(0, 1));
//    }
}
