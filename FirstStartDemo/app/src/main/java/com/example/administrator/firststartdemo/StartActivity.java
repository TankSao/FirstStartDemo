package com.example.administrator.firststartdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private SharePreferenceHelper sharePreferenceHelper;
    private long lastTime;
    private TextView start;
    private ViewPager firshVp;
    private TextView text;
    private LinearLayout linear;
    private int images[] = new int[]{R.mipmap.vp1, R.mipmap.vp2, R.mipmap.vp3};
    private ArrayList<View> views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        firshVp = findViewById(R.id.firsh_vp);
        linear = findViewById(R.id.linear);
        text = findViewById(R.id.text);
        start = (TextView) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,MainActivity.class));
                finish();
            }
        });
        sharePreferenceHelper = new SharePreferenceHelper(this,"start_time");
        lastTime = (long)sharePreferenceHelper.getSharedPreference("last_time",Long.parseLong("0"));
        long currentTime = System.currentTimeMillis();
        if(lastTime == 0){
            //第一次启动
            sharePreferenceHelper.put("last_time",currentTime);
            initVp();
        }else{
            if(currentTime - lastTime <= 24*60*60*1000){
                //上一次启动距离现在小于一天
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }else{
                initVp();
            }
            sharePreferenceHelper.put("last_time",currentTime);
        }
    }

    //初始化欢迎导航
    private void initVp() {
        views = new ArrayList<View>();
        for (int i = 0; i < 3; i++) {
            View view = View.inflate(this, R.layout.vp_item, null);
            ImageView imageView = view.findViewById(R.id.iv_vp);
            imageView.setImageResource(images[i]);
            views.add(view);
        }
        text.setText("1/"+images.length);
        firshVp.setAdapter(new MyPagerAdapter(views, this));
        firshVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                text.setText(position+1+"/"+images.length);
                if (position == 2) {
                    start.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

class MyPagerAdapter extends PagerAdapter {
    private ArrayList<View> simpleDraweeViews;
    private Context mContext;

    //main
    public MyPagerAdapter(ArrayList<View> simpleDraweeViews, Context mContext) {
        this.simpleDraweeViews = simpleDraweeViews;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return simpleDraweeViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(simpleDraweeViews.get(position));
        return simpleDraweeViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(simpleDraweeViews.get(position));
    }
}
