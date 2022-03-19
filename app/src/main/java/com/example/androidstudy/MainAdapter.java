package com.example.androidstudy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private ArrayList<MainData> appList;
    private OnItemClick mCallback;
    private Context context;

    public MainAdapter(Context context, ArrayList<MainData> arrayList, OnItemClick listener) {
        appList = arrayList;
        this.context = context;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.CustomViewHolder holder, int position) {
        holder.iv_profile.setImageResource(appList.get(position).getIv_profile());
        holder.tv_lecturer.setText(appList.get(position).getTv_lecturer());
        holder.tv_content.setText(appList.get(position).getTv_content());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curContent = holder.tv_content.getText().toString();
                Toast.makeText(view.getContext(), curContent, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String appName = holder.tv_content.getText().toString();

                try {
                    mCallback.onClick(appName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                //Intent intent = getAppIntent(view);
                //startActivity(intent, )
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != appList ? appList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView iv_profile;
        protected TextView tv_lecturer;
        protected TextView tv_content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.tv_lecturer = (TextView) itemView.findViewById(R.id.tv_lecturer);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
