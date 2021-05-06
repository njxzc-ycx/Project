package com.njxzc.myshop.personal_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;

import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.AllAddressAdapter;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class addAddressActivity extends AppCompatActivity {
    private EditText et_linkman;
    private EditText et_linkphone;
    private EditText et_addressDetail;
    private TextView tv_area;
    private LinearLayout ll_area;
    private Button btn_submit;
    private CheckBox cb_isdefault;
    private SharedPreferences sp;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Model.getInstance().init(this);
        initView();
        init();
    }


    private void initView() {
        et_linkman = (EditText) findViewById(R.id.et_linkman);
        et_linkphone = (EditText) findViewById(R.id.et_linkphone);
        et_addressDetail = (EditText) findViewById(R.id.et_addressDetail);
        tv_area = (TextView) findViewById(R.id.tv_area);
        ll_area = (LinearLayout) findViewById(R.id.ll_area);
        cb_isdefault = (CheckBox) findViewById(R.id.cb_isdefault);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    private void init() {
        //点击地区信息 进行地区选择
        ll_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断输入法的隐藏状态
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    selectAddress();//调用CityPicker选取区域

                }
            }
        });

    }

    private void selectAddress() {
        CityPicker cityPicker = new CityPicker.Builder(addAddressActivity.this)
                .textSize(16)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#64B5F6")
                .cancelTextColor("#64B5F6")
                .province("江苏省")
                .city("南京市")
                .district("江宁区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(9)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                //为TextView赋值
                tv_area.setText(province.trim() + "," + city.trim() + "," + district.trim());
            }
        });
    }

    public void submit(View v) throws Exception{
        closeKeyboard();
        final String linkman = et_linkman.getText().toString();
        final String linkphone = et_linkphone.getText().toString();
        final String area = (String) tv_area.getText();
        final String addressDetail = et_addressDetail.getText().toString();
        final int userId = sp.getInt("userId",0);
        final int isdefault;
        if (cb_isdefault.isChecked()){
            isdefault = 1;
        }else {
            isdefault = 0;
        }
        if (linkman==""||linkman.length()==0||linkman==null) {
            Toast.makeText(addAddressActivity.this,"收件人不得为空",Toast.LENGTH_SHORT).show();
        }else if (addressDetail==""||addressDetail.length()==0||addressDetail==null){
            Toast.makeText(addAddressActivity.this,"详细地址不得为空",Toast.LENGTH_SHORT).show();
        } else if(!judgePhoneNums(linkphone)){
            return;
        }else {
            //start the progress dialog
            mProgressDialog = ProgressDialog.show(addAddressActivity.this, "", "正在提交...");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = OkhttpHelper.getIp() + "/shopsystem/androidAddress/addAddress";
                    OkhttpHelper.addAddress(url, String.valueOf(userId), addressDetail, area, linkphone, linkman, String.valueOf(isdefault), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(addAddressActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseData = response.body().string();
                            if (responseData.equals("addSuccess")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(addAddressActivity.this, "新增地址成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                        intent.putExtra("flag", "refreshAddress");
                                        LocalBroadcastManager.getInstance(addAddressActivity.this).sendBroadcast(intent);
                                        sendBroadcast(intent);
                                        addAddressActivity.this.finish();
                                    }
                                });
                            } else if (responseData.equals("addFail")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(addAddressActivity.this, "新增地址失败", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            // dismiss the progress dialog
                            mProgressDialog.dismiss();
                        }
                    });


                }
            });

        }

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

    public void backPersonalInfo(View v) throws Exception{
        this.finish();
    }
}
