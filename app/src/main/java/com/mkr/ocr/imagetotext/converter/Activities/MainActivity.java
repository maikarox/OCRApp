package com.mkr.ocr.imagetotext.converter.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.mkr.ocr.imagetotext.converter.Fragments.FragmentAdapter;
import com.mkr.ocr.imagetotext.converter.Fragments.FragmentDashboard;
import com.mkr.ocr.imagetotext.converter.Fragments.FragmentHome;
import com.mkr.ocr.imagetotext.converter.Fragments.FragmentSaved;
import com.mkr.ocr.imagetotext.converter.R;
import com.mkr.ocr.imagetotext.converter.Utilities.UtilityLib;


public class MainActivity extends AppCompatActivity  {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    static SharedPreferences sharedPreferences;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager, Context context){
        FragmentAdapter Adapter = new FragmentAdapter(fragmentManager);
        Adapter.add(new FragmentHome(), context.getString(R.string.home_tab));
        Adapter.add(new FragmentSaved(), context.getString(R.string.saved));
        Adapter.add(new FragmentDashboard(), context.getString(R.string.settings_ttab));
        viewPager.setAdapter(Adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        //Init Viewpager
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        //Setup Fragment
        setupFm(getSupportFragmentManager(), viewPager, MainActivity.this);
        //Set Active Item When Activity Start
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new PageChange());

    }



    public class PageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:
                    viewPager.setCurrentItem(0);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    break;
                case 1:
                    viewPager.setCurrentItem(1);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
                    break;
                case 2:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
                    viewPager.setCurrentItem(2);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String googlePlayUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String marketUrl = "market://details?id=" + getPackageName();
        if (id == R.id.menu_rate) {
            UtilityLib.rateTheApp(this, marketUrl, googlePlayUrl);
        } else if (id == R.id.menu_pp) {

           UtilityLib.openWeb(this, "https://minomak.blogspot.com/p/privacy-policy.html");
        }else if(id == R.id.menu_share){
           shareApp(googlePlayUrl);
        }
        return super.onOptionsItemSelected(item);
    }
    private void shareApp(String googlePlayUrl){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, googlePlayUrl);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
