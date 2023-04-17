package com.example.globalcredenceapp.notice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.globalcredenceapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.RecyclerView;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeVH> {
    List<NoticeModelClass> noticeList;

    public NoticeAdapter(List<NoticeModelClass> noticeList) {
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public NoticeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item_layout,parent,false);
        return new NoticeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeVH holder, int position) {
        NoticeModelClass noticeModelClass = noticeList.get(position);
        holder.title.setText(noticeModelClass.getTitle());
        holder.date.setText(noticeModelClass.getDate());
        holder.body.setText(noticeModelClass.getBody());

        boolean isExpandable = noticeList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class NoticeVH extends RecyclerView.ViewHolder {
        TextView title,date,body;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public NoticeVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.MessageText);
            date = itemView.findViewById(R.id.MessageDate);
            body = itemView.findViewById(R.id.BodyText);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoticeModelClass noticeModelClass = noticeList.get(getAdapterPosition());
                    noticeModelClass.setExpandable(!noticeModelClass.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}


