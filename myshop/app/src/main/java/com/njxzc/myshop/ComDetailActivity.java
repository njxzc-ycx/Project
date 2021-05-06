package com.njxzc.myshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import com.hyphenate.easeui.EaseConstant;
import com.njxzc.myshop.adapter.MsgAdapter;
import com.njxzc.myshop.adapter.MyRecyclerViewAdapter;
import com.njxzc.myshop.customview.NestedExpandaleListView;
import com.njxzc.myshop.utils.DealComImageOther;
import com.njxzc.myshop.utils.GetStandardDate;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ComDetailActivity extends AppCompatActivity {

    private ArrayList<Map<String,Object>> comDatas;
    private static ArrayList<Map<String,Object>> msgDatas;
    private ArrayList<Map<String,Object>> replayDatas;
    private ArrayList<String> comImageOtherDatas;
    private ImageView iv_photo;
    private TextView tv_publisherName;
    private TextView tv_school;
    private TextView coin2;
    private TextView tv_primePrice;
    private TextView tv_currentPrice;
    private TextView tv_isBargain;
    private TextView tv_onTime;
    private TextView tv_des;
    private TextView tv_iWant;
    private TextView tv_comment;
    private TextView tv_isSoldOut;
    private TextView tv_comment_content;
    private TextView tv_comment_content2;
    private TextView tv_comment_content3;
    private TextView hide_down;
    private TextView hide_down2;
    private TextView hide_down3;
    private TextView tv_buyit;
    private TextView tv_hits;
    private ImageView iv_sex;
    private ImageView iv_collect;
    private Button bt_comment_send;
    private Button bt_comment_send2;
    private Button bt_comment_send3;
    private RecyclerView rv_comImages;
    private MyRecyclerViewAdapter adapter;
    private LinearLayout ll_comDetail_bottom;
    private RelativeLayout rl_comment;
    private RelativeLayout rl_comment2;
    private RelativeLayout rl_comment3;
    private MsgAdapter msgAdapter;
    private NestedScrollView nsv_comDetail;
    private NestedExpandaleListView lv_comments;
    private ProgressDialog mProgressDialog;
    private SharedPreferences sp;
    String comId;
    String userName;
    public static String publisherName;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_detail);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initView();
        init();
        initCollect();
        initCollectClick();
        initAddHits();
        initComment();

    }



    private void initView() {
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        tv_publisherName = (TextView) findViewById(R.id.tv_publisherName);
        tv_school = (TextView) findViewById(R.id.tv_school);
        tv_hits = (TextView) findViewById(R.id.tv_hits);
        tv_isSoldOut = (TextView) findViewById(R.id.tv_isSoldOut);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        coin2 = (TextView) findViewById(R.id.coin2);
        coin2.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);//划线
        tv_primePrice = (TextView) findViewById(R.id.tv_primePrice);
        tv_buyit = (TextView) findViewById(R.id.tv_buyit);
        tv_primePrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);//划线
        tv_currentPrice = (TextView) findViewById(R.id.tv_currentPrice);
        tv_isBargain = (TextView) findViewById(R.id.tv_isBargain);
        tv_onTime = (TextView) findViewById(R.id.tv_onTime);
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_comment_content = (TextView) findViewById(R.id.comment_content);
        tv_comment_content2 = (TextView) findViewById(R.id.comment_content2);
        tv_comment_content3 = (TextView) findViewById(R.id.comment_content3);
        tv_iWant = (TextView) findViewById(R.id.tv_iWant);
        hide_down = (TextView) findViewById(R.id.hide_down);
        hide_down2 = (TextView) findViewById(R.id.hide_down2);
        hide_down3 = (TextView) findViewById(R.id.hide_down3);
        bt_comment_send = (Button) findViewById(R.id.bt_comment_send);
        bt_comment_send2 = (Button) findViewById(R.id.bt_comment_send2);
        bt_comment_send3 = (Button) findViewById(R.id.bt_comment_send3);
        ll_comDetail_bottom = (LinearLayout) findViewById(R.id.ll_comDetail_bottom);
        lv_comments = (NestedExpandaleListView) findViewById(R.id.lv_comments);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lv_comments.setNestedScrollingEnabled(false);
        }
        lv_comments.setGroupIndicator(null);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        rl_comment2 = (RelativeLayout) findViewById(R.id.rl_comment2);
        rl_comment3 = (RelativeLayout) findViewById(R.id.rl_comment3);
        rv_comImages = (RecyclerView) findViewById(R.id.rv_comImages);
        rv_comImages.setNestedScrollingEnabled(false);//解决RecyclerView与ScrollView的滑动冲突
        nsv_comDetail = (NestedScrollView) findViewById(R.id.nsv_comDetail);

        rv_comImages.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    private void init() {
        final Intent intent = getIntent();
        comDatas = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("datas");
        final int position = intent.getIntExtra("position", 0);//获取第一个页面传递的position
        comId = comDatas.get(position).get("comId").toString();
        int collects = (Integer) comDatas.get(position).get("collects");
        final int sellerId = (int) comDatas.get(position).get("sellerId");
        System.out.println(sellerId);
        publisherName = comDatas.get(position).get("nickName").toString();
        String des = comDatas.get(position).get("des").toString();
        userName = comDatas.get(position).get("userName").toString();
        System.out.println(userName);
        String school = comDatas.get(position).get("school").toString();
        int flag = (int) comDatas.get(position).get("flag");
        String profilePhoto = comDatas.get(position).get("profilePhoto").toString();
        Double primePrice = (Double) comDatas.get(position).get("primePrice");
        Double currentPrice = (Double) comDatas.get(position).get("currentPrice");
        String comImageOther = comDatas.get(position).get("comImageOthers").toString();
        String comImageMain = comDatas.get(position).get("comImageMain").toString();
        String onTime = comDatas.get(position).get("onTime").toString();
        String inTime = comDatas.get(position).get("inTime").toString();
        try {
            //将字符串类型转为date类型
            Date data =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inTime);
            //将时间戳转为 多少天前、多少小时前、几年前、几个月前
            String transOnTime = GetStandardDate.getTimeFormatText(data);
            tv_onTime.setText(transOnTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comImageOtherDatas = new ArrayList<>();
        //用空格分开comImageOther转为数组
        comImageOtherDatas = DealComImageOther.dealComImageOther(comImageOther);
        comImageOtherDatas.add(0,comImageMain);
        int sex = (Integer) comDatas.get(position).get("sex");
        int isBargain = (Integer) comDatas.get(position).get("isBargain");
        tv_publisherName.setText(publisherName);
        tv_school.setText(school);
        //如果sex为1则为男  如果为0则为女
        if(sex==1){
            iv_sex.setImageResource(R.drawable.sex_man);
        }else {
            iv_sex.setImageResource(R.drawable.sex_woman);
        }
        Glide.with(ComDetailActivity.this)
                .load(profilePhoto).asBitmap()
                /*.apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_photo);
        tv_primePrice.setText(String.valueOf(primePrice));
        tv_currentPrice.setText(String.valueOf(currentPrice));
        //如果isBargain为0则为不讲价 1位可讲价
        if(isBargain==0){
            tv_isBargain.setText("不讲价");
        }else {
            tv_isBargain.setText("可讲价");
        }

        tv_des.setText(des);
        //设置RecyclerView的适配器
        adapter = new MyRecyclerViewAdapter(ComDetailActivity.this,comImageOtherDatas);
        rv_comImages.setAdapter(adapter);
        RecyclerView rv_comImages = (RecyclerView) findViewById(R.id.rv_comImages);
        //LayoutManager
        rv_comImages.setLayoutManager(new LinearLayoutManager(ComDetailActivity.this,LinearLayoutManager.VERTICAL,false));
        final int userId = sp.getInt("userId",0);
        //如果这是自己的商品则不能点击
        if(sellerId==userId){
            tv_iWant.setVisibility(View.GONE);
            tv_buyit.setVisibility(View.GONE);
        }
        if (flag==2||flag==3){
            if (flag==3){
                tv_isSoldOut.setVisibility(View.VISIBLE);
            }else {
                tv_isSoldOut.setText("已下架");
                tv_isSoldOut.setVisibility(View.VISIBLE);
            }
            tv_buyit.setVisibility(View.GONE);
        }





        //我想要点击事件
        tv_iWant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //如果这是自己的商品则不能点击
                if(sellerId==userId){
                    Toast.makeText(ComDetailActivity.this,"这是您自己的宝贝≥﹏≤",Toast.LENGTH_SHORT).show();
                }else{
                    // TODO: 2021/2/16  如果不是自己的商品，点击进入聊天界面，包括聊天砍价或者直接购买
                    Intent chat = new Intent(ComDetailActivity.this,ChatActivity.class);
                    chat.putExtra("userName",userName);
                    chat.putExtra("publisherName",publisherName);
                    chat.putExtra(EaseConstant.EXTRA_USER_ID,userName);
                    chat.putExtra(EaseConstant.EXTRA_USER_NICKNAME,publisherName);
                    startActivity(chat);
                }

            }
        });

        //直接购买点击事件
        tv_buyit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果这是自己的商品则不能点击
                if(sellerId==userId){
                    Toast.makeText(ComDetailActivity.this,"这是您自己的宝贝≥﹏≤",Toast.LENGTH_SHORT).show();
                }else{
                    // TODO: 2021/2/16  如果不是自己的商品，直接购买
                    Intent tobuy = new Intent(ComDetailActivity.this,BuyActivity.class);
                    tobuy.putExtra("position",position);
                    tobuy.putExtra("datas",(Serializable) comDatas);
                    startActivity(tobuy);
                }

            }
        });

        tv_publisherName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComDetailActivity.this,PerHomePageActivity.class);
                intent.putExtra("userId",sellerId);
                startActivity(intent);
            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComDetailActivity.this,PerHomePageActivity.class);
                intent.putExtra("userId",sellerId);
                startActivity(intent);
            }
        });

    }

    /*//调用easeui直接发送想发送的内容，无需通过文本框输入
    //send message
    protected void sendTextMessage(String content) {
        if(EaseAtMessageHelper.get().containsAtUsername(content)){
            sendAtMessage(content);
        }else{
            EMMessage message = EMMessage.createTxtSendMessage(content, userName);
            sendMessage(message);
        }
    }

    private void sendAtMessage(String content){
        EMMessage message = EMMessage.createTxtSendMessage(content, userName);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(userName);
        if(EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)){
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        }else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message){
        if (message == null) {
            return;
        }
        //设置要发送扩展消息用户昵称
        message.setAttribute("nickName",sp.getString("nickName",""));
        //设置要发送扩展消息用户头像
        message.setAttribute("profilePhoto", sp.getString("profilePhoto",""));
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);

    }*/

    //判断是否为收藏
    private void initCollect() {
        final String userId = String.valueOf(sp.getInt("userId",0));
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidCollect/isCollected";
                    OkhttpHelper.isCollected(url,userId,comId,new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseData = response.body().string();
                            System.out.println(responseData);
                            if(responseData.equals("isCollected")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iv_collect.setImageResource(R.drawable.heart_red);
                                        flag=1;
                                    }
                                });
                            }else if(responseData.equals("noCollected")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iv_collect.setImageResource(R.drawable.heart_default);
                                        flag=0;
                                    }
                                });
                            }
                        }

                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void initCollectClick() {
        iv_collect.setOnClickListener(new View.OnClickListener() {
            final String userId = String.valueOf(sp.getInt("userId",0));
            @Override
            public void onClick(View view) {
                if (flag==1){
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidCollect/cancelCollected";
                                OkhttpHelper.cancelCollected(url,userId,comId,new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String responseData = response.body().string();
                                        System.out.println(responseData);
                                        if(responseData.equals("cancelSuccess")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    iv_collect.setImageResource(R.drawable.heart_default);
                                                    flag=0;
                                                    Toast.makeText(ComDetailActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else if(responseData.equals("cancelFail")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ComDetailActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else if (flag==0){
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidCollect/joinCollected";
                                OkhttpHelper.joinCollected(url,userId,comId,new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String responseData = response.body().string();
                                        System.out.println(responseData);
                                        if(responseData.equals("joinSuccess")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    iv_collect.setImageResource(R.drawable.heart_red);
                                                    flag=1;
                                                    Toast.makeText(ComDetailActivity.this," 收藏成功",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else if(responseData.equals("joinFail")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ComDetailActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });

    }

    private void initAddHits() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidCom/addHits";
                    OkhttpHelper.addHits(url,comId,new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String hits = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_hits.setText(String.valueOf(hits));
                                }
                            });
                        }

                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    private void initComment() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url =  OkhttpHelper.getIp()+"/shopsystem/androidMsg/findMsgAndReplayByComId";
                OkhttpHelper.findMsgAndReplayByComId(url,comId,new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final String responseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseData);
                            msgDatas = new ArrayList<>();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject list = jsonArray.getJSONObject(i);
                                JSONObject msg = list.getJSONObject("message");
                                JSONObject user = msg.getJSONObject("user");
                                final int msgId = msg.getInt("msgId");
                                String msgDes = msg.getString("msgDes");
                                String msgTime = msg.getString("msgTime");
                                //将字符串类型转为date类型
                                Date data =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                int sex = user.getInt("sex");
                                int userId = msg.getInt("userId");
                                String nickName = user.getString("nickName");
                                String photo = user.getString("profilePhoto");
                                final Map map = new HashMap();
                                map.put("msgId",msgId);
                                map.put("msgDes",msgDes);
                                map.put("msgTime",transMsgTime);
                                map.put("sex",sex);
                                map.put("nickName",nickName);
                                map.put("photo",photo);
                                map.put("userId",userId);
                                JSONArray replays = list.getJSONArray("replay");
                                System.out.println(replays);
                                replayDatas = new ArrayList<Map<String, Object>>();
                                for(int j=0;j<replays.length();j++){
                                    JSONObject replay = replays.getJSONObject(j);
                                    JSONObject user2 = replay.getJSONObject("user");
                                    int replayId = replay.getInt("replayId");
                                    String replayDes = replay.getString("replayDes");
                                    String replayTime = replay.getString("replayTime");
                                    //将字符串类型转为date类型
                                    Date data2 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                    //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                    String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                    int replayerId = replay.getInt("replayerId");
                                    int replaySex = user2.getInt("sex");
                                    String replayNickName = user2.getString("nickName");
                                    String msgerName = user2.getString("msgerName");
                                    String replayPhoto = user2.getString("profilePhoto");
                                    final Map map2 = new HashMap();
                                    map2.put("replayerId",replayerId);
                                    map2.put("replayId",replayId);
                                    map2.put("replayDes",replayDes);
                                    map2.put("replayTime",transReplayTime);
                                    map2.put("msgerName",msgerName);
                                    map2.put("replaySex",replaySex);
                                    map2.put("replayNickName",replayNickName);
                                    map2.put("replayPhoto",replayPhoto);
                                    replayDatas.add(map2);
                                }
                                map.put("replayDatas",replayDatas);
                                msgDatas.add(map);
                            }
                            System.out.println(msgDatas);



                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    msgAdapter = new MsgAdapter(ComDetailActivity.this,msgDatas);
                                    lv_comments.setAdapter(msgAdapter);
                                    //第一次加载就展开所有的子类
                                    int groupCount = lv_comments.getCount();

                                    for (int i=0; i<groupCount; i++) {
                                        lv_comments.expandGroup(i);

                                    }
                                    clickMsgItem();

                                }
                            });





                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
        });
        tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_comDetail_bottom.setVisibility(View.GONE);
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                rl_comment.setVisibility(View.VISIBLE);
                tv_comment_content.setFocusable(true);
                tv_comment_content.setFocusableInTouchMode(true);
                tv_comment_content.requestFocus();
                tv_comment_content.setHint("请输入留言内容");
            }
        });

        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_comDetail_bottom.setVisibility(View.VISIBLE);
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(tv_comment_content.getWindowToken(), 0);
            }
        });

        bt_comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msgDes = tv_comment_content.getText().toString();
                if(msgDes==""||msgDes.length()==0||msgDes==null){
                    Toast.makeText(ComDetailActivity.this, "留言内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    final int userId = sp.getInt("userId",0);
                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    final String msgTime = formatter.format(curDate);
                            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String url =  OkhttpHelper.getIp()+"/shopsystem/androidMsg/addMessage";
                                        OkhttpHelper.addMessage(url,String.valueOf(userId),comId,msgTime,msgDes,new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                try {
                                                    final String responseData = response.body().string();
                                                    JSONArray jsonArray = new JSONArray(responseData);
                                                    msgDatas = new ArrayList<>();
                                                    for (int i=0;i<jsonArray.length();i++){
                                                        JSONObject list = jsonArray.getJSONObject(i);
                                                        JSONObject msg = list.getJSONObject("message");
                                                        JSONObject user = msg.getJSONObject("user");
                                                        final int msgId = msg.getInt("msgId");
                                                        String msgDes = msg.getString("msgDes");
                                                        String msgTime = msg.getString("msgTime");
                                                        //将字符串类型转为date类型
                                                        Date data =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                                        //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                        String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                                        int sex = user.getInt("sex");
                                                        int userId = msg.getInt("userId");
                                                        String nickName = user.getString("nickName");
                                                        String photo = user.getString("profilePhoto");
                                                        final Map map = new HashMap();
                                                        map.put("msgId",msgId);
                                                        map.put("msgDes",msgDes);
                                                        map.put("msgTime",transMsgTime);
                                                        map.put("sex",sex);
                                                        map.put("nickName",nickName);
                                                        map.put("photo",photo);
                                                        map.put("userId",userId);
                                                        JSONArray replays = list.getJSONArray("replay");
                                                        System.out.println(replays);
                                                        replayDatas = new ArrayList<Map<String, Object>>();
                                                        for(int j=0;j<replays.length();j++){
                                                            JSONObject replay = replays.getJSONObject(j);
                                                            JSONObject user2 = replay.getJSONObject("user");
                                                            int replayId = replay.getInt("replayId");
                                                            String replayDes = replay.getString("replayDes");
                                                            String replayTime = replay.getString("replayTime");
                                                            //将字符串类型转为date类型
                                                            Date data2 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                                            //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                            String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                                            int replayerId = replay.getInt("replayerId");
                                                            int replaySex = user2.getInt("sex");
                                                            String replayNickName = user2.getString("nickName");
                                                            String msgerName = user2.getString("msgerName");
                                                            String replayPhoto = user2.getString("profilePhoto");
                                                            final Map map2 = new HashMap();
                                                            map2.put("replayerId",replayerId);
                                                            map2.put("replayId",replayId);
                                                            map2.put("replayDes",replayDes);
                                                            map2.put("replayTime",transReplayTime);
                                                            map2.put("msgerName",msgerName);
                                                            map2.put("replaySex",replaySex);
                                                            map2.put("replayNickName",replayNickName);
                                                            map2.put("replayPhoto",replayPhoto);
                                                            replayDatas.add(map2);
                                                        }
                                                        map.put("replayDatas",replayDatas);
                                                        msgDatas.add(map);
                                                    }
                                                    System.out.println(msgDatas);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(ComDetailActivity.this,"留言成功",Toast.LENGTH_SHORT).show();
                                                            closeKeyboard();
                                                            tv_comment_content.setText("");
                                                            msgAdapter = new MsgAdapter(ComDetailActivity.this,msgDatas);
                                                            lv_comments.setAdapter(msgAdapter);
                                                            //第一次加载就展开所有的子类
                                                            int groupCount = lv_comments.getCount();

                                                            for (int i=0; i<groupCount; i++) {
                                                                lv_comments.expandGroup(i);

                                                            }
                                                            Handler handler = new Handler();
                                                            handler.post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    nsv_comDetail.fullScroll(nsv_comDetail.FOCUS_DOWN);
                                                                }
                                                            });
                                                        }
                                                    });







                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        });


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                }
            }
        });

    }


    private void clickMsgItem() {

        //点击父项
        lv_comments.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                ll_comDetail_bottom.setVisibility(View.GONE);
                 rl_comment.setVisibility(View.GONE);
                rl_comment3.setVisibility(View.GONE);
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                rl_comment2.setVisibility(View.VISIBLE);
                tv_comment_content2.setFocusable(true);
                tv_comment_content2.setFocusableInTouchMode(true);
                tv_comment_content2.requestFocus();
                String msgerName = (String) msgDatas.get(groupPosition).get("nickName");
                tv_comment_content2.setHint("回复"+msgerName+"：");
                bt_comment_send2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String replayDes = tv_comment_content2.getText().toString();
                        if(replayDes==""||replayDes.length()==0||replayDes==null){
                            Toast.makeText(ComDetailActivity.this, "留言内容不能为空", Toast.LENGTH_SHORT).show();
                        }else {
                            final int msgId = (int) msgDatas.get(groupPosition).get("msgId");
                            final int msgerId = (int) msgDatas.get(groupPosition).get("userId");
                            final int replayerId = sp.getInt("userId", 0);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            final String replayTime = formatter.format(curDate);
                            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidMsg/addReplay";
                                                OkhttpHelper.addReplay(url,String.valueOf(msgId),String.valueOf(msgerId),String.valueOf(replayerId),replayDes,replayTime,comId,new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        try {
                                                            final String responseData = response.body().string();
                                                            JSONArray jsonArray = new JSONArray(responseData);
                                                            msgDatas = new ArrayList<>();
                                                            for (int i=0;i<jsonArray.length();i++){
                                                                JSONObject list = jsonArray.getJSONObject(i);
                                                                JSONObject msg = list.getJSONObject("message");
                                                                JSONObject user = msg.getJSONObject("user");
                                                                final int msgId = msg.getInt("msgId");
                                                                String msgDes = msg.getString("msgDes");
                                                                String msgTime = msg.getString("msgTime");
                                                                //将字符串类型转为date类型
                                                                Date data =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                                                int sex = user.getInt("sex");
                                                                int userId = msg.getInt("userId");
                                                                String nickName = user.getString("nickName");
                                                                String photo = user.getString("profilePhoto");
                                                                final Map map = new HashMap();
                                                                map.put("msgId",msgId);
                                                                map.put("msgDes",msgDes);
                                                                map.put("msgTime",transMsgTime);
                                                                map.put("sex",sex);
                                                                map.put("nickName",nickName);
                                                                map.put("photo",photo);
                                                                map.put("userId",userId);
                                                                JSONArray replays = list.getJSONArray("replay");
                                                                System.out.println(replays);
                                                                replayDatas = new ArrayList<Map<String, Object>>();
                                                                for(int j=0;j<replays.length();j++){
                                                                    JSONObject replay = replays.getJSONObject(j);
                                                                    JSONObject user2 = replay.getJSONObject("user");
                                                                    int replayId = replay.getInt("replayId");
                                                                    String replayDes = replay.getString("replayDes");
                                                                    String replayTime = replay.getString("replayTime");
                                                                    //将字符串类型转为date类型
                                                                    Date data2 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                                                    //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                    String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                                                    int replayerId = replay.getInt("replayerId");
                                                                    int replaySex = user2.getInt("sex");
                                                                    String replayNickName = user2.getString("nickName");
                                                                    String msgerName = user2.getString("msgerName");
                                                                    String replayPhoto = user2.getString("profilePhoto");
                                                                    final Map map2 = new HashMap();
                                                                    map2.put("replayerId",replayerId);
                                                                    map2.put("replayId",replayId);
                                                                    map2.put("replayDes",replayDes);
                                                                    map2.put("replayTime",transReplayTime);
                                                                    map2.put("msgerName",msgerName);
                                                                    map2.put("replaySex",replaySex);
                                                                    map2.put("replayNickName",replayNickName);
                                                                    map2.put("replayPhoto",replayPhoto);
                                                                    replayDatas.add(map2);
                                                                }
                                                                map.put("replayDatas",replayDatas);
                                                                msgDatas.add(map);
                                                            }
                                                            System.out.println(msgDatas);

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(ComDetailActivity.this,"留言成功",Toast.LENGTH_SHORT).show();
                                                                    closeKeyboard();
                                                                    tv_comment_content2.setText("");
                                                                    msgAdapter = new MsgAdapter(ComDetailActivity.this,msgDatas);
                                                                    lv_comments.setAdapter(msgAdapter);
                                                                    //第一次加载就展开所有的子类
                                                                    int groupCount = lv_comments.getCount();

                                                                    for (int i=0; i<groupCount; i++) {
                                                                        lv_comments.expandGroup(i);

                                                                    }
                                                                }
                                                            });





                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
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
                        }
                    }
                });


                return true;
            }
        });

        hide_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_comDetail_bottom.setVisibility(View.VISIBLE);
                rl_comment2.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(tv_comment_content2.getWindowToken(), 0);
                tv_comment_content2.setText("");
            }
        });

        //点击子项
        lv_comments.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,final int i,final int i1, long l) {
                ll_comDetail_bottom.setVisibility(View.GONE);
                rl_comment.setVisibility(View.GONE);
                rl_comment2.setVisibility(View.GONE);
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                rl_comment3.setVisibility(View.VISIBLE);
                tv_comment_content3.setFocusable(true);
                tv_comment_content3.setFocusableInTouchMode(true);
                tv_comment_content3.requestFocus();
                tv_comment_content3.setText("");
                final ArrayList<Map<String, Object>> replayData = (ArrayList<Map<String, Object>>) msgDatas.get(i).get("replayDatas");
                String msgerName = (String) replayData.get(i1).get("replayNickName");
                tv_comment_content3.setHint("回复" + msgerName + "：");
                bt_comment_send3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String replayDes = tv_comment_content3.getText().toString();
                        if(replayDes==""||replayDes.length()==0||replayDes==null){
                            Toast.makeText(ComDetailActivity.this, "留言内容不能为空", Toast.LENGTH_SHORT).show();
                        }else {
                            final int msgerId = (int) replayData.get(i1).get("replayerId");
                            final int msgId = (int) msgDatas.get(i).get("msgId");
                            final int replayerId = sp.getInt("userId", 0);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            final String replayTime = formatter.format(curDate);
                            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidMsg/addReplay";
                                                OkhttpHelper.addReplay(url,String.valueOf(msgId),String.valueOf(msgerId),String.valueOf(replayerId),replayDes,replayTime,comId,new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(ComDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        try {
                                                            final String responseData = response.body().string();
                                                            JSONArray jsonArray = new JSONArray(responseData);
                                                            msgDatas = new ArrayList<>();
                                                            for (int i=0;i<jsonArray.length();i++){
                                                                JSONObject list = jsonArray.getJSONObject(i);
                                                                JSONObject msg = list.getJSONObject("message");
                                                                JSONObject user = msg.getJSONObject("user");
                                                                final int msgId = msg.getInt("msgId");
                                                                String msgDes = msg.getString("msgDes");
                                                                String msgTime = msg.getString("msgTime");
                                                                //将字符串类型转为date类型
                                                                Date data =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                                                int sex = user.getInt("sex");
                                                                int userId = msg.getInt("userId");
                                                                String nickName = user.getString("nickName");
                                                                String photo = user.getString("profilePhoto");
                                                                final Map map = new HashMap();
                                                                map.put("msgId",msgId);
                                                                map.put("msgDes",msgDes);
                                                                map.put("msgTime",transMsgTime);
                                                                map.put("sex",sex);
                                                                map.put("nickName",nickName);
                                                                map.put("photo",photo);
                                                                map.put("userId",userId);
                                                                JSONArray replays = list.getJSONArray("replay");
                                                                System.out.println(replays);
                                                                replayDatas = new ArrayList<Map<String, Object>>();
                                                                for(int j=0;j<replays.length();j++){
                                                                    JSONObject replay = replays.getJSONObject(j);
                                                                    JSONObject user2 = replay.getJSONObject("user");
                                                                    int replayId = replay.getInt("replayId");
                                                                    String replayDes = replay.getString("replayDes");
                                                                    String replayTime = replay.getString("replayTime");
                                                                    //将字符串类型转为date类型
                                                                    Date data2 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                                                    //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                    String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                                                    int replayerId = replay.getInt("replayerId");
                                                                    int replaySex = user2.getInt("sex");
                                                                    String replayNickName = user2.getString("nickName");
                                                                    String msgerName = user2.getString("msgerName");
                                                                    String replayPhoto = user2.getString("profilePhoto");
                                                                    final Map map2 = new HashMap();
                                                                    map2.put("replayerId",replayerId);
                                                                    map2.put("replayId",replayId);
                                                                    map2.put("replayDes",replayDes);
                                                                    map2.put("replayTime",transReplayTime);
                                                                    map2.put("msgerName",msgerName);
                                                                    map2.put("replaySex",replaySex);
                                                                    map2.put("replayNickName",replayNickName);
                                                                    map2.put("replayPhoto",replayPhoto);
                                                                    replayDatas.add(map2);
                                                                }
                                                                map.put("replayDatas",replayDatas);
                                                                msgDatas.add(map);
                                                            }
                                                            System.out.println(msgDatas);

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(ComDetailActivity.this,"留言成功",Toast.LENGTH_SHORT).show();
                                                                    closeKeyboard();
                                                                    tv_comment_content3.setText("");
                                                                    msgAdapter = new MsgAdapter(ComDetailActivity.this,msgDatas);
                                                                    lv_comments.setAdapter(msgAdapter);
                                                                    //第一次加载就展开所有的子类
                                                                    int groupCount = lv_comments.getCount();

                                                                    for (int i=0; i<groupCount; i++) {
                                                                        lv_comments.expandGroup(i);

                                                                    }

                                                                }
                                                            });





                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
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
                        }
                    }
                });


                return true;
            }
        });

        hide_down3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_comDetail_bottom.setVisibility(View.VISIBLE);
                rl_comment3.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(tv_comment_content3.getWindowToken(), 0);
                tv_comment_content3.setText("");
            }
        });
    }



    //只是关闭软键盘
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void backHomepage(View v) throws Exception{
        this.finish();
    }
}
