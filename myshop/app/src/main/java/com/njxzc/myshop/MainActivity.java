package com.njxzc.myshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.njxzc.myshop.base.BaseFragment;
import com.njxzc.myshop.fragment.DiscussFrameFragment;
import com.njxzc.myshop.fragment.HomepageFrameFragment;
import com.njxzc.myshop.fragment.NewmsgFrameFragment;
import com.njxzc.myshop.fragment.PersonalFrameFragment;
import com.njxzc.myshop.personal_activity.SettingsActivity;
import com.njxzc.myshop.personal_activity.UpdateSchoolActivity;
import com.njxzc.myshop.utils.Constant;
import com.njxzc.myshop.utils.Model;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private RadioGroup mRg_main;
    private RadioButton rb_homepage;
    private RadioButton rb_collect;
    private RadioButton rb_newmsg;
    private RadioButton rb_personal;
    private ImageView iv_publish;
    private List<BaseFragment> mBaseFragment;
    private int position;//选中的Fragment对应的位置
    private Fragment mContent;//上次切换到的Fragment
    private SharedPreferences sp;
    private long exitTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initCompoment();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
        checkSchool();
        NewIntent();
    }


    private void initCompoment() {
        mRg_main = (RadioGroup) findViewById(R.id.rg_main);
        rb_homepage = (RadioButton) findViewById(R.id.rb_homepage);
        rb_collect = (RadioButton) findViewById(R.id.rb_collect);
        rb_newmsg = (RadioButton) findViewById(R.id.rb_newmsg);
        rb_personal = (RadioButton) findViewById(R.id.rb_personal);
        iv_publish = (ImageView) findViewById(R.id.iv_publish);
        RadioButton[] rb = new RadioButton[4];
        rb[0] = rb_homepage;
        rb[1] = rb_collect;
        rb[2] = rb_newmsg;
        rb[3] = rb_personal;
        for(RadioButton r:rb){
            Drawable[] drawables = r.getCompoundDrawables();
            Rect rect = new Rect(0,0,drawables[1].getMinimumWidth()/2,drawables[1].getMinimumHeight()/2);
            drawables[1].setBounds(rect);
            r.setCompoundDrawables(null , drawables[1] , null ,null);
        }
        iv_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PublishComActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new HomepageFrameFragment());//主页框架Fragment
        mBaseFragment.add(new DiscussFrameFragment());//发现框架Fragment
        mBaseFragment.add(new NewmsgFrameFragment());//消息框架Fragment
        mBaseFragment.add(new PersonalFrameFragment());//我的框架Fragment
    }

    private void checkSchool() {
        String school = sp.getString("school","");
        System.out.println("学校："+school);
        if (school.equals("")||school==""||school.length()==0||school.equals("null")){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //防止返回键把对话框取消掉
            builder.setCancelable(false);
            builder.setMessage("您还未绑定您的学校信息");
            builder.setTitle("提示");
            builder.setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Intent intent = new Intent(MainActivity.this,UpdateSchoolActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            });
            //显示对话框
            builder.create().show();
        }



    }

    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_homepage);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId){
                case R.id.rb_homepage://主页框架
                    position = 0;
                    break;
                case R.id.rb_collect://收藏框架
                    position = 1;
                    break;
                case R.id.rb_newmsg://消息框架
                    position = 2;
                    break;
                case R.id.rb_personal://我的框架
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();
            //替换
            switchFragment(mContent,to);

        }

    }

    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    /*
    *@param from 刚显示的Fragment,马上就要被隐藏
    *@param to 马上要切换到的Fragment,一会要显示
    */
    private void switchFragment(Fragment from,Fragment to) {
        if(from != to){
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //判断有没有被添加才切换
            if(!to.isAdded()){
                //to没有被添加
                //from隐藏
                if(from != null){
                    ft.hide(from);
                }
                //添加to
                if(to != null){
                    ft.add(R.id.fl_content,to).commit();
                }
            }else {
                //to已经被添加
                //from隐藏
                if(from != null){
                    ft.hide(from);
                }
                //显示to
                if(to != null){
                    ft.show(to).commit();
                }
            }

        }
    }

    /*private void switchFragment(BaseFragment fragment) {
        //1.得到FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        //3.替换
        transaction.replace(R.id.fl_content,fragment);
        //4.提交事务
        transaction.commit();
    }*/


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次返回键退出淘校园", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        EMClient.getInstance().logout(true);
                    }
                }.start();
                try {
                    Thread.sleep(100);
                    finish();
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void NewIntent() {
        Intent intent = getIntent();
        if (intent.getIntExtra("isConflict",0)==202){
            Constant.ACCOUNT_CONFLICT=0;
            showConflictDialog();
        }
    }

    private void showConflictDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        TextView title = new TextView(this);
        title.setText("下线通知");
        title.setPadding(0, 25, 0, 0);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(18);
        title.setTextColor(Color.BLACK);
        builder.setCustomTitle(title);
        builder.setMessage("您的账号在另一台Android手机登录了淘校园，如非本人操作，则密码可能已泄露，建议修改密码。");
        //环信退出登录
        new Thread(){
            @Override
            public void run() {
                super.run();
                EMClient.getInstance().logout(true);
            }
        }.start();
        //保证退出下次不再自动登录
        //将sharedPreferences中保存的用户名，密码清空 自动登录设为false
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName","");
        editor.putString("userPwd","");
        editor.putString("profilePhoto","");
        editor.putString("school","");
        editor.putString("nickName","");
        editor.putInt("userId",0);
        editor.putBoolean("is_auto",false);
        editor.commit();
        //防止返回键把对话框取消掉
        builder.setCancelable(false);

        //确定按钮
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                //跳转到登录界面后，并将栈底的Activity全部都销毁
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        //显示对话框
        builder.create().show();
    }


}
