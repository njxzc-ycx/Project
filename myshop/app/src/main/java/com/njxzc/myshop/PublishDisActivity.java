package com.njxzc.myshop;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.njxzc.myshop.adapter.ListPicsAdapter;
import com.njxzc.myshop.customview.MyGridView;
import com.njxzc.myshop.utils.CompressHelper;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishDisActivity extends AppCompatActivity {
    private ImageView iv_addbtn;
    private TextView tv_type;
    private TextView tv_textCount;
    private EditText et_disDes;
    private MyGridView gv_listPic;
    private LinearLayout ll_chooseType;
    private ArrayList<String> photodatas;
    private ListPicsAdapter listPicsAdapter;
    private RadioButton rb_isAnonymous;
    private RadioButton rb_noAnonymous;
    public static int count = 6;
    private SharedPreferences sp;
    private int discussTypeId = 0;
    private int isAnonymity = 2;
    private int num = 500;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_dis);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initView();
        Model.getInstance().init(this);
        photodatas = new ArrayList<>();

        //广播接收分类activity传来的数据
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("getDiscussType".equals(msg)) {
                    String discussTypeName = intent.getStringExtra("discussTypeName");
                    discussTypeId = intent.getIntExtra("discussTypeId",0);
                    tv_type.setText(discussTypeName);
                }
            }
        };

        broadcastManager.registerReceiver(receiver, intentFilter);
        //点击添加图片 进入多选图片
        iv_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 6;
                PhotoPicker.builder()
                        .setPhotoCount(count)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(PublishDisActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });
    }

    private void initView() {
        iv_addbtn = (ImageView) findViewById(R.id.iv_addbtn);
        et_disDes = (EditText) findViewById(R.id.et_disDes);
        gv_listPic = (MyGridView) findViewById(R.id.gv_listPic);
        ll_chooseType = (LinearLayout) findViewById(R.id.ll_chooseType);
        rb_isAnonymous = (RadioButton) findViewById(R.id.rb_isAnonymous);
        rb_noAnonymous = (RadioButton) findViewById(R.id.rb_noAnonymous);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_textCount = (TextView) findViewById(R.id.tv_textCount);
        ll_chooseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishDisActivity.this,ChooseDisTypeActivity.class);
                startActivity(intent);
            }
        });
        et_disDes.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                wordNum= s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = s.length();
                //TextView显示剩余字数
                tv_textCount.setText(number+"/"+num);
                selectionStart=et_disDes.getSelectionStart();
                selectionEnd = et_disDes.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    et_disDes.setText(s);
                    et_disDes.setSelection(tempSelection);//设置光标在最后
                }

            }
        });


    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                count -= photos.size();
                photodatas.addAll(photos);
                listPicsAdapter = new ListPicsAdapter(PublishDisActivity.this,photodatas);
                gv_listPic.setAdapter(listPicsAdapter);
                gv_listPic.setVisibility(View.VISIBLE);
            }
        }
    }
    public void publish(View v) throws Exception{
        final int userId = sp.getInt("userId",0);
        final String discussDes = et_disDes.getText().toString();
        if (rb_isAnonymous.isChecked()){
            isAnonymity=1;
        }else if (rb_noAnonymous.isChecked()){
            isAnonymity=0;
        }
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        final String discussTime = formatter.format(curDate);
        if (discussDes==""||discussDes.length()==0||discussDes==null){
            Toast.makeText(PublishDisActivity.this,"发布内容不得为空",Toast.LENGTH_SHORT).show();
        }else if(discussTypeId==0){
            Toast.makeText(PublishDisActivity.this,"请选择标签",Toast.LENGTH_SHORT).show();
        }else if(isAnonymity==2){
            Toast.makeText(PublishDisActivity.this,"请选择是否匿名",Toast.LENGTH_SHORT).show();
        }
        else {
            mProgressDialog = ProgressDialog.show(PublishDisActivity.this, "", "正在发布...");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    String url = OkhttpHelper.getIp() + "/shopsystem/androidDiscuss/addDiscuss";
                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    for (int i = 0; i < photodatas.size(); i++) { //对文件进行遍历
                        File file = new File(photodatas.get(i)); //生成文件
                        //压缩图片
                        File comImageOtherCompressed = CompressHelper.getDefault(getApplicationContext()).compressToFile(file);
                        requestBody.addFormDataPart( //给Builder添加上传的文件
                                "discussImagesFiles",  //请求的名字
                                file.getName(), //文件的文字，服务器端用来解析的
                                RequestBody.create(MediaType.parse("image/*"), comImageOtherCompressed) //创建RequestBody，把上传的文件放入
                        );
                    }
                    requestBody.addFormDataPart("userId", String.valueOf(userId));
                    requestBody.addFormDataPart("discussDes", discussDes);
                    requestBody.addFormDataPart("typeId", String.valueOf(discussTypeId));
                    requestBody.addFormDataPart("isAnonymity", String.valueOf(isAnonymity));
                    requestBody.addFormDataPart("discussTime", discussTime);

                    final Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody.build())
                            .build();
                    client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PublishDisActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            System.out.println(responseData);
                            if (responseData.equals("addSuccess")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PublishDisActivity.this, "发布圈子成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                        intent.putExtra("flag","refreshDiscuss");
                                        intent.putExtra("type",discussTypeId);
                                        LocalBroadcastManager.getInstance(PublishDisActivity.this).sendBroadcast(intent);
                                        sendBroadcast(intent);
                                        PublishDisActivity.this.finish();
                                    }
                                });
                            } else if (responseData.equals("addFail")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PublishDisActivity.this, "发布圈子失败", Toast.LENGTH_SHORT).show();
                                        PublishDisActivity.this.finish();
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

    public void backHomepage(View v) throws Exception{
        this.finish();
    }
}
