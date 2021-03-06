package com.example.checkpoint3for5236;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MeetingContextAdapter extends RecyclerView.Adapter<MeetingContextAdapter.MeetingViewHolder> {

    private ArrayList<MeetingContext> mContextList;
    private MeetingContextAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MeetingContextAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        //public Button button;

        public MeetingViewHolder(View itemView, final MeetingContextAdapter.OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public MeetingContextAdapter(ArrayList<MeetingContext> exampleList) {
        mContextList = exampleList;
    }

    @Override
    public MeetingContextAdapter.MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        MeetingContextAdapter.MeetingViewHolder evh = new MeetingContextAdapter.MeetingViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(MeetingContextAdapter.MeetingViewHolder holder, int position) {
        MeetingContext currentItem = mContextList.get(position);

        holder.mTextView1.setText(currentItem.getContext());
    }

    @Override
    public int getItemCount() {
        return mContextList.size();
    }

}

