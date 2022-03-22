package com.example.androidstudy;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private ArrayList<MainData> appList;
    private OnItemClick mCallback;              // 클릭된 아이템의 이름을 mainActivity로 반환하기 위한 listener
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();    // 다중 선택시 선택한 position에 대한 item 정보를 보관하는 객체

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

                // get()는 position값이 존재하면 true를 반환함
                if (selectedItems.get(holder.getAdapterPosition())) { // VISIBLE -> INVISIBLE
                    selectedItems.delete(holder.getAdapterPosition());
                    holder.item_expand.setVisibility(View.GONE);
                } else {
                    // INVISIBLE -> VISIBLE
                    selectedItems.put(holder.getAdapterPosition(), true);
                    holder.item_expand.setVisibility(View.VISIBLE);

                    // btn_expand_start event
                    holder.btn_expand_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String appName = holder.tv_content.getText().toString();

                            // MainActivity의 OnClick 호출
                            try {
                                mCallback.onClick(appName);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String appName = holder.tv_content.getText().toString();

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
        protected LinearLayout item_expand;
        protected Button btn_expand_url, btn_expand_start;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.tv_lecturer = (TextView) itemView.findViewById(R.id.tv_lecturer);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            this.item_expand = (LinearLayout) itemView.findViewById(R.id.item_expend);
            this.btn_expand_url = (Button) itemView.findViewById(R.id.btn_expand_url);
            this.btn_expand_start = (Button) itemView.findViewById(R.id.btn_expand_start);
        }
    }
}
