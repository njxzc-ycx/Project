package com.njxzc.myshop;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.utils.EditTextPwdDeleteView;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;

public class CodeLoginActivity extends AppCompatActivity {

    String APPKEY="3235b0b37cfbd";
    String APPSECRET="38a466b58df315d50f2e50313917680b";
    private EditText et_rgPhone;
    private EditText et_rgCode;
    private TextView getCode;
    //倒计时显示   可以手动更改。
    int i = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_login);
        initCompoment();//调用组件初始化方法
    }

    private void initCompoment() {
        et_rgPhone = (EditText) findViewById(R.id.et_rgPhone);
        et_rgCode = (EditText) findViewById(R.id.et_rgCode);
        getCode = (TextView) findViewById(R.id.getCode);

        //启动短信验证
        MobSDK.init(this,APPKEY,APPSECRET);
        EventHandler eventHandler = new EventHandler(){
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void GetCode(View v) throws Exception{
        final String phone = et_rgPhone.getText().toString();
        if(!judgePhoneNums(phone)) {
            return;
        }else{
            // 1. 通过sdk发送短信验证
            SMSSDK.getVerificationCode("86", phone);
            // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
            getCode.setClickable(false);
            getCode.setText("重新发送(" + i + ")");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (; i > 0; i--) {
                        handler.sendEmptyMessage(-9);
                        if (i <= 0) {
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendEmptyMessage(-8);
                }
            }).start();
        }
    }

    public void login(View v) throws Exception{
        //1.获取输入的用户名密码
        String phone = et_rgPhone.getText().toString();
        String code = et_rgCode.getText().toString();
        if(code==""||code.length()==0||code==null){
            Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
        }
        else{
            //将收到的验证码和手机号提交再次核对
            SMSSDK.submitVerificationCode("86", phone,code);
        }

    }

    /**
     *
     */
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                getCode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                getCode.setText("获取验证码");
                getCode.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                Log.e("result", "result=" + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    final String userName = et_rgPhone.getText().toString();
                    final String phone= et_rgPhone.getText().toString();
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String url = "http://192.168.0.103:8088/shopsystem/androidUser/findUserPhone";
                                OkhttpHelper.codeLogin(url, phone, new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(CodeLoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String responseData = response.body().string();
                                        if (responseData == "") {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(CodeLoginActivity.this, "手机号未注册", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else {
                                            try {
                                                final JSONObject user = new JSONObject(responseData);
                                                final User getUser = new User();
                                                getUser.setUserId(user.getInt("userId"));
                                                getUser.setUserName(user.getString("userName"));
                                                getUser.setNickName(user.getString("nickName"));
                                                getUser.setSchool(user.getString("school"));
                                                getUser.setProfilePhoto(user.getString("profilePhoto"));
                                                if (user.getInt("state") == 0) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(CodeLoginActivity.this, "您的账号已被封禁！如有疑问,请联系客服", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(CodeLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                            AppInfo appInfo = (AppInfo) getApplication();
                                                            appInfo.setUser(getUser);//将用户信息保存在AppInfo中
                                                            Intent intent = new Intent(CodeLoginActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            CodeLoginActivity.this.finish();
                                                        }
                                                    });

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                });
                            }
                        }).start();

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    ((Throwable) data).printStackTrace();
                    String responseData = ((Throwable) data).getMessage().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String message = jsonObject.getString("description");
                        Toast.makeText(CodeLoginActivity.this,message,Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    public void backLogin(View v) throws Exception{
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    //重写onKeyDown方法 点击手机返回键可以回到主界面
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        //反注册回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
