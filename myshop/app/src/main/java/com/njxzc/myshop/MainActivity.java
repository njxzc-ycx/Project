package com.njxzc.myshop;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.njxzc.myshop.base.BaseFragment;
import com.njxzc.myshop.fragment.CollectFrameFragment;
import com.njxzc.myshop.fragment.HomepageFrameFragment;
import com.njxzc.myshop.fragment.NewmsgFrameFragment;
import com.njxzc.myshop.fragment.PersonalFrameFragment;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.test.TestgridviewComActivity;
import com.njxzc.myshop.utils.ComAdapter;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends FragmentActivity {

    private RadioGroup mRg_main;
    private List<BaseFragment> mBaseFragment;
    private int position;//选中的Fragment对应的位置
    private Fragment mContent;//上次切换到的Fragment
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCompoment();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
    }


    private void initCompoment() {
        mRg_main = (RadioGroup) findViewById(R.id.rg_main);
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new HomepageFrameFragment());//主页框架Fragment
        mBaseFragment.add(new CollectFrameFragment());//收藏框架Fragment
        mBaseFragment.add(new NewmsgFrameFragment());//消息框架Fragment
        mBaseFragment.add(new PersonalFrameFragment());//我的框架Fragment
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
            //才切换
            //判断有没有被添加
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
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }





}
