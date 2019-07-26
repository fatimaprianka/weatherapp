package com.example.shamim.weather.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shamim.weather.Adapter.ForecastWeatherAdapter;
import com.example.shamim.weather.MainActivity;
import com.example.shamim.weather.R;
import com.example.shamim.weather.WeatherServiceAPI;
import com.example.shamim.weather.model.CurrentWeatherResponse;
import com.example.shamim.weather.model.ForecastWeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastWeather extends Fragment {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private WeatherServiceAPI weatherServiceAPI;
    /*private double latitude = 23.750854;
    private double longitude = 90.393527;*/
    private double latitude,longitude;
    private String unit="metric";
    private int count=20;
    private RecyclerView recyclerView;
    private ForecastWeatherAdapter adapter;
    private FusedLocationProviderClient client;

    private TextView temTv,dateTv,cityTv;


    public ForecastWeather() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast_weather, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        getDeviceCurrentLocation();



        return view;
        }

        public void getForecastWeatherData(){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            weatherServiceAPI = retrofit.create(WeatherServiceAPI.class);

            String api = getString(R.string.weather_api_key);
            String customUrl = String.format("forecast?lat=%f&lon=%f&units=%s&cnt=%d&appid=%s", latitude, longitude, unit, count, api);

            Call<ForecastWeatherResponse> forecastWeatherResponseCall = weatherServiceAPI.getForecastWeather(customUrl);
            forecastWeatherResponseCall.enqueue(new Callback<ForecastWeatherResponse>() {
                @Override
                public void onResponse(Call<ForecastWeatherResponse> call, Response<ForecastWeatherResponse> response) {
                    if (response.code() == 200) {
                        ForecastWeatherResponse forecastWeatherResponse = response.body();
                        // Toast.makeText(getActivity(), "temp: " + forecastWeatherResponse.getMain().getTemp(), Toast.LENGTH_SHORT).show();

                        List<ForecastWeatherResponse.ListFor> lists = new ArrayList<>();
                        lists = forecastWeatherResponse.getList();
                        //Toast.makeText(getActivity(), ""+lists.size(), Toast.LENGTH_SHORT).show();
                        adapter = new ForecastWeatherAdapter(getActivity(), lists);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setAdapter(adapter);

                        Toast.makeText(getActivity(), "Forecast Value Retrived", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Did not get value", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ForecastWeatherResponse> call, Throwable t) {
                    Log.e("forecast", "onFailure: " + t.getMessage());
                }
            });

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==11 && grantResults[0]== PackageManager.PERMISSION_GRANTED);

        getDeviceCurrentLocation();

    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},11);

            return false;
        }
        return true;

    }
    public void getDeviceCurrentLocation() {
        if (checkLocationPermission()) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        return;
                    }
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Toast.makeText(getActivity(), "Forecast Lat:" + latitude + "  " + "lon:" + longitude, Toast.LENGTH_SHORT).show();

                    getForecastWeatherData();


                }
            });

        } else {
            checkLocationPermission();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.convertFarenhite:
                unit="imperial";
                getDeviceCurrentLocation();
                break;

            case  R.id.convertCelcius:
                unit="metric";
                getDeviceCurrentLocation();
        }
        return super.onOptionsItemSelected(item);
    }


}
