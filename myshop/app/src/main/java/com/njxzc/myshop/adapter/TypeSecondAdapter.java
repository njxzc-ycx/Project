package com.njxzc.myshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.CategoryActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.TypeComActivity;
import com.njxzc.myshop.customview.MyGridView;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class TypeSecondAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;

    private ArrayList<Map<String,Object>> typeThirdDatas;

    private TypeThirdAdapter typeThirdAdapter;




    public TypeSecondAdapter(Context context, List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
            return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        Model.getInstance().init(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.typesecond_style,null);
        //根据view对象查找样式文件中需要显示的组件
        TextView tv_secondName = view.findViewById(R.id.tv_secondName);
        final MyGridView gv_typeThird = view.findViewById(R.id.gv_typeThird);

        //解决listview中嵌套gridview无法点击问题
        gv_typeThird.setClickable(true);
        gv_typeThird.setPressed(true);
        gv_typeThird.setEnabled(true);

        //取出每一行需要显示的数据
        final Map<String,Object> hm = data.get(position);
        String secondName= (String)hm.get("secondName");
        typeThirdDatas= (ArrayList<Map<String, Object>>) hm.get("typeThirdDatas");
        typeThirdAdapter = new TypeThirdAdapter(context,typeThirdDatas);
        gv_typeThird.setAdapter(typeThirdAdapter);
        //把显示的值赋给组件
        tv_secondName.setText(secondName);

        gv_typeThird.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context,TypeComActivity.class);
                typeThirdDatas = new ArrayList<Map<String, Object>>();
                typeThirdDatas = (ArrayList<Map<String, Object>>) hm.get("typeThirdDatas");
                intent.putExtra("position",i);
                intent.putExtra("datas",(Serializable) typeThirdDatas);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
