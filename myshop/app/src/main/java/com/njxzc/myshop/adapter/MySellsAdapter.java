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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.EaseConstant;
import com.njxzc.myshop.BuyActivity;
import com.njxzc.myshop.ChatActivity;
import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.personal_activity.MyOrdersActivity;
import com.njxzc.myshop.personal_activity.UpdateComActivity;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class MySellsAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;


    public MySellsAdapter(Context context, List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, final View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.sells_style,null);
        TextView tv_orderId = view.findViewById(R.id.tv_orderId);
        final TextView tv_status = view.findViewById(R.id.tv_status);
        ImageView iv_comImageMain = view.findViewById(R.id.iv_comImageMain);
        iv_comImageMain.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView tv_comName = view.findViewById(R.id.tv_comName);
        TextView tv_currentPrice = view.findViewById(R.id.tv_currentPrice);
        TextView tv_totalPrice = view.findViewById(R.id.tv_totalPrice);
        TextView tv_chat = view.findViewById(R.id.tv_chat);
        TextView tv_getTime = view.findViewById(R.id.tv_getTime);
        Button btn_decline = view.findViewById(R.id.btn_decline);
        final Button btn_delivery = view.findViewById(R.id.btn_delivery);
        Map<String,Object> hm = data.get(position);
        final int orderId = (int) hm.get("orderId");
        int status = (int) hm.get("status");
        int buyCount = (int) hm.get("buyCount");
        String comImageMain = (String) hm.get("comImageMain");
        String comName = (String) hm.get("comName");
        String getTime = (String) hm.get("getTime");
        Double currentPrice = (Double) hm.get("currentPrice");
        Double totalPrice = (Double) hm.get("total");
        final String publisherName = (String) hm.get("nickName");
        final String userName = (String) hm.get("userName");
        tv_comName.setText(comName);
        tv_orderId.setText("订单号："+String.valueOf(orderId));
        tv_totalPrice.setText("共"+String.valueOf(buyCount)+"件商品，应付款："+String.valueOf(totalPrice)+"元");
        tv_currentPrice.setText("￥"+String.valueOf(currentPrice));
        if (status==0){
            tv_status.setText("待买家付款");
            btn_decline.setVisibility(View.VISIBLE);
        }else if(status==1){
            tv_status.setText("买家已付款，请尽快发货");
            btn_delivery.setVisibility(View.VISIBLE);
        }else if(status==2){
            tv_status.setText("卖家已发货，等待买家收货");
        }else if(status==3){
            tv_status.setText("买家确认收货，交易完成");
        }else if(status==4){
            tv_status.setText("卖家关闭了订单");
        }else if(status==5){
            tv_status.setText("买家关闭了订单");
        }
        Glide.with(context)
                .load(comImageMain).asBitmap()
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_comImageMain);
        if (status==3){
            tv_getTime.setText("交易完成时间："+getTime);
            tv_getTime.setVisibility(View.VISIBLE);
        }


        tv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(context,ChatActivity.class);
                chat.putExtra("userName",userName);
                chat.putExtra("publisherName",publisherName);
                chat.putExtra(EaseConstant.EXTRA_USER_ID,userName);
                chat.putExtra(EaseConstant.EXTRA_USER_NICKNAME,publisherName);
                context.startActivity(chat);
            }
        });
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("关闭订单");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("您确定关闭订单号："+orderId+"的订单吗？");
                //取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                //确定按钮
                builder.setPositiveButton("确认关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        final int status = 4;
                        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidOrder/sellerManageOrder";
                                    OkhttpHelper.sellerManageOrder(url,String.valueOf(orderId),String.valueOf(status),new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            final String responseData = response.body().string();
                                            System.out.println(responseData);
                                            if (responseData.equals("success")) {
                                                Looper.prepare();
                                                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                                intent.putExtra("flag", "refreshOrders");
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                                context.sendBroadcast(intent);
                                                Toast.makeText(context, "订单已关闭", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }else {
                                                Looper.prepare();
                                                Toast.makeText(context, "订单关闭失败", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }



                                        }

                                    });


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
                //显示对话框
                builder.create().show();


            }
        });

        btn_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("订单发货");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("您确定发货订单号："+orderId+"的订单吗？");
                //取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                //确定按钮
                builder.setPositiveButton("确认发货", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        final int status = 2;
                        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidOrder/sellerManageOrder";
                                    OkhttpHelper.sellerManageOrder(url,String.valueOf(orderId),String.valueOf(status),new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            final String responseData = response.body().string();
                                            System.out.println(responseData);
                                            if (responseData.equals("success")) {
                                                Looper.prepare();
                                                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                                intent.putExtra("flag", "refreshOrders");
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                                context.sendBroadcast(intent);
                                                Toast.makeText(context, "订单已发货", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }else {
                                                Looper.prepare();
                                                Toast.makeText(context, "订单发货失败", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }


                                        }

                                    });


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
