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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.R;
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
import okhttp3.Response;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class MyComsAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;

    private ArrayList<Map<String,Object>> comData;

    public MyComsAdapter(Context context, List<Map<String,Object>> data){
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
        View view = layoutInflater.inflate(R.layout.my_coms_style,null);
        //根据view对象查找样式文件中需要显示的组件
        ImageView comimagemain = (ImageView)view.findViewById(R.id.iv_comImageMain);
        ImageView iv_updateCom = (ImageView)view.findViewById(R.id.iv_updateCom);
        ImageView iv_withdrawCom = (ImageView)view.findViewById(R.id.iv_withdrawCom);
        comimagemain.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView comname = (TextView)view.findViewById(R.id.tv_comName);
        TextView ontime = (TextView)view.findViewById(R.id.tv_onTime);
        TextView currentprice = (TextView)view.findViewById(R.id.tv_currentPrice);
        TextView collect = (TextView)view.findViewById(R.id.tv_collects);
        TextView tv_status = (TextView)view.findViewById(R.id.tv_status);
        //取出每一行需要显示的数据
        final Map<String,Object> hm = data.get(position);
        final String comName = (String)hm.get("comName");
        final String des = (String)hm.get("des");
        String onTime = (String)hm.get("onTime");
        String inTime = (String)hm.get("inTime");
        final Double currentPrice = (Double) hm.get("currentPrice");
        Integer collects = (Integer) hm.get("collects");
        final Integer comId = (Integer) hm.get("comId");
        final Integer count = (Integer) hm.get("count");
        final Integer flag = (Integer) hm.get("flag");
        final String comImageMain = (String)hm.get("comImageMain");
        //把显示的值赋给组件
        comname.setText(comName);
        ontime.setText("发布时间："+inTime);
        currentprice.setText("￥"+String.valueOf(currentPrice));
        collect.setText(String.valueOf(collects));
        if (flag==0){
            tv_status.setText("审核中");
        }else if (flag==1){
            tv_status.setText("已上架");
            iv_updateCom.setVisibility(View.VISIBLE);
            iv_withdrawCom.setVisibility(View.VISIBLE);
        }else if(flag==2){
            tv_status.setText("已下架");
            iv_updateCom.setVisibility(View.GONE);
            iv_withdrawCom.setVisibility(View.GONE);
        }else if(flag==3){
            tv_status.setText("已售罄");
            iv_updateCom.setVisibility(View.GONE);
            iv_withdrawCom.setVisibility(View.GONE);
        }
        //显示图片
        //glide 为4.6.1版本
        Glide.with(context)
                .load(comImageMain).asBitmap()
                /*.apply(new RequestOptions().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(comimagemain);

        iv_updateCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comData = new ArrayList<Map<String, Object>>();
                Map map = new HashMap();
                String comImageOthers = (String) hm.get("comImageOthers");
                Double primePrice = (Double) hm.get("primePrice");
                String thirdName = (String) hm.get("thirdName");
                int isBargain = (int) hm.get("isBargain");
                int thirdId = (int) hm.get("thirdId");
                int count = (int) hm.get("count");
                int comId = (int) hm.get("comId");
                map.put("comName",comName);
                map.put("des",des);
                map.put("comImageOthers",comImageOthers);
                map.put("comImageMain",comImageMain);
                map.put("currentPrice",currentPrice);
                map.put("primePrice",primePrice);
                map.put("isBargain",isBargain);
                map.put("thirdId",thirdId);
                map.put("comId",comId);
                map.put("thirdName",thirdName);
                map.put("count",count);
                comData.add(map);
                System.out.println(comData);
                UpdateComActivity.counts=6;
                Intent intent = new Intent(context,UpdateComActivity.class);
                intent.putExtra("datas",(Serializable) comData);
                context.startActivity(intent);

            }
        });

        iv_withdrawCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定要下架此商品吗?");
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
                                        String url = OkhttpHelper.getIp()+"/shopsystem/androidCom/updateComFlagByComId";
                                        OkhttpHelper.withdrawCom(url,String.valueOf(comId),new okhttp3.Callback() {
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
                                                if (responseData.equals("updateSuccess")){
                                                    Looper.prepare();
                                                    Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                                    intent.putExtra("flag","refresh");
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                                    context.sendBroadcast(intent);
                                                    Toast.makeText(context, "下架成功", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                }else if(responseData.equals("updateFail")){
                                                    Looper.prepare();
                                                    Toast.makeText(context, "下架失败", Toast.LENGTH_SHORT).show();
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
