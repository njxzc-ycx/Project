package com.njxzc.myshop;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.njxzc.myshop.adapter.ListComPicsAdapter;
import com.njxzc.myshop.adapter.ListPicsAdapter;
import com.njxzc.myshop.customview.MyGridView;
import com.njxzc.myshop.personal_activity.UpdateNickNameActivity;
import com.njxzc.myshop.utils.CompressHelper;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;
import com.njxzc.myshop.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import static com.hyphenate.util.UriUtils.getMimeType;

public class PublishComActivity extends AppCompatActivity {
    private ImageView iv_addbtn;
    private ImageView iv_reduce;
    private ImageView iv_add;
    private ArrayList<String> photodatas;
    private ArrayList<String> comOtherImages ;
    private String comMainImage ;
    private ListComPicsAdapter listPicsAdapter;
    private LinearLayout ll_chooseType;
    private TextView tv_type;
    private TextView tv_counts;
    private TextView tv_textCount;
    private EditText et_comName;
    private EditText et_comDesc;
    private EditText et_currentPrice;
    private EditText et_primePrice;
    private RadioGroup eg_isBargain;
    private RadioButton eb_noBargain;
    private RadioButton eb_isBargain;
    private int num = 150;
    private MyGridView gv_listPic;
    public static int count = 6;
    int thirdId = 0;
    private SharedPreferences sp;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_com);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Model.getInstance().init(this);
        initView();
        photodatas = new ArrayList<>();
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
                        .start(PublishComActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });

        //选择分类
        ll_chooseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishComActivity.this,ChooseTypeActivity.class);
                startActivity(intent);
            }
        });
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
        tv_textCount = (TextView) findViewById(R.id.tv_textCount);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tv_counts.getText().toString());
                if (count>0&&count<10){
                    count++;
                }else {
                    Toast.makeText(PublishComActivity.this,"数量最多只能10件",Toast.LENGTH_SHORT).show();
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

        et_comDesc.addTextChangedListener(new TextWatcher() {
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
                selectionStart=et_comDesc.getSelectionStart();
                selectionEnd = et_comDesc.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    et_comDesc.setText(s);
                    et_comDesc.setSelection(tempSelection);//设置光标在最后
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
                listPicsAdapter = new ListComPicsAdapter(PublishComActivity.this,photodatas);
                gv_listPic.setAdapter(listPicsAdapter);
                gv_listPic.setVisibility(View.VISIBLE);
            }
        }
    }

    public void publish(View v) throws Exception{
        final int userId = sp.getInt("userId",0);
        final String comName = et_comName.getText().toString();
        final String comDes = et_comDesc.getText().toString();
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
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        final String inTime = formatter.format(curDate);
        if (comName==""||comName.length()==0||comName==null){
            Toast.makeText(PublishComActivity.this,"请填写商品名",Toast.LENGTH_SHORT).show();
        }else if(comDes==""||comDes.length()==0||comDes==null){
            Toast.makeText(PublishComActivity.this,"请填写商品描述",Toast.LENGTH_SHORT).show();
        }else if(comDes.length()<10){
            Toast.makeText(PublishComActivity.this,"商品描述至少10个字",Toast.LENGTH_SHORT).show();
        }else if(photodatas.size()<2){
            Toast.makeText(PublishComActivity.this,"商品图片至少2张",Toast.LENGTH_SHORT).show();
        }else if(primePrice1==""||primePrice1.length()==0||primePrice1==null||!primePrice1.matches("^[0-9]+(.[0-9]{1,2})?$")){
            Toast.makeText(PublishComActivity.this,"商品原价输入格式不正确",Toast.LENGTH_SHORT).show();
        }else if(currentPrice1==""||currentPrice1.length()==0||currentPrice1==null||!currentPrice1.matches("^[0-9]+(.[0-9]{1,2})?$")){
            Toast.makeText(PublishComActivity.this,"商品现价输入格式不正确",Toast.LENGTH_SHORT).show();
        }else if(isBargain==2){
            Toast.makeText(PublishComActivity.this,"请选择是否支持讲价",Toast.LENGTH_SHORT).show();
        }else if(thirdId==0){
            Toast.makeText(PublishComActivity.this,"请选择商品分类",Toast.LENGTH_SHORT).show();
        }
        else {
            comMainImage = photodatas.get(0);//第一张给主图
            comOtherImages = new ArrayList<>();
            for (int i=1;i<photodatas.size();i++){//循环将第一张之后的图加到其他图片集合中
                int j = 0;
                comOtherImages.add(j++,photodatas.get(i));
            }
            System.out.println(comMainImage);
            System.out.println(comOtherImages);
            final double primePrice = Double.parseDouble(et_primePrice.getText().toString());
            final double currentPrice = Double.parseDouble(et_currentPrice.getText().toString());
            mProgressDialog = ProgressDialog.show(PublishComActivity.this, "", "正在发布...");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidCom/addCom";
                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    for (int i = 0; i < comOtherImages.size(); i++) { //对文件进行遍历
                        File file = new File(comOtherImages.get(i)); //生成文件
                        //压缩图片
                        File comImageOtherCompressed = CompressHelper.getDefault(getApplicationContext()).compressToFile(file);
                        requestBody.addFormDataPart( //给Builder添加上传的文件
                                "imageOtherFiles",  //请求的名字
                                file.getName(), //文件的文字，服务器端用来解析的
                                RequestBody.create(MediaType.parse("image/*"), comImageOtherCompressed) //创建RequestBody，把上传的文件放入
                        );
                    }
                    File comImageMain = new File(comMainImage);
                /*System.out.println(String.format("图片原质量大小 : %s", getReadableFileSize(comImageMain.length())));*/
                    //压缩图片
                    File comImageMainCompressed = CompressHelper.getDefault(getApplicationContext()).compressToFile(comImageMain);
                /*System.out.println(String.format("压缩图片质量大小 : %s", getReadableFileSize(comImageMainCompressed.length())));*/
                    requestBody.addFormDataPart( //给Builder添加上传的文件
                            "imageMainFile",  //请求的名字
                            comImageMain.getName(), //文件的文字，服务器端用来解析的
                            RequestBody.create(MediaType.parse("image/*"), comImageMainCompressed));//创建RequestBody，把上传的文件放入
                    requestBody.addFormDataPart("sellerId", String.valueOf(userId));
                    requestBody.addFormDataPart("comName", comName);
                    requestBody.addFormDataPart("des", comDes);
                    requestBody.addFormDataPart("inTime", inTime);
                    requestBody.addFormDataPart("isBargain", String.valueOf(finalIsBargain));
                    requestBody.addFormDataPart("primePrice", String.valueOf(primePrice));
                    requestBody.addFormDataPart("currentPrice", String.valueOf(currentPrice));
                    requestBody.addFormDataPart("count", String.valueOf(count));
                    requestBody.addFormDataPart("typesId", String.valueOf(thirdId));

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
                                    Toast.makeText(PublishComActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            System.out.println(responseData);
                            if (responseData.equals("addComSuccess")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PublishComActivity.this,"发布二手成功",Toast.LENGTH_SHORT).show();
                                        /*Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                        intent.putExtra("flag","refreshCom");
                                        LocalBroadcastManager.getInstance(PublishComActivity.this).sendBroadcast(intent);
                                        sendBroadcast(intent);*/
                                        PublishComActivity.this.finish();
                                    }
                                });
                            }else if (responseData.equals("addComFail")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PublishComActivity.this,"发布二手失败",Toast.LENGTH_SHORT).show();
                                        PublishComActivity.this.finish();
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

    //获取图片质量大小
    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void backHomepage(View v) throws Exception{
        this.finish();
    }
}
