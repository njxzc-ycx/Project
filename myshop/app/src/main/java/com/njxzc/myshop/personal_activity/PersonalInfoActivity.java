package com.njxzc.myshop.personal_activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;

import com.mob.tools.network.MultiPart;
import com.njxzc.myshop.BuyActivity;
import com.njxzc.myshop.LoginActivity;
import com.njxzc.myshop.MainActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.utils.BitmapToFile;
import com.njxzc.myshop.utils.FileUtilcll;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalInfoActivity extends AppCompatActivity {
    private ImageView iv_pI_photo;
    private TextView tv_pI_nikeName;
    private TextView tv_pI_sex;
    private TextView tv_pI_birth;
    private TextView tv_pI_school;
    private LinearLayout ll_pI_photo;
    private LinearLayout ll_pI_nikeName;
    private LinearLayout ll_pI_sex;
    private LinearLayout ll_pI_birth;
    private LinearLayout ll_pI_address;
    private LinearLayout ll_pI_school;
    private ProgressDialog mProgressDialog;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    final int WRITE_EXTERNAL_STORE_REQUEST_CODE=1;

    TimePickerView pvTime;
    private SharedPreferences sp;
    String nickName;
    String profilePhoto;
    int sex;
    String birth;
    String school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //检查外部存储空间是否有权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PersonalInfoActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORE_REQUEST_CODE);
        }


        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("refreshNickName".equals(msg)){
                    String newNickName = intent.getStringExtra("nickName");
                    nickName = newNickName;
                    tv_pI_nikeName.setText(nickName);
                }else if("refreshSex".equals(msg)){
                    int newSex = intent.getIntExtra("sex",3);
                    sex = newSex;
                    if(sex==1){
                        tv_pI_sex.setText("男");
                    }else if(sex==0){
                        tv_pI_sex.setText("女");
                    }else {
                        tv_pI_sex.setText("未设置");
                    }
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
        initView();
        init();
        click();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }


    private void initView() {
        iv_pI_photo = (ImageView) findViewById(R.id.iv_pI_photo);
        tv_pI_nikeName = (TextView) findViewById(R.id.tv_pI_nikeName);
        tv_pI_sex = (TextView) findViewById(R.id.tv_pI_sex);
        tv_pI_birth = (TextView) findViewById(R.id.tv_pI_birth);
        tv_pI_school = (TextView) findViewById(R.id.tv_pI_school);
        ll_pI_photo = (LinearLayout) findViewById(R.id.ll_pI_photo);
        ll_pI_nikeName = (LinearLayout) findViewById(R.id.ll_pI_nikeName);
        ll_pI_sex = (LinearLayout) findViewById(R.id.ll_pI_sex);
        ll_pI_birth = (LinearLayout) findViewById(R.id.ll_pI_birth);
        ll_pI_address = (LinearLayout) findViewById(R.id.ll_pI_address);
        ll_pI_school = (LinearLayout) findViewById(R.id.ll_pI_school);
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(2000,0,1);//默认选中时间
        Time t=new Time("GMT+8");
        t.setToNow(); // 取得系统时间
        int year = t.year;
        int month = t.month;
        int day = t.monthDay;
        Calendar startDate = Calendar.getInstance();
        startDate.set(1950, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //将结束时间设为系统当天日期
        endDate.set(year, month, day);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                TextView btn = (TextView) v;
                btn.setText(getTimes(date));
                final int userId = sp.getInt("userId",0);
                final String newBirth = getTimes(date);
                //start the progress dialog
                mProgressDialog = ProgressDialog.show(PersonalInfoActivity.this, "", "正在提交...");
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            String url =  OkhttpHelper.getIp()+"/shopsystem/androidUser/updateBirth";
                            OkhttpHelper.updateBirth(url, newBirth,String.valueOf(userId), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PersonalInfoActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            // dismiss the progress dialog
                                            mProgressDialog.dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String responseData = response.body().string();
                                    System.out.println(responseData);
                                    if(responseData.equals("updateBirthFail")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(PersonalInfoActivity.this, "生日修改失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else if(responseData.equals("updateBirthSuccess")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(PersonalInfoActivity.this, "生日修改成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    // dismiss the progress dialog
                                    mProgressDialog.dismiss();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true,false,false,false})
                .setLabel("年", "月", "日","","","")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(16)//字号
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .setTitleText("请选择时间")
                .build();
    }

    private void init() {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidUser/findUserByUserId";
                    OkhttpHelper.findUserByUserId(url, String.valueOf(userId), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PersonalInfoActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseData = response.body().string();
                            System.out.println(responseData);
                            try {
                                JSONObject user = new JSONObject(responseData);
                                profilePhoto = user.getString("profilePhoto");
                                nickName = user.getString("nickName");
                                sex = user.getInt("sex");
                                birth = user.getString("birth");
                                school = user.getString("school");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(PersonalInfoActivity.this)
                                                .load(profilePhoto).asBitmap()
                                                /*.apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))*/
                                                .error(R.drawable.errorpic)
                                                .placeholder(R.drawable.defaultpic)
                                                .into(iv_pI_photo);
                                        tv_pI_nikeName.setText(nickName);
                                        if(sex==1){
                                            tv_pI_sex.setText("男");
                                        }else if(sex==0){
                                            tv_pI_sex.setText("女");
                                        }else {
                                            tv_pI_sex.setText("未设置");
                                        }
                                        if(birth.equals("null")){
                                            tv_pI_birth.setText("未设置");
                                        }else {
                                            tv_pI_birth.setText(birth);
                                        }
                                        tv_pI_school.setText(school);


                                    }
                                });

                            } catch (JSONException e) {
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

    private void click() {
        //头像点击
        ll_pI_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PersonalInfoActivity.this);
                builder.setTitle("修改头像");
                String[] items = { "选择本地照片", "拍照" };
                builder.setNegativeButton("取消", null);
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case CHOOSE_PICTURE: // 选择本地照片
                                Intent openAlbumIntent = new Intent(
                                        Intent.ACTION_PICK);
                                openAlbumIntent.setType("image/*");
                                //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                break;
                            case TAKE_PICTURE: // 拍照

                                if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(PersonalInfoActivity.this,new String[]{android.Manifest.permission.CAMERA},1);
                                }else {
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp_image.jpg"));
                                    // 将拍照所得的相片保存到SD卡根目录
                                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                                }




                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        //昵称点击
        ll_pI_nikeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfoActivity.this,UpdateNickNameActivity.class);
                intent.putExtra("nickName",nickName);
                startActivity(intent);
            }
        });
        //性别点击
        ll_pI_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfoActivity.this,UpdateSexActivity.class);
                intent.putExtra("sex",sex);
                startActivity(intent);
            }
        });
        //生日点击
        ll_pI_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pvTime!=null){
                    pvTime.show(tv_pI_birth);
                }
            }
        });
        //地址点击
        ll_pI_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfoActivity.this,AddressesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理

                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            /*iv_pI_photo.setImageBitmap(mBitmap);//显示图片*/
            //上传该图片到服务器的代码
            final File file = BitmapToFile.getFile(mBitmap);
            final String photoOriginalUrl = sp.getString("profilePhoto","");
            final int userId = sp.getInt("userId",0);
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    String url =  OkhttpHelper.getIp()+"/shopsystem/androidUser/updatePhoto";
                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"),file);
                    requestBody.addFormDataPart("photoOriginalUrl",photoOriginalUrl);
                    requestBody.addFormDataPart("file",file.getName(),fileBody);
                    requestBody.addFormDataPart("userId", String.valueOf(userId));
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoActivity.this);
                                    builder.setTitle("提示");
                                    builder.setMessage("您未开启存储权限，无法进行头像上传");
                                    //确定按钮
                                    builder.setPositiveButton("开启权限", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri1 = Uri.fromParts("package", PersonalInfoActivity.this.getPackageName(), null);
                                            intent.setData(uri1);
                                            startActivity(intent);
                                        }
                                    });
                                    //显示对话框
                                    builder.create().show();

                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            final String responseData = response.body().string();
                            if(responseData.equals("updateFail")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PersonalInfoActivity.this, "头像修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PersonalInfoActivity.this, "头像修改成功", Toast.LENGTH_SHORT).show();
                                        String newPhoto = responseData;
                                        sp.edit().putString("profilePhoto",newPhoto).commit();
                                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                        intent.putExtra("flag","refreshProfilePhoto");
                                        LocalBroadcastManager.getInstance(PersonalInfoActivity.this).sendBroadcast(intent);
                                        sendBroadcast(intent);
                                        iv_pI_photo.setImageBitmap(mBitmap);//显示图片
                                    }
                                });
                            }
                        }
                    });


                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==WRITE_EXTERNAL_STORE_REQUEST_CODE){
            for (int i=0;i<permissions.length;i++){
                if (grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(PersonalInfoActivity.this,"权限获取失败！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //时间选择器
    private String getTimes(Date date) {
        //年月日格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }
}
