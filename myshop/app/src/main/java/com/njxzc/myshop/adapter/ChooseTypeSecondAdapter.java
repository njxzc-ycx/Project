package com.njxzc.myshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njxzc.myshop.ChooseTypeActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.TypeComActivity;
import com.njxzc.myshop.customview.MyGridView;
import com.njxzc.myshop.utils.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class ChooseTypeSecondAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;

    private ArrayList<Map<String,Object>> typeThirdDatas;

    private TypeThirdAdapter typeThirdAdapter;




    public ChooseTypeSecondAdapter(Context context, List<Map<String,Object>> data){
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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                typeThirdDatas = new ArrayList<Map<String, Object>>();
                typeThirdDatas = (ArrayList<Map<String, Object>>) hm.get("typeThirdDatas");
                String thirdName = (String) typeThirdDatas.get(position).get("thirdName");
                int thirdId = (int) typeThirdDatas.get(position).get("thirdId");
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("flag","getThirdName");
                intent.putExtra("thirdName",thirdName);
                intent.putExtra("thirdId",thirdId);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                context.sendBroadcast(intent);
                if(Activity.class.isInstance(context))
                {
                    // 转化为activity，然后finish就行了
                    Activity activity = (Activity)context;
                    activity.finish();
                }
            }
        });

        return view;
    }
}
