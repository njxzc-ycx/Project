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
import com.njxzc.myshop.PayActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class MyBuysAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;


    public MyBuysAdapter(Context context, List<Map<String,Object>> data){
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
        View view = layoutInflater.inflate(R.layout.buys_style,null);
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
        Button btn_pay = view.findViewById(R.id.btn_pay);
        final Button btn_getCom = view.findViewById(R.id.btn_getCom);
        Map<String,Object> hm = data.get(position);
        final int orderId = (int) hm.get("orderId");
        int status = (int) hm.get("status");
        final int buyCount = (int) hm.get("buyCount");
        final int flag = (int) hm.get("flag");
        final int count = (int) hm.get("count");
        String comImageMain = (String) hm.get("comImageMain");
        final String comName = (String) hm.get("comName");
        Double currentPrice = (Double) hm.get("currentPrice");
        String getTime = (String) hm.get("getTime");
        final Double totalPrice = (Double) hm.get("total");
        final String publisherName = (String) hm.get("nickName");
        final String userName = (String) hm.get("userName");
        final int comId = (int) hm.get("comId");
        tv_comName.setText(comName);
        tv_orderId.setText("订单号："+String.valueOf(orderId));
        tv_totalPrice.setText("共"+String.valueOf(buyCount)+"件商品，应付款："+String.valueOf(totalPrice)+"元");
        tv_currentPrice.setText("￥"+String.valueOf(currentPrice));
        if (status==0){
            if (flag==2||flag==3){
                tv_status.setText("来晚一步，此商品已被他人买走或被下架");
                btn_decline.setVisibility(View.VISIBLE);
            }else if(buyCount>count){
                tv_status.setText("来晚一步，此商品数量不足，请重新下单");
                btn_decline.setVisibility(View.VISIBLE);
            }else {
                tv_status.setText("等待买家付款");
                btn_decline.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(View.VISIBLE);
            }
        }else if(status==1){
            tv_status.setText("您已付款，请等待卖家发货");
        }else if(status==2){
            tv_status.setText("卖家已发货，等待买家收货");
            btn_getCom.setVisibility(View.VISIBLE);
        }else if(status==3){
            tv_status.setText("您确认收货，交易完成");
        }else if(status==4){
            tv_status.setText("卖家关闭了订单");
        }else if(status==5){
            tv_status.setText("买家关闭了订单");
        }

        if (status == 3){
            tv_getTime.setText("收货时间："+getTime);
            tv_getTime.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(comImageMain).asBitmap()
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_comImageMain);


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

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PayActivity.class);
                intent.putExtra("total",totalPrice);
                intent.putExtra("orderId",orderId);
                intent.putExtra("comName",comName);
                intent.putExtra("count",buyCount);
                intent.putExtra("comId",comId);
                context.startActivity(intent);

            }
        });
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("取消订单");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("您确定取消订单号："+orderId+"的订单吗？");
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
                        final int status = 5;
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
                                                intent.putExtra("flag", "refreshOrder");
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                                context.sendBroadcast(intent);
                                                Toast.makeText(context, "订单已取消", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }else {
                                                Looper.prepare();
                                                Toast.makeText(context, "订单取消失败", Toast.LENGTH_SHORT).show();
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

        btn_getCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("确认收货");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("您已收到订单号："+orderId+"的商品吗？请确认");
                //取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                //确定按钮
                builder.setPositiveButton("确认收货", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        final int status = 3;
                        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidOrder/getOrder";
                                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                    final String getTime = formatter.format(curDate);
                                    OkhttpHelper.getOrder(url,String.valueOf(orderId),String.valueOf(status),String.valueOf(getTime),new Callback() {
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
                                                intent.putExtra("flag", "refreshOrder");
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                                context.sendBroadcast(intent);
                                                Toast.makeText(context, "订单已收货", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }else {
                                                Looper.prepare();
                                                Toast.makeText(context, "订单收货失败", Toast.LENGTH_SHORT).show();
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
