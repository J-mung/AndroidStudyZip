package com.example.androidstudy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private ArrayList<AppInfo> appInfos;
    private Context mContext;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();    // 다중 선택시 선택한 position에 대한 item 정보를 보관하는 객체
    public MainAdapter(Context context, ArrayList<AppInfo> arrayList) {
        appInfos = arrayList;
        this.mContext = context;
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
        holder.iv_profile.setImageResource(appInfos.get(position).getProfile());
        holder.tv_lecturer.setText(appInfos.get(position).getLecturer());
        holder.tv_content.setText(appInfos.get(position).getContent());
        holder.tv_content_id.setText(String.valueOf(appInfos.get(position).getId()));

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 선택된 content의 name, url 등의 정보를 appInfos에서 쉽게 얻기 위해 id값 활용
                int id = Integer.parseInt(holder.tv_content_id.getText().toString())-1;

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
                            Intent intent = null;
                            try {
                                intent = new Intent(mContext, Class.forName("com.example.androidstudy." + appInfos.get(id).getContent()));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            mContext.startActivity(intent);
                        }
                    });

                    // btn_expand_url event
                    holder.btn_expand_url.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = null;
                            if(appInfos.get(id).getUrl().equals("")) {
                                Toast.makeText(mContext, "You need to set url", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                intent = new Intent(mContext, WebViewExam.class);
                                intent.putExtra("url", appInfos.get(id).getUrl());
                                mContext.startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        holder.btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUrlDialog(Integer.parseInt(holder.tv_content_id.getText().toString())-1);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String appName = holder.tv_content.getText().toString();

                return true;
            }
        });
    }

    public void setUrlDialog(int appId) {
        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setIcon(R.mipmap.ic_launcher_round);
        ad.setTitle(appInfos.get(appId).getContent());
        ad.setMessage("url를 설정해주세요.");
        AppInfoXmlParser resParser = new AppInfoXmlParser();

        // test setting url func
        final EditText et = new EditText(mContext);
        String url = appInfos.get(appId).getUrl();
        if(url.equals(""))
            et.setHint("https://www.google.com");
        else
            et.setText(url);
        ad.setView(et);

        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String result = et.getText().toString();
                Log.e("editXml", "xmlParser() call");
                try {
                    appInfos = resParser.editXmlFile(mContext, appId, result);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialogInterface.dismiss();
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ad.show();
    }

    @Override
    public int getItemCount() {
        return (null != appInfos ? appInfos.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView iv_profile;
        protected TextView tv_lecturer, tv_content, tv_content_id;
        protected LinearLayout item_expand;
        protected Button btn_expand_url, btn_expand_start;
        protected ImageButton btn_setting;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.tv_lecturer = (TextView) itemView.findViewById(R.id.tv_lecturer);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            this.tv_content_id = (TextView) itemView.findViewById(R.id.tv_content_id);
            this.item_expand = (LinearLayout) itemView.findViewById(R.id.item_expend);
            this.btn_expand_url = (Button) itemView.findViewById(R.id.btn_expand_url);
            this.btn_expand_start = (Button) itemView.findViewById(R.id.btn_expand_start);
            this.btn_setting = (ImageButton) itemView.findViewById(R.id.btn_setting);
        }
    }
}
