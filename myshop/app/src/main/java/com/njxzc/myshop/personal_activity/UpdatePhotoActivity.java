package com.njxzc.myshop.personal_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.njxzc.myshop.R;

import java.io.File;

public class UpdatePhotoActivity extends AppCompatActivity {
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mms" ;//获取自定义SD路径
    private File photo_file=new File(path);//生成该路径的文件
    private String photoPath;
    private Button btn_photo;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_photo);
        btn_photo = (Button) findViewById(R.id.btn_photo);
    }

    /**\
     * 功能：拍照 （启动相机）
     * @param view
     */
    public void Btn_photo(View view){
        // 激活系统图库，选择一张图片
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent1,PHOTO_REQUEST_GALLERY);
    }

}
