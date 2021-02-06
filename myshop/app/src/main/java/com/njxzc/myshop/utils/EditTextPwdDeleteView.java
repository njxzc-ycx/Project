package com.njxzc.myshop.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.njxzc.myshop.R;

/**
 * Created by 殷晨旭 on 2021/1/30.
 */

public class EditTextPwdDeleteView extends RelativeLayout {

    private ImageView img_delPwd;
    private EditText editTextPwd;
    private RelativeLayout relativeLayout;

    public EditTextPwdDeleteView(Context context) {
        super(context);
    }

    public EditTextPwdDeleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取xml布局文件中的自定义属性的值
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.EditTextDeleteView);
        Drawable drawable = array.getDrawable(R.styleable.EditTextDeleteView_drawable);
        CharSequence hint = array.getText(R.styleable.EditTextDeleteView_hint);
        //初始化组合控件布局
        relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.edittext_del_pwd_layout,this,true);
        img_delPwd = relativeLayout.findViewById(R.id.img_delPwd);
        editTextPwd = relativeLayout.findViewById(R.id.editTextPwd);
        //给控件赋值
        editTextPwd.setHint(hint);
        if(drawable != null)
        {
            img_delPwd.setImageDrawable(drawable);
        }

        //给控件添加事件,点击删除图片的时候将EditText的文字内容设置为空字符
        img_delPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPwd.setText("");
            }
        });

        //在EditText中的内容发生改变时,控制删除按钮的显示和隐藏
        editTextPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0)
                {
                    img_delPwd.setVisibility(VISIBLE);
                }else
                {
                    img_delPwd.setVisibility(GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //回收TypedArray
        array.recycle();



    }

    public String getText()
    {
        return editTextPwd.getText().toString();
    }

    public void setText(String str)
    {
        editTextPwd.setText(str);
    }

    public void setDeleteImageResources(int id)
    {
        img_delPwd.setImageResource(id);
    }

    public void setHint(CharSequence hint)
    {
        editTextPwd.setHint(hint);
    }



}
