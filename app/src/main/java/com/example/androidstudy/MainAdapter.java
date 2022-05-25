package com.example.androidstudy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.androidstudy.activitys.MainActivity;
import com.example.androidstudy.activitys.WebViewExam;
import com.example.androidstudy.activitys.server_system.DeleteDateRequest;
import com.example.androidstudy.activitys.server_system.UpdateitemDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private ArrayList<AppInfo> appInfos;
    private Context mContext;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();    // 다중 선택시 선택한 position에 대한 item 정보를 보관하는 객체
    
    private final StartDragListener mStartDragListener;

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public View rowView;
        protected ImageView iv_profile, iv_drag_drop;
        protected TextView tv_lecturer, tv_content, tv_content_id;
        protected LinearLayout item_expand;
        protected Button btn_expand_url, btn_expand_start;
        protected ImageButton btn_setting;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.iv_drag_drop = (ImageView) itemView.findViewById(R.id.iv_drag_drop);
            this.tv_lecturer = (TextView) itemView.findViewById(R.id.tv_lecturer);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            this.tv_content_id = (TextView) itemView.findViewById(R.id.tv_content_id);
            this.item_expand = (LinearLayout) itemView.findViewById(R.id.item_expend);
            this.btn_expand_url = (Button) itemView.findViewById(R.id.btn_expand_url);
            this.btn_expand_start = (Button) itemView.findViewById(R.id.btn_expand_start);
            this.btn_setting = (ImageButton) itemView.findViewById(R.id.btn_setting);
        }
    }

    public MainAdapter(Context context, ArrayList<AppInfo> arrayList, StartDragListener startDragListener) {
        appInfos = arrayList;
        this.mContext = context;
        mStartDragListener = startDragListener;
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
        holder.iv_profile.setImageResource(Integer.parseInt(appInfos.get(position).getProfile()));
        holder.tv_lecturer.setText(appInfos.get(position).getLecturer());
        holder.tv_content.setText(appInfos.get(position).getContent());
        holder.tv_content_id.setText(String.valueOf(appInfos.get(position).getId()));

        // specific area for drag and drop
        holder.iv_drag_drop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }
        });

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 선택된 content의 name, url 등의 정보를 appInfos에서 쉽게 얻기 위해 idx값 활용
                int idx = findAppinfo(holder.tv_content.getText().toString());

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
                            String path = null;
                            try {
                                path = "com.example.androidstudy.activitys." + holder.tv_content.getText();
                                /*if (Class.forName(path) == null)
                                    path = "com.example.androidstudy.activitys.server_system." + holder.tv_content.getText();*/
                                intent = new Intent(mContext, Class.forName(path));
                            } catch (Exception e) {
                                e.printStackTrace();
                                path = "com.example.androidstudy.activitys.server_system." + holder.tv_content.getText();
                                try {
                                    intent = new Intent(mContext, Class.forName(path));
                                } catch (ClassNotFoundException classNotFoundException) {
                                    classNotFoundException.printStackTrace();
                                }
                            }
                            mContext.startActivity(intent);
                        }
                    });

                    // btn_expand_url event
                    int finalIdx = idx;
                    holder.btn_expand_url.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = null;
                            if(appInfos.get(finalIdx).getUrl().equals("")) {
                                Toast.makeText(mContext, "You need to set url", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                intent = new Intent(mContext, WebViewExam.class);
                                intent.putExtra("url", appInfos.get(finalIdx).getUrl());
                                mContext.startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        // xmlParser를 사용해서 개발했기 때문에 DB연동으로 appinfos를 사용하는 방식과 다름
        // ex) arraylist에서 특정 item을 찾을 때 appinfo의 id를 활용(xmlParser)
        //                                    while문을 사용해서 content를 비교(DB연동)
        holder.btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectOne = holder.getAbsoluteAdapterPosition();
                UpdateitemDialog uidf = new UpdateitemDialog(mContext, appInfos.get(selectOne));
                // adapter 내에서 getSupportFragmentManger() 호출이 불가해서 casting 시도
                uidf.show(((MainActivity)mContext).getSupportFragmentManager(), "PopUpdateFormDialogFragment");
                //ㅅsetUrlDialog(selectOne);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String delete = holder.tv_content.getText().toString();
                //delItem(delete);
                return true;
            }
        });
    }

    public void setUrlDialog(int idx) {
        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setIcon(R.mipmap.ic_launcher_round);
        ad.setTitle(appInfos.get(idx).getContent());
        ad.setMessage("url를 설정해주세요.");
        AppInfoXmlParser resParser = new AppInfoXmlParser();

        // default setting url func
        final EditText et = new EditText(mContext);
        String url = appInfos.get(idx).getUrl();
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
                // 변경사항을 XML file에 반영
                try {
                    appInfos = resParser.editXmlFile(mContext, idx, result);
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

    /*
    * onRowMoved defined in the Contract interface earlier gets called when the drag and drop is done.
    * Here we swap the positions of the two rows present in the ArrayList and call notifyItemMoved to refresh the adapter.
    * */
    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(appInfos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(appInfos, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(CustomViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(CustomViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);
    }

    public int findAppinfo(String content) {
        int idx = 0;
        while(appInfos.get(idx).getContent() != content) {
            idx++;
        }
        return idx;
    }

    // item delete method
    public void delItem(String delete) {
        int idx = findAppinfo(delete);

        Response.Listener<String> delExamListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean delSuccess = jsonObject.getBoolean("success");

                    if(delSuccess) {
                        appInfos.remove(idx);
                        notifyItemRemoved(idx);
                        notifyItemRangeChanged(idx, appInfos.size());
                        Toast.makeText(mContext, "삭제 완료", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DeleteDateRequest delRequest = new DeleteDateRequest(MainActivity.getUserID(), appInfos.get(idx).getContent(), delExamListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(delRequest);

    }
}
