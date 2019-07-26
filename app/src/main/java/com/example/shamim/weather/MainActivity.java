package com.example.shamim.weather;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shamim.weather.fragment.CurrentWeather;
import com.example.shamim.weather.fragment.ForecastWeather;
import com.example.shamim.weather.model.Constant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity  {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private FusedLocationProviderClient client;
   // private boolean isConvertToCelcius=false;
   Context context;
   public String query;
    private android.support.v7.widget.Toolbar toolbar;
  //  @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);


        client = LocationServices.getFusedLocationProviderClient(this);

        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("5 days Forecast"));

      /*  Intent intent =getIntent();
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
           query =intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(context, ""+query, Toast.LENGTH_SHORT).show();

        }*/

        //String activity= this.getClass().getSimpleName();
        CurrentPagerAdapter adapter = new CurrentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());



        viewPager.setAdapter(adapter);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
@Override
public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        }

@Override
public void onTabUnselected(TabLayout.Tab tab) {

        }

@Override
public void onTabReselected(TabLayout.Tab tab) {

        }
        });

        }

private class CurrentPagerAdapter extends FragmentPagerAdapter {
    private int tabCount;

    public CurrentPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CurrentWeather();
            case 1:
                return new ForecastWeather();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.search,menu);
    *//*    SearchManager manager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menu.findItem(R.id.searching).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);*//*
        return true;
    }*/
/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem convertToFarenhite= menu.findItem(R.id.convertFarenhite);
        MenuItem convertToCelcius=menu.findItem(R.id.convertCelcius);
        String activity= this.getClass().getSimpleName();

        if(activity.equals(Constant.WeatherLocation.CELCIUS)){
            convertToFarenhite.setVisible(false);
            convertToCelcius.setVisible(true);
        }
        else{
            convertToCelcius.setVisible(false);
            convertToFarenhite.setVisible(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment=null;
        switch (item.getItemId()){
            case R.id.convertFarenhite:
                isConvertToCelcius=false;
                currentWeather=new CurrentWeather();
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,currentWeather).commit();
                //Intent intent=getIntent();
                break;

            case R.id.convertCelcius:
                isConvertToCelcius=true;

                break;
        }
        return true;
    }*/

/*
    public void passVal(FragmentCommunicator fragmentCommunicator) {
        fragmentCommunicator.passData("metric");
        this.fragmentCommunicator = fragmentCommunicator;

    }
*/




}