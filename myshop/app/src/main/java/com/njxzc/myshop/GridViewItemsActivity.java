package com.njxzc.myshop;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class GridViewItemsActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private LinearLayout outsideLayout;
    //接收Intent传递过来的图片URL
    ArrayList<String> list = new ArrayList<>();
    //获取点击的图片的position
    private int imagePosition;
    //使用ViewPager和CirclePageIndicator来显示图片给用户查看
    private ViewPager image_banner;
    private TextView tv_page;
    private ViewPagerPhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_items);
        list = getIntent().getStringArrayListExtra("list");
        imagePosition = getIntent().getIntExtra("position", 0);
        initView();
        initData();
    }

    public void initView() {
        outsideLayout = (LinearLayout) findViewById(R.id.outside_layout);
        outsideLayout.setOnClickListener(this);
        image_banner = (ViewPager) findViewById(R.id.image_banner);
        tv_page = (TextView) findViewById(R.id.tv_page);
        image_banner.setOnPageChangeListener(this);
        //设置当选择第一张照片时的页码
        if (imagePosition==0){
            tv_page.setText((imagePosition+1)+"/"+list.size());
        }
    }

    private void initData() {
        setData(list);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.outside_layout:
                finish();
                break;
            default:
                break;
        }
    }

    public void setData(List<String> imageList) {
        adapter = new ViewPagerPhotosAdapter(imageList);
        image_banner.setAdapter(adapter);
        image_banner.setCurrentItem(imagePosition);//设置viewpager的初始化位置
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //这个方法的调用是viewPager停下来以后选中的位置
        int realPosition;
        if(adapter.getDataRealSize() != 0){
            realPosition = position % adapter.getDataRealSize();
            System.out.println(realPosition);
        }else {
            realPosition = 0;
        }
        tv_page.setText((realPosition+1)+"/"+adapter.getDataRealSize());


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ViewPagerAdapter
     */
    private class ViewPagerPhotosAdapter extends PagerAdapter {

        private List<String> imageList;

        public ViewPagerPhotosAdapter(List<String> imageList) {
            this.imageList = imageList;
        }

        @Override
        public int getCount() {
            return imageList == null ? 0 : imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(GridViewItemsActivity.this).inflate(R.layout.item_diary_image_item, null);
            PhotoView image = view.findViewById(R.id.img);
                Glide.with(GridViewItemsActivity.this)
                        .load(imageList.get(position))
                        .into(image);
            container.addView(view);


            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public int getDataRealSize(){
            if(imageList != null){
                return imageList.size();
            }
            return  0;
        }
    }


}
