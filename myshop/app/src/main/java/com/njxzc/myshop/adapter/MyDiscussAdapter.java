package com.njxzc.myshop.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.GridViewItemsActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.customview.MyGridView;
import com.njxzc.myshop.utils.DealComImageOther;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class MyDiscussAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;

    private ArrayList<String> imagesData;
    private ArrayList<String> itemImageData;
    private DisGvImagesAdapter disGvImagesAdapter;

    public static int count=6;


    public MyDiscussAdapter(Context context, List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (count<data.size()) {
            return count;
        }else {
            return data.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.mydiscuss_style,null);
        Model.getInstance().init(context);
        //根据view对象查找样式文件中需要显示的组件
        LinearLayout ll_userInfo = (LinearLayout)view.findViewById(R.id.ll_userInfo);
        ImageView iv_photo = (ImageView)view.findViewById(R.id.iv_photo);
        TextView tv_nickName = (TextView)view.findViewById(R.id.tv_nickName);
        TextView tv_deleteDis = (TextView)view.findViewById(R.id.tv_deleteDis);
        ImageView iv_sex = (ImageView)view.findViewById(R.id.iv_sex);
        final ImageView iv_up = (ImageView)view.findViewById(R.id.iv_up);
        TextView tv_school = (TextView)view.findViewById(R.id.tv_school);
        TextView tv_discussDes = (TextView)view.findViewById(R.id.tv_discussDes);
        TextView tv_discussTime = (TextView)view.findViewById(R.id.tv_discussTime);
        final TextView tv_ups = (TextView)view.findViewById(R.id.tv_ups);
        TextView tv_comments = (TextView)view.findViewById(R.id.tv_comments);
        TextView tv_hits = (TextView)view.findViewById(R.id.tv_hits);
        MyGridView gv_discussImages = (MyGridView) view.findViewById(R.id.gv_discussImages);
        //解决listview中嵌套gridview无法点击问题
        gv_discussImages.setClickable(true);
        gv_discussImages.setPressed(true);
        gv_discussImages.setEnabled(true);


        //取出每一行需要显示的数据
        Map<String,Object> hm = data.get(position);
        final int discussId = (int) hm.get("discussId");
        String profilePhoto = (String) hm.get("profilePhoto");
        String nickName = (String) hm.get("nickName");
        int sex = (int) hm.get("sex");
        final int userId = (int) hm.get("userId");
        String school = (String) hm.get("school");
        String discussDes = (String) hm.get("discussDes");
        String discussTime = (String) hm.get("discussTime");
        final int ups = (int) hm.get("discussUp");
        int comments = (int) hm.get("discussComments");
        int hits = (int) hm.get("discussHits");
        int isAnonymity = (int) hm.get("isAnonymity");

        if (isAnonymity==0) {
            tv_nickName.setText(nickName);
            //显示图片
            //glide 为4.6.1版本
            Glide.with(context)
                    .load(profilePhoto).asBitmap()
                    .error(R.drawable.errorpic)
                    .placeholder(R.drawable.defaultpic)
                    .into(iv_photo);
        }else {
            tv_nickName.setText("匿名用户");
            if (sex==1){
                iv_photo.setImageResource(R.drawable.anonymity_man);
            }else {
                iv_photo.setImageResource(R.drawable.anonymity_woman);
            }

        }
        tv_school.setText(school);
        tv_discussDes.setText(discussDes);
        tv_discussTime.setText(discussTime);
        tv_ups.setText(String.valueOf(ups));
        tv_comments.setText(String.valueOf(comments));
        tv_hits.setText(String.valueOf(hits));
        if (sex==1){
            iv_sex.setImageResource(R.drawable.sex_man);
        }else {
            iv_sex.setImageResource(R.drawable.sex_woman);
        }
        imagesData = new ArrayList<>();
        final String discussImages = (String) hm.get("discussImages");

        if (discussImages.equals(null)||discussImages.equals("")){
            gv_discussImages.setVisibility(View.GONE);
        }else {
            imagesData = DealComImageOther.dealComImageOther(discussImages);
            disGvImagesAdapter = new DisGvImagesAdapter(context, imagesData);
            gv_discussImages.setAdapter(disGvImagesAdapter);

            gv_discussImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    itemImageData = new ArrayList<String>();
                    itemImageData = DealComImageOther.dealComImageOther(discussImages);
                    Intent imageItemIntent = new Intent(context, GridViewItemsActivity.class);
                    imageItemIntent.putExtra("list", itemImageData);
                    imageItemIntent.putExtra("position", position);
                    context.startActivity(imageItemIntent);
                }
            });
        }



        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int newUps = ups+1;
                iv_up.setImageResource(R.drawable.up_press);
                tv_ups.setText(String.valueOf(newUps));
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url =  OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/addDisscussUp";
                            OkhttpHelper.addDiscussUp(url,String.valueOf(discussId),new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    iv_up.setClickable(false);


                                }

                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        tv_deleteDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定要删除这篇帖子吗?");
                builder.setTitle("提示");

                //取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                //确定按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                            String url = OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/deleteDisByDiscussId";
                                        OkhttpHelper.deleteDis(url,String.valueOf(discussId),new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Looper.prepare();
                                                Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                final String responseData = response.body().string();
                                                System.out.println(responseData);
                                                if (responseData.equals("deleteSuccess")){
                                                    Looper.prepare();
                                                    Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                                    intent.putExtra("flag","refresh");
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                                    context.sendBroadcast(intent);
                                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                }else if(responseData.equals("deleteFail")){
                                                    Looper.prepare();
                                                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                }

                                            }
                                        });

                                    }
                                });
                            }
                        });
                    }
                });
                //显示对话框
                builder.create().show();

            }
        });

        return view;
    }
}
