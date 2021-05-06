package com.njxzc.myshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.adapter.DisGvImagesAdapter;
import com.njxzc.myshop.adapter.DisMsgAdapter;
import com.njxzc.myshop.customview.MyGridView;
import com.njxzc.myshop.customview.NestedExpandaleListView;
import com.njxzc.myshop.utils.DealComImageOther;
import com.njxzc.myshop.utils.GetStandardDate;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DiscussDetailActivity extends AppCompatActivity {
    private int discussId;
    private LinearLayout ll_userInfo;
    private ImageView iv_photo;
    private TextView tv_nickName;
    private ImageView iv_sex;
    private ImageView iv_up;
    private ImageView iv_comment;
    private TextView tv_discussDes;
    private TextView tv_school;
    private TextView tv_discussTime;
    private TextView tv_ups;
    private TextView tv_comments;
    private TextView tv_hits;
    private TextView tv_comment;
    private TextView tv_comment_content;
    private TextView tv_comment_content2;
    private TextView tv_comment_content3;
    private TextView hide_down;
    private TextView hide_down2;
    private TextView hide_down3;
    private Button bt_comment_send;
    private Button bt_comment_send2;
    private Button bt_comment_send3;
    private RelativeLayout rl_comment;
    private RelativeLayout rl_comment2;
    private RelativeLayout rl_comment3;
    private MyGridView gv_discussImages;
    private NestedScrollView nsv_DiscussDetail;
    private ArrayList<String> imagesData;
    private ArrayList<Map<String,Object>> disMsgDatas;
    private ArrayList<Map<String,Object>> disReplayDatas;
    private DisGvImagesAdapter disGvImagesAdapter;
    private NestedExpandaleListView lv_comments;
    private DisMsgAdapter disMsgAdapter;
    private SharedPreferences sp;
    public static int isAnonymity;
    public static int publisherId;
    private int discussUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_detail);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initView();
        init();
        initComment();
        clickComment();
        clickUp();
        clickUser();
    }



    private void initView() {
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        ll_userInfo = (LinearLayout) findViewById(R.id.ll_userInfo);
        iv_up = (ImageView) findViewById(R.id.iv_up);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_comment = (ImageView) findViewById(R.id.iv_comment);
        tv_discussDes = (TextView) findViewById(R.id.tv_discussDes);
        tv_school = (TextView) findViewById(R.id.tv_school);
        tv_discussTime = (TextView) findViewById(R.id.tv_discussTime);
        tv_ups = (TextView) findViewById(R.id.tv_ups);
        tv_comments = (TextView) findViewById(R.id.tv_comments);
        tv_hits = (TextView) findViewById(R.id.tv_hits);
        gv_discussImages = (MyGridView) findViewById(R.id.gv_discussImages);
        lv_comments = (NestedExpandaleListView) findViewById(R.id.lv_comments);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lv_comments.setNestedScrollingEnabled(false);
        }
        lv_comments.setGroupIndicator(null);// 去掉ExpandableListView 默认的箭头
        tv_comment_content = (TextView) findViewById(R.id.comment_content);
        tv_comment_content2 = (TextView) findViewById(R.id.comment_content2);
        tv_comment_content3 = (TextView) findViewById(R.id.comment_content3);
        hide_down = (TextView) findViewById(R.id.hide_down);
        hide_down2 = (TextView) findViewById(R.id.hide_down2);
        hide_down3 = (TextView) findViewById(R.id.hide_down3);
        bt_comment_send = (Button) findViewById(R.id.bt_comment_send);
        bt_comment_send2 = (Button) findViewById(R.id.bt_comment_send2);
        bt_comment_send3 = (Button) findViewById(R.id.bt_comment_send3);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        rl_comment2 = (RelativeLayout) findViewById(R.id.rl_comment2);
        rl_comment3 = (RelativeLayout) findViewById(R.id.rl_comment3);
        nsv_DiscussDetail = (NestedScrollView) findViewById(R.id.nsv_DiscussDetail);



    }

    private void init() {
        Intent intent = getIntent();
        discussId = intent.getIntExtra("discussId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final String url= OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/findDiscussByDiscussId";
                OkhttpHelper.findDiscussByDiscussId(url,String.valueOf(discussId) ,new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        try {
                            JSONObject data = new JSONObject(responseData);
                            JSONObject user = data.getJSONObject("user");
                            discussId = data.getInt("discussId");
                            final String discussDes = data.getString("discussDes");
                            final String discussImages = data.getString("discussImages");
                            final String discussTime = data.getString("discussTime");
                            //将字符串类型转为date类型
                            Date time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(discussTime);
                            //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                            final String tranDiscussTime = GetStandardDate.getTimeFormatText(time);
                            discussUp = data.getInt("discussUp");
                            final int discussComments = data.getInt("discussComments");
                            final int discussHits = data.getInt("discussHits");
                            publisherId = data.getInt("userId");
                            isAnonymity = data.getInt("isAnonymity");
                            final String nickName = user.getString("nickName");
                            final String profilePhoto = user.getString("profilePhoto");
                            final String school = user.getString("school");
                            final int sex = user.getInt("sex");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isAnonymity==0) {
                                        tv_nickName.setText(nickName);
                                        //显示图片
                                        //glide 为4.6.1版本
                                        Glide.with(DiscussDetailActivity.this)
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
                                    tv_discussTime.setText(tranDiscussTime);
                                    tv_ups.setText(String.valueOf(discussUp));
                                    tv_comments.setText(String.valueOf(discussComments));
                                    tv_hits.setText(String.valueOf(discussHits));
                                    if (sex==1){
                                        iv_sex.setImageResource(R.drawable.sex_man);
                                    }else {
                                        iv_sex.setImageResource(R.drawable.sex_woman);
                                    }
                                    imagesData = new ArrayList<>();
                                    if (discussImages.equals(null)||discussImages.equals("")){
                                        gv_discussImages.setVisibility(View.GONE);
                                    }else {
                                        imagesData = DealComImageOther.dealComImageOther(discussImages);
                                        System.out.println(imagesData);
                                        disGvImagesAdapter = new DisGvImagesAdapter(DiscussDetailActivity.this, imagesData);
                                        gv_discussImages.setAdapter(disGvImagesAdapter);

                                        gv_discussImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                imagesData = new ArrayList<String>();
                                                imagesData = DealComImageOther.dealComImageOther(discussImages);
                                                Intent imageItemIntent = new Intent(DiscussDetailActivity.this, GridViewItemsActivity.class);
                                                imageItemIntent.putExtra("list", imagesData);
                                                imageItemIntent.putExtra("position", position);
                                                startActivity(imageItemIntent);
                                            }
                                        });
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
            }
        });

    }

    private void initComment() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url = OkhttpHelper.getIp() + "/shopsystem/androidDisMsg/findDisMsgAndDisReplayBydiscussId";
                OkhttpHelper.findDisMsgAndDisReplayBydiscussId(url, String.valueOf(discussId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final String responseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseData);
                            disMsgDatas = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject list = jsonArray.getJSONObject(i);
                                JSONObject msg = list.getJSONObject("dismessage");
                                JSONObject user = msg.getJSONObject("user");
                                final int msgId = msg.getInt("msgId");
                                String msgDes = msg.getString("msgDes");
                                String msgTime = msg.getString("msgTime");
                                //将字符串类型转为date类型
                                Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                int sex = user.getInt("sex");
                                int userId = msg.getInt("userId");
                                String nickName = user.getString("nickName");
                                String photo = user.getString("profilePhoto");
                                final Map map = new HashMap();
                                map.put("msgId", msgId);
                                map.put("msgDes", msgDes);
                                map.put("msgTime", transMsgTime);
                                map.put("sex", sex);
                                map.put("nickName", nickName);
                                map.put("photo", photo);
                                map.put("userId", userId);
                                JSONArray replays = list.getJSONArray("disreplay");
                                System.out.println(replays);
                                disReplayDatas = new ArrayList<Map<String, Object>>();
                                for (int j = 0; j < replays.length(); j++) {
                                    JSONObject replay = replays.getJSONObject(j);
                                    JSONObject user2 = replay.getJSONObject("user");
                                    int replayId = replay.getInt("replayId");
                                    String replayDes = replay.getString("replayDes");
                                    String replayTime = replay.getString("replayTime");
                                    //将字符串类型转为date类型
                                    Date data2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                    //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                    String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                    int replayerId = replay.getInt("replayerId");
                                    int msgerId = replay.getInt("msgerId");
                                    int replaySex = user2.getInt("sex");
                                    String replayNickName = user2.getString("nickName");
                                    String msgerName = user2.getString("msgerName");
                                    String replayPhoto = user2.getString("profilePhoto");
                                    final Map map2 = new HashMap();
                                    map2.put("replayerId", replayerId);
                                    map2.put("replayId", replayId);
                                    map2.put("msgerId", msgerId);
                                    map2.put("replayDes", replayDes);
                                    map2.put("replayTime", transReplayTime);
                                    map2.put("msgerName", msgerName);
                                    map2.put("replaySex", replaySex);
                                    map2.put("replayNickName", replayNickName);
                                    map2.put("replayPhoto", replayPhoto);
                                    disReplayDatas.add(map2);
                                }
                                map.put("disReplayDatas", disReplayDatas);
                                disMsgDatas.add(map);
                            }
                            System.out.println(disMsgDatas);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    disMsgAdapter = new DisMsgAdapter(DiscussDetailActivity.this, disMsgDatas);
                                    lv_comments.setAdapter(disMsgAdapter);
                                    //第一次加载就展开所有的子类
                                    int groupCount = lv_comments.getCount();

                                    for (int i = 0; i < groupCount; i++) {
                                        lv_comments.expandGroup(i);

                                    }
                                    clickDisMsgItem();


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
    }

    private void clickUp() {
        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int newUps = discussUp+1;
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
    }

    private void clickComment() {
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Toast.makeText(DiscussDetailActivity.this, "留言内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    final int userId = sp.getInt("userId",0);
                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    final String msgTime = formatter.format(curDate);
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidDisMsg/addDisMessage";
                                OkhttpHelper.addDisMessage(url,String.valueOf(userId),String.valueOf(discussId),msgTime,msgDes,new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        try {
                                            final String responseData = response.body().string();
                                            JSONArray jsonArray = new JSONArray(responseData);
                                            disMsgDatas = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject list = jsonArray.getJSONObject(i);
                                                JSONObject msg = list.getJSONObject("dismessage");
                                                JSONObject user = msg.getJSONObject("user");
                                                final int msgId = msg.getInt("msgId");
                                                String msgDes = msg.getString("msgDes");
                                                String msgTime = msg.getString("msgTime");
                                                //将字符串类型转为date类型
                                                Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                                int sex = user.getInt("sex");
                                                int userId = msg.getInt("userId");
                                                String nickName = user.getString("nickName");
                                                String photo = user.getString("profilePhoto");
                                                final Map map = new HashMap();
                                                map.put("msgId", msgId);
                                                map.put("msgDes", msgDes);
                                                map.put("msgTime", transMsgTime);
                                                map.put("sex", sex);
                                                map.put("nickName", nickName);
                                                map.put("photo", photo);
                                                map.put("userId", userId);
                                                JSONArray replays = list.getJSONArray("disreplay");
                                                System.out.println(replays);
                                                disReplayDatas = new ArrayList<Map<String, Object>>();
                                                for (int j = 0; j < replays.length(); j++) {
                                                    JSONObject replay = replays.getJSONObject(j);
                                                    JSONObject user2 = replay.getJSONObject("user");
                                                    int replayId = replay.getInt("replayId");
                                                    String replayDes = replay.getString("replayDes");
                                                    String replayTime = replay.getString("replayTime");
                                                    //将字符串类型转为date类型
                                                    Date data2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                                    //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                    String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                                    int replayerId = replay.getInt("replayerId");
                                                    int msgerId = replay.getInt("msgerId");
                                                    int replaySex = user2.getInt("sex");
                                                    String replayNickName = user2.getString("nickName");
                                                    String msgerName = user2.getString("msgerName");
                                                    String replayPhoto = user2.getString("profilePhoto");
                                                    final Map map2 = new HashMap();
                                                    map2.put("replayerId", replayerId);
                                                    map2.put("replayId", replayId);
                                                    map2.put("msgerId", msgerId);
                                                    map2.put("replayDes", replayDes);
                                                    map2.put("replayTime", transReplayTime);
                                                    map2.put("msgerName", msgerName);
                                                    map2.put("replaySex", replaySex);
                                                    map2.put("replayNickName", replayNickName);
                                                    map2.put("replayPhoto", replayPhoto);
                                                    disReplayDatas.add(map2);
                                                }
                                                map.put("disReplayDatas", disReplayDatas);
                                                disMsgDatas.add(map);
                                            }
                                            System.out.println(disMsgDatas);


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(DiscussDetailActivity.this,"留言成功",Toast.LENGTH_SHORT).show();
                                                    closeKeyboard();
                                                    tv_comment_content.setText("");
                                                    disMsgAdapter = new DisMsgAdapter(DiscussDetailActivity.this, disMsgDatas);
                                                    lv_comments.setAdapter(disMsgAdapter);
                                                    //第一次加载就展开所有的子类
                                                    int groupCount = lv_comments.getCount();

                                                    for (int i = 0; i < groupCount; i++) {
                                                        lv_comments.expandGroup(i);

                                                    }
                                                    Handler handler = new Handler();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            nsv_DiscussDetail.fullScroll(nsv_DiscussDetail.FOCUS_DOWN);
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

    private void clickDisMsgItem() {

        //点击父项
        lv_comments.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
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
                String msgerName = (String) disMsgDatas.get(groupPosition).get("nickName");
                int userId = (int) disMsgDatas.get(groupPosition).get("userId");
                if (userId==publisherId&&isAnonymity==1){
                    tv_comment_content2.setHint("回复匿名用户：");
                }else {
                    tv_comment_content2.setHint("回复" + msgerName + "：");
                }
                bt_comment_send2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String replayDes = tv_comment_content2.getText().toString();
                        if(replayDes==""||replayDes.length()==0||replayDes==null){
                            Toast.makeText(DiscussDetailActivity.this, "留言内容不能为空", Toast.LENGTH_SHORT).show();
                        }else {
                            final int msgId = (int) disMsgDatas.get(groupPosition).get("msgId");
                            final int msgerId = (int) disMsgDatas.get(groupPosition).get("userId");
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
                                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidDisMsg/addDisReplay";
                                                OkhttpHelper.addDisReplay(url,String.valueOf(msgId),String.valueOf(msgerId),String.valueOf(replayerId),replayDes,replayTime,String.valueOf(discussId),new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(DiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        try {
                                                            final String responseData = response.body().string();
                                                            JSONArray jsonArray = new JSONArray(responseData);
                                                            disMsgDatas = new ArrayList<>();
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject list = jsonArray.getJSONObject(i);
                                                                JSONObject msg = list.getJSONObject("dismessage");
                                                                JSONObject user = msg.getJSONObject("user");
                                                                final int msgId = msg.getInt("msgId");
                                                                String msgDes = msg.getString("msgDes");
                                                                String msgTime = msg.getString("msgTime");
                                                                //将字符串类型转为date类型
                                                                Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                                                int sex = user.getInt("sex");
                                                                int userId = msg.getInt("userId");
                                                                String nickName = user.getString("nickName");
                                                                String photo = user.getString("profilePhoto");
                                                                final Map map = new HashMap();
                                                                map.put("msgId", msgId);
                                                                map.put("msgDes", msgDes);
                                                                map.put("msgTime", transMsgTime);
                                                                map.put("sex", sex);
                                                                map.put("nickName", nickName);
                                                                map.put("photo", photo);
                                                                map.put("userId", userId);
                                                                JSONArray replays = list.getJSONArray("disreplay");
                                                                System.out.println(replays);
                                                                disReplayDatas = new ArrayList<Map<String, Object>>();
                                                                for (int j = 0; j < replays.length(); j++) {
                                                                    JSONObject replay = replays.getJSONObject(j);
                                                                    JSONObject user2 = replay.getJSONObject("user");
                                                                    int replayId = replay.getInt("replayId");
                                                                    int msgerId = replay.getInt("msgerId");
                                                                    String replayDes = replay.getString("replayDes");
                                                                    String replayTime = replay.getString("replayTime");
                                                                    //将字符串类型转为date类型
                                                                    Date data2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                                                    //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                    String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                                                    int replayerId = replay.getInt("replayerId");
                                                                    int replaySex = user2.getInt("sex");
                                                                    String replayNickName = user2.getString("nickName");
                                                                    String msgerName = user2.getString("msgerName");
                                                                    String replayPhoto = user2.getString("profilePhoto");
                                                                    final Map map2 = new HashMap();
                                                                    map2.put("replayerId", replayerId);
                                                                    map2.put("replayId", replayId);
                                                                    map2.put("msgerId", msgerId);
                                                                    map2.put("replayDes", replayDes);
                                                                    map2.put("replayTime", transReplayTime);
                                                                    map2.put("msgerName", msgerName);
                                                                    map2.put("replaySex", replaySex);
                                                                    map2.put("replayNickName", replayNickName);
                                                                    map2.put("replayPhoto", replayPhoto);
                                                                    disReplayDatas.add(map2);
                                                                }
                                                                map.put("disReplayDatas", disReplayDatas);
                                                                disMsgDatas.add(map);
                                                            }
                                                            System.out.println(disMsgDatas);


                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(DiscussDetailActivity.this,"留言成功",Toast.LENGTH_SHORT).show();
                                                                    closeKeyboard();
                                                                    tv_comment_content2.setText("");
                                                                    disMsgAdapter = new DisMsgAdapter(DiscussDetailActivity.this, disMsgDatas);
                                                                    lv_comments.setAdapter(disMsgAdapter);
                                                                    //第一次加载就展开所有的子类
                                                                    int groupCount = lv_comments.getCount();

                                                                    for (int i = 0; i < groupCount; i++) {
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
                final ArrayList<Map<String, Object>> replayData = (ArrayList<Map<String, Object>>) disMsgDatas.get(i).get("disReplayDatas");
                String msgerName = (String) replayData.get(i1).get("replayNickName");
                int replayer = (int) replayData.get(i1).get("replayerId");
                if (replayer==publisherId&&isAnonymity==1){
                    tv_comment_content3.setHint("回复匿名用户：");
                }else {
                    tv_comment_content3.setHint("回复" + msgerName + "：");
                }
                bt_comment_send3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String replayDes = tv_comment_content3.getText().toString();
                        if(replayDes==""||replayDes.length()==0||replayDes==null){
                            Toast.makeText(DiscussDetailActivity.this, "留言内容不能为空", Toast.LENGTH_SHORT).show();
                        }else {
                            final int msgerId = (int) replayData.get(i1).get("replayerId");
                            final int msgId = (int) disMsgDatas.get(i).get("msgId");
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
                                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidDisMsg/addDisReplay";
                                                OkhttpHelper.addDisReplay(url,String.valueOf(msgId),String.valueOf(msgerId),String.valueOf(replayerId),replayDes,replayTime,String.valueOf(discussId),new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(DiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        try {
                                                            final String responseData = response.body().string();
                                                            JSONArray jsonArray = new JSONArray(responseData);
                                                            disMsgDatas = new ArrayList<>();
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject list = jsonArray.getJSONObject(i);
                                                                JSONObject msg = list.getJSONObject("dismessage");
                                                                JSONObject user = msg.getJSONObject("user");
                                                                final int msgId = msg.getInt("msgId");
                                                                String msgDes = msg.getString("msgDes");
                                                                String msgTime = msg.getString("msgTime");
                                                                //将字符串类型转为date类型
                                                                Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgTime);
                                                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                String transMsgTime = GetStandardDate.getTimeFormatText(data);
                                                                int sex = user.getInt("sex");
                                                                int userId = msg.getInt("userId");
                                                                String nickName = user.getString("nickName");
                                                                String photo = user.getString("profilePhoto");
                                                                final Map map = new HashMap();
                                                                map.put("msgId", msgId);
                                                                map.put("msgDes", msgDes);
                                                                map.put("msgTime", transMsgTime);
                                                                map.put("sex", sex);
                                                                map.put("nickName", nickName);
                                                                map.put("photo", photo);
                                                                map.put("userId", userId);
                                                                JSONArray replays = list.getJSONArray("disreplay");
                                                                System.out.println(replays);
                                                                disReplayDatas = new ArrayList<Map<String, Object>>();
                                                                for (int j = 0; j < replays.length(); j++) {
                                                                    JSONObject replay = replays.getJSONObject(j);
                                                                    JSONObject user2 = replay.getJSONObject("user");
                                                                    int replayId = replay.getInt("replayId");
                                                                    int msgerId = replay.getInt("msgerId");
                                                                    String replayDes = replay.getString("replayDes");
                                                                    String replayTime = replay.getString("replayTime");
                                                                    //将字符串类型转为date类型
                                                                    Date data2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(replayTime);
                                                                    //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                                                    String transReplayTime = GetStandardDate.getTimeFormatText(data2);
                                                                    int replayerId = replay.getInt("replayerId");
                                                                    int replaySex = user2.getInt("sex");
                                                                    String replayNickName = user2.getString("nickName");
                                                                    String msgerName = user2.getString("msgerName");
                                                                    String replayPhoto = user2.getString("profilePhoto");
                                                                    final Map map2 = new HashMap();
                                                                    map2.put("replayerId", replayerId);
                                                                    map2.put("msgerId", msgerId);
                                                                    map2.put("replayId", replayId);
                                                                    map2.put("replayDes", replayDes);
                                                                    map2.put("replayTime", transReplayTime);
                                                                    map2.put("msgerName", msgerName);
                                                                    map2.put("replaySex", replaySex);
                                                                    map2.put("replayNickName", replayNickName);
                                                                    map2.put("replayPhoto", replayPhoto);
                                                                    disReplayDatas.add(map2);
                                                                }
                                                                map.put("disReplayDatas", disReplayDatas);
                                                                disMsgDatas.add(map);
                                                            }
                                                            System.out.println(disMsgDatas);


                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(DiscussDetailActivity.this,"留言成功",Toast.LENGTH_SHORT).show();
                                                                    closeKeyboard();
                                                                    tv_comment_content3.setText("");
                                                                    disMsgAdapter = new DisMsgAdapter(DiscussDetailActivity.this, disMsgDatas);
                                                                    lv_comments.setAdapter(disMsgAdapter);
                                                                    //第一次加载就展开所有的子类
                                                                    int groupCount = lv_comments.getCount();

                                                                    for (int i = 0; i < groupCount; i++) {
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
                rl_comment3.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(tv_comment_content3.getWindowToken(), 0);
                tv_comment_content3.setText("");
            }
        });


    }

    private void clickUser() {
        ll_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiscussDetailActivity.this,PerHomePageActivity.class);
                intent.putExtra("userId",publisherId);
                startActivity(intent);

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

    public void back(View v) throws Exception{
        this.finish();
    }
}
