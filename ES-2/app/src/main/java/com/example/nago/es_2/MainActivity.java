package com.example.nago.es_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends FragmentActivity implements MaterialTabListener{
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tabHost = (MaterialTabHost)this.findViewById(R.id.tabHost);
        viewPager = (ViewPager) this.findViewById(R.id.pager);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap image = BitmapFactory.decodeResource(this.getResources(),R.drawable.backgnd2,options);
        tabHost.setBackground(new BitmapDrawable(getResources(),image));
        Bitmap image2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.backgnd,options);
        viewPager.setBackground(new BitmapDrawable(getResources(),image2));


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        Drawable[] arrDrawable = new Drawable[3];
        arrDrawable[0] = getResources().getDrawable(R.drawable.weather);
        arrDrawable[1]  = getResources().getDrawable(R.drawable.schedule);
        arrDrawable[2]  = getResources().getDrawable(R.drawable.option);

        for(int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setIcon(arrDrawable[i]).setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }


    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

}


