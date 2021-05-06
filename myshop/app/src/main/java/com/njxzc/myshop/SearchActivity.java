package com.njxzc.myshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.njxzc.myshop.adapter.ComAdapter;
import com.njxzc.myshop.adapter.SearchComAdapter;
import com.njxzc.myshop.personal_activity.SettingsActivity;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<Map<String,Object>> datas;
    private SearchComAdapter searchComAdapter;
    private EditText search_et_input;
    private GridView gv_searchCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Model.getInstance().init(this);
        initView();
        init();
        clickItem();
    }


    private void initView() {
        search_et_input = (EditText) findViewById(R.id.search_et_input);
        gv_searchCom = (GridView) findViewById(R.id.gv_searchCom);
    }

    private void init() {
        search_et_input.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;//用于临时保存关键词
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;//文本变化时,将内容给临时变量
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String key = temp.toString();
                if (key==null||key.length()==0||key=="") {
                    datas.clear();
                    searchComAdapter.notifyDataSetChanged();
                }
                else {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url = OkhttpHelper.getIp() + "/shopsystem/androidCom/searchComBykey";
                                OkhttpHelper.searchCom(url, key, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(SearchActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        try {
                                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                                            JSONArray jsonArray = new JSONArray(responseData);
                                            datas = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject list = jsonArray.getJSONObject(i);
                                                JSONObject user = list.getJSONObject("user");
                                                String school = user.getString("school");
                                                String nickName = user.getString("nickName");
                                                String userName = user.getString("userName");
                                                String profilePhoto = user.getString("profilePhoto");
                                                int sex = user.getInt("sex");
                                                String comImageMain = list.getString("comImageMain");
                                                String comImageOthers = list.getString("comImageOther");
                                                String updateTime = list.getString("updateTime");
                                                String onTime = list.getString("onTime");
                                                String inTime = list.getString("inTime");
                                                String comName = list.getString("comName");
                                                int sellerId = list.getInt("sellerId");
                                                int counts = list.getInt("count");
                                                int comId = list.getInt("comId");
                                                int flag = list.getInt("flag");
                                                int hits = list.getInt("hits");
                                                String des = list.getString("des");
                                                Double currentPrice = list.getDouble("currentPrice");
                                                Double primePrice = list.getDouble("primePrice");
                                                int isBargain = list.getInt("isBargain");
                                                int collects = list.getInt("collects");
                                                Map map = new HashMap();
                                                map.put("comImageMain",comImageMain);
                                                map.put("comName",comName);
                                                map.put("userName",userName);
                                                map.put("des",des);
                                                map.put("counts",counts);
                                                map.put("hits",hits);
                                                map.put("flag",flag);
                                                map.put("comId",comId);
                                                map.put("updateTime",updateTime);
                                                map.put("sellerId",sellerId);
                                                map.put("currentPrice",currentPrice);
                                                map.put("primePrice",primePrice);
                                                map.put("isBargain",isBargain);
                                                map.put("collects",collects);
                                                map.put("school",school);
                                                map.put("nickName",nickName);
                                                map.put("sex",sex);
                                                map.put("profilePhoto",profilePhoto);
                                                map.put("comImageOthers",comImageOthers);
                                                map.put("onTime",onTime);
                                                map.put("inTime",inTime);
                                                datas.add(map);
                                            }
                                            System.out.println(datas);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    searchComAdapter = new SearchComAdapter(SearchActivity.this, datas);
                                                    gv_searchCom.setAdapter(searchComAdapter);
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(SearchActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
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

    public void toSearch(View v) throws Exception{
        closeKeyboard();
        final String key = search_et_input.getText().toString();
        if (key==null||key.length()==0||key=="") {
            Toast.makeText(SearchActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
        }
        else {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = OkhttpHelper.getIp() + "/shopsystem/androidCom/searchComBykey";
                        OkhttpHelper.searchCom(url, key, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SearchActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    datas = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject list = jsonArray.getJSONObject(i);
                                        JSONObject user = list.getJSONObject("user");
                                        String school = user.getString("school");
                                        String nickName = user.getString("nickName");
                                        String userName = user.getString("userName");
                                        String profilePhoto = user.getString("profilePhoto");
                                        int sex = user.getInt("sex");
                                        String comImageMain = list.getString("comImageMain");
                                        String comImageOthers = list.getString("comImageOther");
                                        String updateTime = list.getString("updateTime");
                                        String onTime = list.getString("onTime");
                                        String inTime = list.getString("inTime");
                                        String comName = list.getString("comName");
                                        int sellerId = list.getInt("sellerId");
                                        int counts = list.getInt("count");
                                        int comId = list.getInt("comId");
                                        int flag = list.getInt("flag");
                                        int hits = list.getInt("hits");
                                        String des = list.getString("des");
                                        Double currentPrice = list.getDouble("currentPrice");
                                        Double primePrice = list.getDouble("primePrice");
                                        int isBargain = list.getInt("isBargain");
                                        int collects = list.getInt("collects");
                                        Map map = new HashMap();
                                        map.put("comImageMain",comImageMain);
                                        map.put("comName",comName);
                                        map.put("userName",userName);
                                        map.put("des",des);
                                        map.put("counts",counts);
                                        map.put("hits",hits);
                                        map.put("flag",flag);
                                        map.put("comId",comId);
                                        map.put("updateTime",updateTime);
                                        map.put("sellerId",sellerId);
                                        map.put("currentPrice",currentPrice);
                                        map.put("primePrice",primePrice);
                                        map.put("isBargain",isBargain);
                                        map.put("collects",collects);
                                        map.put("school",school);
                                        map.put("nickName",nickName);
                                        map.put("sex",sex);
                                        map.put("profilePhoto",profilePhoto);
                                        map.put("comImageOthers",comImageOthers);
                                        map.put("onTime",onTime);
                                        map.put("inTime",inTime);
                                        datas.add(map);
                                    }
                                    System.out.println(datas);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            searchComAdapter = new SearchComAdapter(SearchActivity.this, datas);
                                            gv_searchCom.setAdapter(searchComAdapter);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SearchActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
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
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = OkhttpHelper.getIp() + "/shopsystem/androidCom/searchComBykey";
                        OkhttpHelper.searchCom(url, key, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SearchActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    datas = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject list = jsonArray.getJSONObject(i);
                                        JSONObject user = list.getJSONObject("user");
                                        String school = user.getString("school");
                                        String nickName = user.getString("nickName");
                                        String profilePhoto = user.getString("profilePhoto");
                                        int sex = user.getInt("sex");
                                        String comImageMain = list.getString("comImageMain");
                                        String comImageOthers = list.getString("comImageOther");
                                        String onTime = list.getString("onTime");
                                        String comName = list.getString("comName");
                                        int sellerId = list.getInt("sellerId");
                                        int comId = list.getInt("comId");
                                        int hits = list.getInt("hits");
                                        String des = list.getString("des");
                                        Double currentPrice = list.getDouble("currentPrice");
                                        Double primePrice = list.getDouble("primePrice");
                                        int isBargain = list.getInt("isBargain");
                                        int collects = list.getInt("collects");
                                        Map map = new HashMap();
                                        map.put("comImageMain",comImageMain);
                                        map.put("comName",comName);
                                        map.put("des",des);
                                        map.put("hits",hits);
                                        map.put("comId",comId);
                                        map.put("sellerId",sellerId);
                                        map.put("currentPrice",currentPrice);
                                        map.put("primePrice",primePrice);
                                        map.put("isBargain",isBargain);
                                        map.put("collects",collects);
                                        map.put("school",school);
                                        map.put("nickName",nickName);
                                        map.put("sex",sex);
                                        map.put("profilePhoto",profilePhoto);
                                        map.put("comImageOthers",comImageOthers);
                                        map.put("onTime",onTime);
                                        datas.add(map);
                                    }
                                    System.out.println(datas);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            comAdapter = new ComAdapter(SearchActivity.this, datas);
                                            gv_searchCom.setAdapter(comAdapter);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SearchActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }

                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();*/

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

    private void clickItem() {
        gv_searchCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Toast.makeText(SearchActivity.this,
                        "点击位置"+position+searchdatas.get(position).get("comName"),
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(SearchActivity.this,ComDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("datas",(Serializable) datas);
                startActivity(intent);
            }
        });
    }

    public void backHomepage(View v) throws Exception{
        this.finish();
    }
}
