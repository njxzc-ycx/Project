package com.njxzc.myshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.adapter.MyFragmentPagerAdapter;
import com.njxzc.myshop.fragment.perComFragment;
import com.njxzc.myshop.fragment.perDisFragment;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PerHomePageActivity extends AppCompatActivity {
    private TextView tv_nickName;
    private TextView tv_school;
    private ImageView iv_sex;
    private ImageView iv_photo;
    private ViewPager viewpaper;
    private TabLayout tab_layout;
    private Button btn_follow;
    private Button btn_nofollow;
    private String[] titles = {"二手","帖子"};
    private List<Fragment> data;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_home_page);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Model.getInstance().init(this);
        initView();
        initUser();
        //初始化Fragment
        initFragment();
        initFollow();
        ClickBtn();
    }




    private void initView() {
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_school = (TextView) findViewById(R.id.tv_school);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        viewpaper = (ViewPager) findViewById(R.id.viewpaper);
        btn_follow = (Button) findViewById(R.id.btn_follow);
        btn_nofollow = (Button) findViewById(R.id.btn_nofollow);
    }

    private void initFragment() {
        //初始化需要显示的所有界面
        data = new ArrayList<>();
        data.add(new perComFragment());
        data.add(new perDisFragment());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager,data,titles);
        viewpaper.setAdapter(fragmentPagerAdapter);
        tab_layout.setupWithViewPager(viewpaper);
    }

    private void initUser() {
        final int userId = getIntent().getIntExtra("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url = OkhttpHelper.getIp()+"/shopsystem/androidUser/findUserByUserId";
                OkhttpHelper.findUserByUserId(url, String.valueOf(userId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PerHomePageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        try {
                            JSONObject user = new JSONObject(responseData);
                            final String profilePhoto = user.getString("profilePhoto");
                            final String nickName = user.getString("nickName");
                            final String school = user.getString("school");
                            final int sex = user.getInt("sex");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_nickName.setText(nickName);
                                    tv_school.setText(school);
                                    Glide.with(PerHomePageActivity.this)
                                            .load(profilePhoto).asBitmap()
                                            .error(R.drawable.errorpic)
                                            .placeholder(R.drawable.defaultpic)
                                            .into(iv_photo);
                                    if (sex==1){
                                        iv_sex.setImageResource(R.drawable.sex_man);
                                    }else {
                                        iv_sex.setImageResource(R.drawable.sex_woman);
                                    }

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });



    }

    private void initFollow() {
        final int userId = getIntent().getIntExtra("userId",0);
        final int userId2 = sp.getInt("userId",0);
        if (userId2==userId){
            btn_follow.setVisibility(View.GONE);
        }else {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String url = OkhttpHelper.getIp()+"/shopsystem/androidFollow/isFollowed";
                    OkhttpHelper.isFollowed(url,String.valueOf(userId) ,String.valueOf(userId2), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PerHomePageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            if (responseData.equals("isFollowed")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn_follow.setVisibility(View.GONE);
                                        btn_nofollow.setVisibility(View.VISIBLE);
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn_follow.setVisibility(View.VISIBLE);
                                        btn_nofollow.setVisibility(View.GONE);
                                    }
                                });
                            }

                        }
                    });
                }
            });
        }

    }

    private void ClickBtn() {
        final int userId = getIntent().getIntExtra("userId",0);
        final int userId2 = sp.getInt("userId",0);
        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        String url = OkhttpHelper.getIp()+"/shopsystem/androidFollow/joinFollowed";
                        OkhttpHelper.Followed(url,String.valueOf(userId) ,String.valueOf(userId2), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PerHomePageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                if (responseData.equals("joinSuccess")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PerHomePageActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                            btn_follow.setVisibility(View.GONE);
                                            btn_nofollow.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else if(responseData.equals("joinFail")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PerHomePageActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        });
                    }
                });
            }
        });

        btn_nofollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        String url = OkhttpHelper.getIp()+"/shopsystem/androidFollow/cancelFollowed";
                        OkhttpHelper.Followed(url,String.valueOf(userId) ,String.valueOf(userId2), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PerHomePageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                if (responseData.equals("cancelSuccess")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PerHomePageActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                            btn_follow.setVisibility(View.VISIBLE);
                                            btn_nofollow.setVisibility(View.GONE);
                                        }
                                    });
                                }else if(responseData.equals("cancelFail")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PerHomePageActivity.this, "取消关注失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        });
                    }
                });
            }
        });

    }



    public void backDiscussFragment(View v) throws Exception{
        this.finish();
    }
}
