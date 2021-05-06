package com.njxzc.myshop.personal_activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.njxzc.myshop.ChooseTypeActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.UpdateListPicsAdapter;
import com.njxzc.myshop.customview.MyGridView;
import com.njxzc.myshop.utils.CompressHelper;
import com.njxzc.myshop.utils.DealComImageOther;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
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

public class UpdateComActivity extends AppCompatActivity {
    private ImageView iv_addbtn;
    private ImageView iv_reduce;
    private ImageView iv_add;
    private ArrayList<String> photodatas ;
    private ArrayList<String> comOtherImages ;
    private String comMainImage ;
    private UpdateListPicsAdapter updateListPicsAdapter;
    private LinearLayout ll_chooseType;
    private TextView tv_type;
    private TextView tv_counts;
    private EditText et_comName;
    private EditText et_comDesc;
    private EditText et_currentPrice;
    private EditText et_primePrice;
    private RadioGroup eg_isBargain;
    private RadioButton eb_noBargain;
    private RadioButton eb_isBargain;

    private MyGridView gv_listPic;
    public static int counts = 6;
    private int thirdId = 0;
    private String comImageMain;
    private String comImageOthers;
    private int comId;

    private ArrayList<Map<String,Object>> comDatas;
    private ArrayList<String> getphotos;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_com);
        initView();
        init();
        //广播接收分类activity传来的数据
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("getThirdName".equals(msg)) {
                    String thirdName = intent.getStringExtra("thirdName");
                    thirdId = intent.getIntExtra("thirdId",0);
                    tv_type.setText(thirdName);
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    private void initView() {
        iv_addbtn = (ImageView) findViewById(R.id.iv_addbtn);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_reduce = (ImageView) findViewById(R.id.iv_reduce);
        gv_listPic = (MyGridView) findViewById(R.id.gv_listPic);
        ll_chooseType = (LinearLayout) findViewById(R.id.ll_chooseType);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_counts = (TextView) findViewById(R.id.tv_counts);
        et_comName = (EditText) findViewById(R.id.et_comName);
        et_comDesc = (EditText) findViewById(R.id.et_comDesc);
        et_currentPrice = (EditText) findViewById(R.id.et_currentPrice);
        et_primePrice = (EditText) findViewById(R.id.et_primePrice);
        eg_isBargain = (RadioGroup) findViewById(R.id.eg_isBargain);
        eb_noBargain = (RadioButton) findViewById(R.id.eb_noBargain);
        eb_isBargain = (RadioButton) findViewById(R.id.eb_isBargain);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tv_counts.getText().toString());
                if (count>0&&count<10){
                    count++;
                }else {
                    Toast.makeText(UpdateComActivity.this,"数量最多只能10件",Toast.LENGTH_SHORT).show();
                }
                tv_counts.setText(String.valueOf(count));
            }
        });


        iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tv_counts.getText().toString());
                if (count>1){
                    count--;
                }
                tv_counts.setText(String.valueOf(count));
            }
        });

        //选择分类
        ll_chooseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateComActivity.this,ChooseTypeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        final Intent intent = getIntent();
        comDatas = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("datas");
        String des = (String) comDatas.get(0).get("des");
        String comName = (String) comDatas.get(0).get("comName");
        comImageMain = (String) comDatas.get(0).get("comImageMain");
        comImageOthers = (String) comDatas.get(0).get("comImageOthers");
        Double currentPrice = (Double) comDatas.get(0).get("currentPrice");
        Double primePrice = (Double) comDatas.get(0).get("primePrice");
        int isBargain = (int) comDatas.get(0).get("isBargain");
        comId = (int) comDatas.get(0).get("comId");
        int count = (int) comDatas.get(0).get("count");
        thirdId = (int) comDatas.get(0).get("thirdId");
        String thirdName = (String) comDatas.get(0).get("thirdName");
        et_comName.setText(comName);
        et_comDesc.setText(des);
        et_currentPrice.setText(String.valueOf(currentPrice));
        et_primePrice.setText(String.valueOf(primePrice));
        if (isBargain==1){
            eb_isBargain.setChecked(true);
        }else {
            eb_noBargain.setChecked(true);
        }
        tv_counts.setText(String.valueOf(count));
        tv_type.setText(thirdName);
        getphotos = new ArrayList<>();
        getphotos.add(comImageMain);
        getphotos.addAll(DealComImageOther.dealComImageOther(comImageOthers));
        counts -= getphotos.size();
        updateListPicsAdapter = new UpdateListPicsAdapter(this,getphotos);
        gv_listPic.setAdapter(updateListPicsAdapter);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                counts -= photos.size();
                getphotos.addAll(photos);
                updateListPicsAdapter = new UpdateListPicsAdapter(UpdateComActivity.this,getphotos);
                gv_listPic.setAdapter(updateListPicsAdapter);
                gv_listPic.setVisibility(View.VISIBLE);
            }
        }
    }

    public void submit(View v) throws Exception{
        final String comName = et_comName.getText().toString();
        final String comDes = et_comDesc.getText().toString();
        final ArrayList<String> comImagesOriginal = new ArrayList<String>();
        comMainImage = getphotos.get(0);//第一张给主图
        comOtherImages = new ArrayList<>();
        for (int i=1;i<getphotos.size();i++){//循环将第一张之后的图加到其他图片集合中
            File file = new File(getphotos.get(i)); //生成文件
            if (file.exists()){//如果文件存在 则说明是新增加的图片 将它添加到新增其他照片的数组中
                int j = 0;
                comOtherImages.add(j++,getphotos.get(i));
            }else {//如果文件不存在 则说明是已经上传的文件 将它添加到原本的商品其他图片路径
                int j = 0;
                comImagesOriginal.add(j++,getphotos.get(i));
            }
        }

        final int count = Integer.parseInt(tv_counts.getText().toString());
        int isBargain = 2;
        if (eb_isBargain.isChecked()){
            isBargain=1;
        }else if (eb_noBargain.isChecked()){
            isBargain=0;
        }
        String primePrice1 = et_primePrice.getText().toString();
        String currentPrice1 = et_currentPrice.getText().toString();
        final int finalIsBargain = isBargain;
        if (comName==""||comName.length()==0||comName==null){
            Toast.makeText(UpdateComActivity.this,"请填写商品名",Toast.LENGTH_SHORT).show();
        }else if(comDes==""||comDes.length()==0||comDes==null){
            Toast.makeText(UpdateComActivity.this,"请填写商品描述",Toast.LENGTH_SHORT).show();
        }else if(comDes.length()<10){
            Toast.makeText(UpdateComActivity.this,"商品描述至少10个字",Toast.LENGTH_SHORT).show();
        }else if(getphotos.size()<2){
            Toast.makeText(UpdateComActivity.this,"商品图片至少2张",Toast.LENGTH_SHORT).show();
        }else if(primePrice1==""||primePrice1.length()==0||primePrice1==null||!primePrice1.matches("^[0-9]+(.[0-9]{1,2})?$")){
            Toast.makeText(UpdateComActivity.this,"商品原价输入格式不正确",Toast.LENGTH_SHORT).show();
        }else if(currentPrice1==""||currentPrice1.length()==0||currentPrice1==null||!currentPrice1.matches("^[0-9]+(.[0-9]{1,2})?$")){
            Toast.makeText(UpdateComActivity.this,"商品现价输入格式不正确",Toast.LENGTH_SHORT).show();
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            final String updateTime = formatter.format(curDate);
            final double primePrice = Double.parseDouble(et_primePrice.getText().toString());
            final double currentPrice = Double.parseDouble(et_currentPrice.getText().toString());
            mProgressDialog = ProgressDialog.show(UpdateComActivity.this, "", "正在提交...");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    String url = OkhttpHelper.getIp() + "/shopsystem/androidCom/updateComPublished";
                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    for (int i = 0; i < comOtherImages.size(); i++) { //对文件进行遍历
                        File file = new File(comOtherImages.get(i)); //生成文件
                        File comImageOtherCompressed = CompressHelper.getDefault(getApplicationContext()).compressToFile(file);
                        requestBody.addFormDataPart( //给Builder添加上传的文件
                                "imageOtherFiles",  //请求的名字
                                file.getName(), //文件的文字，服务器端用来解析的
                                RequestBody.create(MediaType.parse("image/*"), comImageOtherCompressed) //创建RequestBody，把上传的文件放入
                        );

                    }
                    File comimageMain = new File(comMainImage);
                /*System.out.println(String.format("图片原质量大小 : %s", getReadableFileSize(comImageMain.length())));*/
                    //压缩图片
                    File comImageMainCompressed = CompressHelper.getDefault(getApplicationContext()).compressToFile(comimageMain);
                /*System.out.println(String.format("压缩图片质量大小 : %s", getReadableFileSize(comImageMainCompressed.length())));*/
                    requestBody.addFormDataPart( //给Builder添加上传的文件
                            "imageMainFile",  //请求的名字
                            comimageMain.getName(), //文件的文字，服务器端用来解析的
                            RequestBody.create(MediaType.parse("image/*"), comImageMainCompressed));//创建RequestBody，把上传的文件放入
                    requestBody.addFormDataPart("otherImageOriginalUrl", String.valueOf(comImagesOriginal));
                    requestBody.addFormDataPart("mainImageOriginalUrl", comMainImage);
                    requestBody.addFormDataPart("comName", comName);
                    requestBody.addFormDataPart("des", comDes);
                    requestBody.addFormDataPart("isBargain", String.valueOf(finalIsBargain));
                    requestBody.addFormDataPart("primePrice", String.valueOf(primePrice));
                    requestBody.addFormDataPart("currentPrice", String.valueOf(currentPrice));
                    requestBody.addFormDataPart("count", String.valueOf(count));
                    requestBody.addFormDataPart("typesId", String.valueOf(thirdId));
                    requestBody.addFormDataPart("comId", String.valueOf(comId));
                    requestBody.addFormDataPart("updateTime",updateTime);
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
                                    Toast.makeText(UpdateComActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            System.out.println(responseData);
                            if (responseData.equals("updateComSuccess")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                        intent.putExtra("flag", "refresh");
                                        LocalBroadcastManager.getInstance(UpdateComActivity.this).sendBroadcast(intent);
                                        sendBroadcast(intent);
                                        Toast.makeText(UpdateComActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        UpdateComActivity.this.finish();
                                    }
                                });

                            } else if (responseData.equals("updateComFail")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdateComActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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
